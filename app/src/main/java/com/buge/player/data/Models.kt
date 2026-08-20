package com.buge.player.data

import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.MimeTypes
import java.util.UUID

enum class MediaKind { AUDIO, VIDEO, AUTO }
enum class AppTheme { SYSTEM, LIGHT, DARK }
enum class AccentMode { SYSTEM, ARTWORK, VIOLET, OCEAN, SUNSET, FOREST }
enum class AppLanguage(val localeTag: String, val displayName: String) {
    ENGLISH("en", "English - en"),
    FRENCH("fr", "Français - fr"),
    GERMAN("de", "Deutsch - de"),
    RUSSIAN("ru", "Русский - ru"),
    PORTUGUESE("pt", "Português - pt"),
    PORTUGUESE_BRAZIL("pt-BR", "Português (Brasil) - pt-rBR"),
    SPANISH("es", "Español - es"),
    CHINESE("zh", "中文 (简体) - zh"),
    CHINESE_TRADITIONAL("zh-TW", "中文 (繁体) - zh-rTW"),
    ARABIC("ar", "العربية - ar"),
    JAPANESE("ja", "日本語 - ja"),
    KOREAN("ko", "한국어 - ko")
}

data class BugeMedia(
    val id: String = UUID.randomUUID().toString(),
    val uri: String,
    val title: String,
    val artist: String = "Buge Player",
    val artworkUri: String? = null,
    val kind: MediaKind = MediaKind.AUTO,
    val durationMs: Long = 0L,
    val addedAt: Long = System.currentTimeMillis()
) {
    fun toMediaItem(): MediaItem {
        val builder = MediaItem.Builder()
            .setMediaId(id)
            .setUri(Uri.parse(uri))
            .setMediaMetadata(
                MediaMetadata.Builder()
                    .setTitle(title)
                    .setArtist(artist)
                    .setArtworkUri(artworkUri?.let(Uri::parse))
                    .setIsPlayable(true)
                    .build()
            )
        if (kind == MediaKind.AUDIO) builder.setMimeType(MimeTypes.AUDIO_UNKNOWN)
        if (kind == MediaKind.VIDEO) builder.setMimeType(MimeTypes.VIDEO_UNKNOWN)
        if (uri.lowercase().contains(".m3u8")) builder.setMimeType(MimeTypes.APPLICATION_M3U8)
        return builder.build()
    }

    fun serialize(): String = listOf(id, uri, title, artist, artworkUri.orEmpty(), kind.name, durationMs, addedAt)
        .joinToString("|") { java.util.Base64.getUrlEncoder().withoutPadding().encodeToString(it.toString().toByteArray()) }

    companion object {
        fun deserialize(value: String): BugeMedia? = runCatching {
            val parts = value.split("|").map { String(java.util.Base64.getUrlDecoder().decode(it)) }
            BugeMedia(
                id = parts[0], uri = parts[1], title = parts[2], artist = parts[3],
                artworkUri = parts[4].ifBlank { null }, kind = MediaKind.valueOf(parts[5]),
                durationMs = parts[6].toLong(), addedAt = parts[7].toLong()
            )
        }.getOrNull()
    }
}

data class PlayerSnapshot(
    val current: BugeMedia? = null,
    val isPlaying: Boolean = false,
    val isBuffering: Boolean = false,
    val positionMs: Long = 0L,
    val durationMs: Long = 0L,
    val bufferedMs: Long = 0L,
    val speed: Float = 1f,
    val repeatMode: Int = 0,
    val shuffleEnabled: Boolean = false,
    val error: String? = null
)

data class UserSettings(
    val theme: AppTheme = AppTheme.SYSTEM,
    val accent: AccentMode = AccentMode.SYSTEM,
    val language: AppLanguage = AppLanguage.ENGLISH,
    val keepScreenOn: Boolean = false,
    val autoplay: Boolean = true
)

val DemoStream = BugeMedia(
    id = "demo-bbb-hls",
    uri = "https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8",
    title = "Big Buck Bunny • HLS Demo",
    artist = "Network stream",
    kind = MediaKind.VIDEO
)
