package com.buge.player.media

import android.content.ComponentName
import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.PlaybackException
import androidx.media3.common.PlaybackParameters
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.buge.player.data.BugeMedia
import com.buge.player.data.PlayerSnapshot
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class PlaybackConnection(context: Context) {
    private val appContext = context.applicationContext
    private val scope = CoroutineScope(Dispatchers.Main.immediate)
    private val catalog = mutableMapOf<String, BugeMedia>()
    private var controllerFuture: ListenableFuture<MediaController>? = null
    private var controller: MediaController? = null
    private var progressJob: Job? = null
    private var pendingAction: ((MediaController) -> Unit)? = null
    private var autoplayEnabled: Boolean = true

    private val _snapshot = MutableStateFlow(PlayerSnapshot())
    val snapshot: StateFlow<PlayerSnapshot> = _snapshot.asStateFlow()
    private val _player = MutableStateFlow<Player?>(null)
    val player: StateFlow<Player?> = _player.asStateFlow()

    private val listener = object : Player.Listener {
        override fun onEvents(player: Player, events: Player.Events) = refresh()
        override fun onPlayerError(error: PlaybackException) = refresh(error.message ?: "Playback error")
    }

    fun connect() {
        if (controller != null || controllerFuture != null) return
        val token = SessionToken(appContext, ComponentName(appContext, PlaybackService::class.java))
        controllerFuture = MediaController.Builder(appContext, token).buildAsync().also { future ->
            future.addListener({
                runCatching { future.get() }.onSuccess { connected ->
                    controller = connected
                    _player.value = connected
                    connected.addListener(listener)
                    pendingAction?.also { action -> pendingAction = null; action(connected) }
                    refresh()
                    startProgressTicker()
                }.onFailure { refresh("Unable to connect to playback service") }
            }, androidx.core.content.ContextCompat.getMainExecutor(appContext))
        }
    }

    fun play(media: BugeMedia, queue: List<BugeMedia> = emptyList()) {
        catalog[media.id] = media
        queue.forEach { catalog[it.id] = it }
        withPlayer { player ->
            val source = if (queue.isEmpty()) listOf(media) else queue
            val index = source.indexOfFirst { it.id == media.id }.coerceAtLeast(0)
            player.setMediaItems(source.map(BugeMedia::toMediaItem), index, 0)
            player.prepare()
            if (autoplayEnabled) player.play()
            refresh()
        }
    }

    fun append(media: BugeMedia) = withPlayer { player ->
        catalog[media.id] = media
        player.addMediaItem(media.toMediaItem())
        refresh()
    }

    fun togglePlay() = withPlayer { if (it.isPlaying) it.pause() else it.play() }
    fun seekTo(positionMs: Long) = withPlayer { it.seekTo(positionMs.coerceAtLeast(0)) }
    fun seekBy(deltaMs: Long) = withPlayer { it.seekTo((it.currentPosition + deltaMs).coerceAtLeast(0)) }
    fun next() = withPlayer { it.seekToNextMediaItem() }
    fun previous() = withPlayer { it.seekToPreviousMediaItem() }
    fun setSpeed(speed: Float) = withPlayer { it.playbackParameters = PlaybackParameters(speed) }
    fun setAutoplay(enabled: Boolean) { autoplayEnabled = enabled }
    fun toggleShuffle() = withPlayer { it.shuffleModeEnabled = !it.shuffleModeEnabled }
    fun cycleRepeat() = withPlayer {
        it.repeatMode = when (it.repeatMode) {
            Player.REPEAT_MODE_OFF -> Player.REPEAT_MODE_ALL
            Player.REPEAT_MODE_ALL -> Player.REPEAT_MODE_ONE
            else -> Player.REPEAT_MODE_OFF
        }
    }
    fun clear() = withPlayer { it.clearMediaItems(); refresh() }

    fun stopAndClear() {
        pendingAction = null
        controller?.let { player ->
            player.stop()
            player.clearMediaItems()
            refresh()
        } ?: run {
            controllerFuture?.cancel(true)
            controllerFuture = null
            _snapshot.value = PlayerSnapshot()
        }
    }

    fun release() {
        progressJob?.cancel()
        progressJob = null
        pendingAction = null
        controllerFuture?.cancel(true)
        controller?.removeListener(listener)
        controller?.release()
        controller = null
        controllerFuture = null
        _player.value = null
        _snapshot.value = PlayerSnapshot()
    }

    private fun withPlayer(action: (MediaController) -> Unit) {
        controller?.let(action) ?: run {
            pendingAction = action
            connect()
        }
    }

    private fun startProgressTicker() {
        progressJob?.cancel()
        progressJob = scope.launch {
            while (isActive) {
                refresh()
                delay(250)
            }
        }
    }

    private fun refresh(error: String? = null) {
        val p = controller ?: return
        val current = catalog[p.currentMediaItem?.mediaId] ?: p.currentMediaItem?.let {
            BugeMedia(id = it.mediaId, uri = it.localConfiguration?.uri?.toString().orEmpty(),
                title = it.mediaMetadata.title?.toString() ?: "Unknown media",
                artist = it.mediaMetadata.artist?.toString() ?: "Buge Player",
                artworkUri = it.mediaMetadata.artworkUri?.toString())
        }
        _snapshot.value = PlayerSnapshot(
            current = current,
            isPlaying = p.isPlaying,
            isBuffering = p.playbackState == Player.STATE_BUFFERING,
            positionMs = p.currentPosition.coerceAtLeast(0),
            durationMs = p.duration.takeIf { it > 0 } ?: current?.durationMs ?: 0,
            bufferedMs = p.bufferedPosition.coerceAtLeast(0),
            speed = p.playbackParameters.speed,
            repeatMode = p.repeatMode,
            shuffleEnabled = p.shuffleModeEnabled,
            error = error
        )
    }
}
