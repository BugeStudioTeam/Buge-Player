package com.buge.player.util

import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.core.content.FileProvider
import com.buge.player.data.BugeMedia
import com.buge.player.data.MediaKind
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.net.HttpURLConnection
import java.net.URL

/** Shares the media object itself, never a text-only URL. */
object MediaFileSharer {
    private const val MAX_DOWNLOAD_BYTES = 512L * 1024L * 1024L

    class HlsStreamException : IllegalArgumentException()

    suspend fun share(context: Context, media: BugeMedia) {
        val (shareUri, mimeType) = withContext(Dispatchers.IO) { prepare(context, media) }
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = mimeType
            putExtra(Intent.EXTRA_STREAM, shareUri)
            putExtra(Intent.EXTRA_TITLE, media.title)
            clipData = ClipData.newRawUri(media.title, shareUri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(intent, media.title))
    }

    private fun prepare(context: Context, media: BugeMedia): Pair<Uri, String> {
        val source = Uri.parse(media.uri)
        return when (source.scheme?.lowercase()) {
            "content" -> source to (context.contentResolver.getType(source) ?: mimeFor(media))
            "file" -> copyLocalToCache(context, File(requireNotNull(source.path)), media)
            "http", "https" -> downloadRemote(context, media)
            else -> throw IllegalArgumentException("Unsupported media location")
        }
    }

    private fun copyLocalToCache(context: Context, source: File, media: BugeMedia): Pair<Uri, String> {
        require(source.isFile) { "Local media file is unavailable" }
        val extension = source.extension.ifBlank { extensionFrom(media.uri, "", media.kind) }
        val target = createShareFile(context, media, extension)
        source.inputStream().use { input -> target.outputStream().use { output -> input.copyTo(output) } }
        return fileUri(context, target) to mimeFor(media)
    }

    private fun downloadRemote(context: Context, media: BugeMedia): Pair<Uri, String> {
        if (media.uri.substringBefore('?').endsWith(".m3u8", ignoreCase = true)) throw HlsStreamException()
        val connection = (URL(media.uri).openConnection() as HttpURLConnection).apply {
            instanceFollowRedirects = true
            connectTimeout = 15_000
            readTimeout = 30_000
            requestMethod = "GET"
        }
        try {
            val code = connection.responseCode
            if (code !in 200..299) throw IllegalStateException("Media download failed: HTTP $code")
            val type = connection.contentType?.substringBefore(';')?.trim().orEmpty()
            if (type.contains("mpegurl", ignoreCase = true) || type.contains("vnd.apple.mpegurl", ignoreCase = true)) throw HlsStreamException()
            val extension = extensionFrom(media.uri, type, media.kind)
            val file = createShareFile(context, media, extension)
            var copied = 0L
            connection.inputStream.use { input ->
                file.outputStream().use { output ->
                    val buffer = ByteArray(32 * 1024)
                    while (true) {
                        val count = input.read(buffer)
                        if (count < 0) break
                        copied += count
                        if (copied > MAX_DOWNLOAD_BYTES) throw IllegalStateException("Media file is larger than 512 MB")
                        output.write(buffer, 0, count)
                    }
                }
            }
            return fileUri(context, file) to if (type.isNotBlank()) type else mimeFor(media)
        } finally {
            connection.disconnect()
        }
    }

    private fun createShareFile(context: Context, media: BugeMedia, extension: String): File {
        val folder = File(context.cacheDir, "shared_media").apply { mkdirs() }
        folder.listFiles()?.forEach { if (it.lastModified() < System.currentTimeMillis() - 86_400_000L) it.delete() }
        return File(folder, "${safeName(media.title)}-${System.currentTimeMillis()}.$extension")
    }

    private fun fileUri(context: Context, file: File): Uri =
        FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)

    private fun extensionFrom(url: String, type: String, kind: MediaKind): String {
        val pathExtension = MimeTypeMap.getFileExtensionFromUrl(url.substringBefore('?'))
        if (pathExtension.isNotBlank()) return pathExtension.lowercase()
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(type)?.lowercase()
            ?: if (kind == MediaKind.VIDEO) "mp4" else "mp3"
    }

    private fun mimeFor(media: BugeMedia): String = when (media.kind) {
        MediaKind.AUDIO -> "audio/*"
        MediaKind.VIDEO -> "video/*"
        MediaKind.AUTO -> if (media.uri.contains(".mp4", ignoreCase = true) || media.uri.contains(".mkv", ignoreCase = true)) "video/*" else "audio/*"
    }

    private fun safeName(name: String): String = name.replace(Regex("[^a-zA-Z0-9._-]"), "_").take(72).ifBlank { "buge-media" }
}
