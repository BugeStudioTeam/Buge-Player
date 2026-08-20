package com.buge.player.ui

import android.app.Application
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.LocaleList
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.buge.player.data.AppLanguage
import com.buge.player.data.BugeMedia
import com.buge.player.data.PlayerRepository
import com.buge.player.data.PlayerSnapshot
import com.buge.player.data.UserSettings
import com.buge.player.media.MediaScanner
import com.buge.player.media.PlaybackService
import com.buge.player.media.PlaybackConnection
import com.buge.player.util.ArtworkPalette
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class BugeUiState(
    val player: PlayerSnapshot = PlayerSnapshot(),
    val settings: UserSettings = UserSettings(),
    val favorites: List<BugeMedia> = emptyList(),
    val recent: List<BugeMedia> = emptyList(),
    val library: List<BugeMedia> = emptyList(),
    val isScanning: Boolean = false,
    val artworkSeed: Int? = null,
    val videoFrame: Bitmap? = null,
    val videoFrameFor: String? = null,
    val queue: List<BugeMedia> = emptyList(),
    val onboardingCompleted: Boolean? = null,
    val artworkSeedFor: String? = null,
    val externalOpenMedia: BugeMedia? = null
)

class BugeViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = PlayerRepository(application)
    private val connection = PlaybackConnection(application)
    private val _uiState = MutableStateFlow(BugeUiState())
    val uiState: StateFlow<BugeUiState> = _uiState.asStateFlow()
    val player = connection.player
    private var lastRememberedUri: String? = null

    init {
        connection.connect()
        viewModelScope.launch { repository.settings.collect { settings ->
            connection.setAutoplay(settings.autoplay)
            update { copy(settings = settings) }
        } }
        viewModelScope.launch { repository.favorites.collect { update { copy(favorites = it) } } }
        viewModelScope.launch { repository.recent.collect { update { copy(recent = it) } } }
        viewModelScope.launch { repository.queue.collect { update { copy(queue = it) } } }
        viewModelScope.launch { repository.onboardingCompleted.collect { update { copy(onboardingCompleted = it) } } }
        viewModelScope.launch {
            connection.snapshot.collectLatest { snapshot ->
                update { copy(player = snapshot) }
                snapshot.current?.let { media ->
                    if (snapshot.isPlaying && lastRememberedUri != media.uri) {
                        lastRememberedUri = media.uri
                        repository.remember(media)
                    }
                    if (uiState.value.artworkSeedFor != media.uri) {
                        update { copy(artworkSeed = null, artworkSeedFor = media.uri) }
                        resolveArtworkSeed(media)
                    }
                    resolveVideoFrame(media)
                } ?: run { lastRememberedUri = null }
            }
        }
    }

    fun play(media: BugeMedia, queue: List<BugeMedia> = emptyList()) {
        connection.play(media, queue)
        viewModelScope.launch { repository.saveQueue(if (queue.isEmpty()) listOf(media) else queue) }
    }

    fun openExternalMedia(media: BugeMedia) {
        update { copy(externalOpenMedia = media) }
        play(media)
    }

    fun dismissExternalPlayer() = update { copy(externalOpenMedia = null) }

    /** Stops Media3 and removes the standalone external-player task without leaving audio alive. */
    fun stopPlaybackAndExit() {
        connection.stopAndClear()
        connection.release()
        getApplication<Application>().stopService(Intent(getApplication(), PlaybackService::class.java))
        update {
            copy(
                player = PlayerSnapshot(),
                externalOpenMedia = null,
                artworkSeed = null,
                artworkSeedFor = null,
                videoFrame = null,
                videoFrameFor = null
            )
        }
        viewModelScope.launch { repository.saveQueue(emptyList()) }
    }

    fun addToQueue(media: BugeMedia) {
        connection.append(media)
        viewModelScope.launch { repository.saveQueue(uiState.value.queue + media) }
    }

    fun togglePlay() = connection.togglePlay()
    fun seekTo(position: Long) = connection.seekTo(position)
    fun seekBy(delta: Long) = connection.seekBy(delta)
    fun next() = connection.next()
    fun previous() = connection.previous()
    fun setSpeed(speed: Float) = connection.setSpeed(speed)
    fun toggleShuffle() = connection.toggleShuffle()
    fun cycleRepeat() = connection.cycleRepeat()
    fun clearQueue() { connection.clear(); viewModelScope.launch { repository.saveQueue(emptyList()) } }
    fun toggleFavorite(media: BugeMedia) = viewModelScope.launch { repository.toggleFavorite(media) }

    fun scanLibrary() = viewModelScope.launch {
        update { copy(isScanning = true) }
        val files = MediaScanner.scan(getApplication())
        update { copy(library = files, isScanning = false) }
    }

    fun completeOnboarding() = viewModelScope.launch { repository.completeOnboarding() }

    fun saveSettings(settings: UserSettings) = viewModelScope.launch {
        repository.saveSettings(settings)
        applyLocale(settings.language)
    }

    private fun resolveVideoFrame(media: BugeMedia) {
        val isVideo = isVideoMedia(media)
        if (!isVideo) {
            if (uiState.value.videoFrameFor != null) update { copy(videoFrame = null, videoFrameFor = null) }
            return
        }
        if (uiState.value.videoFrameFor == media.id) return
        update { copy(videoFrame = null, videoFrameFor = media.id) }
        viewModelScope.launch(Dispatchers.IO) {
            val frame = ArtworkPalette.firstVideoFrame(getApplication(), media.uri)
            withContext(Dispatchers.Main) {
                if (uiState.value.player.current?.id == media.id) update { copy(videoFrame = frame, videoFrameFor = media.id) }
            }
        }
    }

    private fun resolveArtworkSeed(media: BugeMedia) = viewModelScope.launch(Dispatchers.IO) {
        val seed = media.artworkUri?.let { ArtworkPalette.seedFromArtworkUrl(it) }
            ?: ArtworkPalette.seedFromMedia(getApplication(), media.uri, isVideoMedia(media))
        withContext(Dispatchers.Main) {
            if (uiState.value.artworkSeedFor == media.uri) update { copy(artworkSeed = seed) }
        }
    }

    private fun isVideoMedia(media: BugeMedia): Boolean =
        media.kind == com.buge.player.data.MediaKind.VIDEO || media.uri.lowercase().let {
            it.contains(".mp4") || it.contains(".mkv") || it.contains(".webm") || it.contains(".m3u8")
        }

    private fun applyLocale(language: AppLanguage) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            getApplication<Application>().getSystemService(android.app.LocaleManager::class.java)
                .applicationLocales = LocaleList.forLanguageTags(language.localeTag)
        }
    }

    private fun update(transform: BugeUiState.() -> BugeUiState) { _uiState.value = _uiState.value.transform() }

    override fun onCleared() {
        connection.release()
        super.onCleared()
    }
}
