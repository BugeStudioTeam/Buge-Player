package com.buge.player.media

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.OpenableColumns
import androidx.core.content.IntentCompat
import com.buge.player.R
import com.buge.player.data.BugeMedia
import com.buge.player.data.MediaKind

object ExternalMediaIntent {
    fun toBugeMedia(context: Context, intent: Intent?): BugeMedia? {
        intent ?: return null
        val uri = when (intent.action) {
            Intent.ACTION_VIEW -> intent.data
            Intent.ACTION_SEND -> IntentCompat.getParcelableExtra(intent, Intent.EXTRA_STREAM, Uri::class.java)
            else -> null
        } ?: intent.clipData?.getItemAt(0)?.uri ?: return null

        val scheme = uri.scheme?.lowercase()
        if (scheme !in setOf("content", "file", "http", "https")) return null
        retainReadPermission(context, intent, uri)
        val mimeType = intent.type ?: context.contentResolver.getType(uri)
        val kind = when {
            mimeType?.startsWith("video/") == true -> MediaKind.VIDEO
            mimeType?.startsWith("audio/") == true -> MediaKind.AUDIO
            uri.toString().lowercase().let { it.endsWith(".mp4") || it.endsWith(".mkv") || it.endsWith(".webm") || it.endsWith(".m3u8") } -> MediaKind.VIDEO
            else -> MediaKind.AUTO
        }
        return BugeMedia(
            uri = uri.toString(),
            title = displayNameFor(context, uri) ?: uri.lastPathSegment?.substringAfterLast('/')?.ifBlank { null } ?: context.getString(R.string.external_media),
            artist = context.getString(R.string.opened_from_another_app),
            kind = kind
        )
    }

    private fun retainReadPermission(context: Context, intent: Intent, uri: Uri) {
        if (uri.scheme != "content") return
        val readFlags = intent.flags and Intent.FLAG_GRANT_READ_URI_PERMISSION
        if (readFlags != 0 && intent.flags and Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION != 0) {
            runCatching { context.contentResolver.takePersistableUriPermission(uri, readFlags) }
        }
    }

    private fun displayNameFor(context: Context, uri: Uri): String? = runCatching {
        context.contentResolver.query(uri, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null)?.use { cursor ->
            val column = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (column >= 0 && cursor.moveToFirst()) cursor.getString(column) else null
        }
    }.getOrNull()
}
