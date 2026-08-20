package com.buge.player.util

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.util.LruCache
import androidx.palette.graphics.Palette
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Collections

object ArtworkPalette {
    private val seedCache = Collections.synchronizedMap(mutableMapOf<String, Int?>())
    private val frameCache = object : LruCache<String, Bitmap>(12) {}

    suspend fun seedFromMedia(context: Context, uri: String, isVideo: Boolean = false): Int? = withContext(Dispatchers.IO) {
        val cacheKey = "seed:$uri:$isVideo"
        synchronized(seedCache) { if (seedCache.containsKey(cacheKey)) return@withContext seedCache[cacheKey] }
        val seed = runCatching {
            val retriever = MediaMetadataRetriever()
            try {
                retriever.setDataSource(context, android.net.Uri.parse(uri))
                val bitmap = if (isVideo) {
                    retriever.getFrameAtTime(1_000_000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
                        ?: retriever.getFrameAtTime(0, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
                } else {
                    val bytes = retriever.embeddedPicture
                    bytes?.let { android.graphics.BitmapFactory.decodeByteArray(it, 0, it.size) }
                        ?: retriever.getFrameAtTime(1_000_000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
                }
                bitmap?.let(::dominantColor)
            } finally {
                retriever.release()
            }
        }.getOrNull()
        synchronized(seedCache) { seedCache[cacheKey] = seed }
        seed
    }

    suspend fun firstVideoFrame(context: Context, uri: String): Bitmap? = withContext(Dispatchers.IO) {
        synchronized(frameCache) { frameCache.get(uri)?.let { return@withContext it } }
        val frame = runCatching {
            val retriever = MediaMetadataRetriever()
            try {
                retriever.setDataSource(context, android.net.Uri.parse(uri))
                retriever.getFrameAtTime(0, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
                    ?: retriever.getFrameAtTime(1_000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
            } finally {
                retriever.release()
            }
        }.getOrNull()
        if (frame != null) synchronized(frameCache) { frameCache.put(uri, frame) }
        frame
    }

    suspend fun seedFromArtworkUrl(url: String): Int? = withContext(Dispatchers.IO) {
        val cacheKey = "url:$url"
        synchronized(seedCache) { if (seedCache.containsKey(cacheKey)) return@withContext seedCache[cacheKey] }
        val seed = runCatching {
            val connection = java.net.URL(url).openConnection().apply {
                connectTimeout = 6_000
                readTimeout = 8_000
                setRequestProperty("User-Agent", "BugePlayer/1.0")
            }
            connection.getInputStream().use { input ->
                android.graphics.BitmapFactory.decodeStream(input)?.let(::dominantColor)
            }
        }.getOrNull()
        synchronized(seedCache) { seedCache[cacheKey] = seed }
        seed
    }

    fun dominantColor(bitmap: Bitmap): Int? {
        val small = Bitmap.createScaledBitmap(bitmap, 96, 96, true)
        return Palette.from(small).maximumColorCount(16).generate().getDominantColor(0).takeIf { it != 0 }
    }
}
