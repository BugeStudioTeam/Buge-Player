package com.buge.player.media

import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import com.buge.player.data.BugeMedia
import com.buge.player.data.MediaKind
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object MediaScanner {
    suspend fun scan(context: Context): List<BugeMedia> = withContext(Dispatchers.IO) {
        val projection = arrayOf(
            MediaStore.MediaColumns._ID,
            MediaStore.MediaColumns.DISPLAY_NAME,
            MediaStore.MediaColumns.DURATION,
            MediaStore.MediaColumns.DATE_ADDED,
            MediaStore.MediaColumns.MIME_TYPE,
            MediaStore.Audio.AudioColumns.ARTIST
        )
        val audio = query(context, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, MediaKind.AUDIO)
        val video = query(context, MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection, MediaKind.VIDEO)
        (audio + video).sortedByDescending(BugeMedia::addedAt)
    }

    private fun query(context: Context, collection: android.net.Uri, projection: Array<String>, kind: MediaKind): List<BugeMedia> {
        return buildList {
            context.contentResolver.query(
                collection, projection, "${MediaStore.MediaColumns.SIZE} > 0", null,
                "${MediaStore.MediaColumns.DATE_ADDED} DESC"
            )?.use { cursor ->
                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID)
                val titleColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME)
                val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DURATION)
                val dateColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_ADDED)
                val artistColumn = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ARTIST)
                while (cursor.moveToNext()) {
                    val uri = ContentUris.withAppendedId(collection, cursor.getLong(idColumn))
                    add(BugeMedia(
                        id = "$kind-${cursor.getLong(idColumn)}",
                        uri = uri.toString(),
                        title = cursor.getString(titleColumn).orEmpty().ifBlank { "Untitled" },
                        artist = (artistColumn.takeIf { it >= 0 }?.let { cursor.getString(it) } ?: "").ifBlank { if (kind == MediaKind.VIDEO) "Video" else "Unknown artist" },
                        kind = kind,
                        durationMs = cursor.getLong(durationColumn),
                        addedAt = cursor.getLong(dateColumn) * 1000L
                    ))
                }
            }
        }
    }
}
