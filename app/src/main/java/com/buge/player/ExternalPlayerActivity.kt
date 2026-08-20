package com.buge.player

import android.app.Activity
import android.app.ActivityManager
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Forward10
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay10
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color as ComposeColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.Player
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.buge.player.data.BugeMedia
import com.buge.player.data.MediaKind
import com.buge.player.data.PlayerSnapshot
import com.buge.player.media.ExternalMediaIntent
import com.buge.player.ui.BugeViewModel
import com.buge.player.ui.theme.BugeTheme
import kotlin.math.max

/**
 * Transparent, task-isolated entry point for Android ACTION_VIEW media intents.
 * It intentionally does not route through MainActivity, so Files and other source apps
 * remain visually beneath this focused player window.
 */
class ExternalPlayerActivity : ComponentActivity() {
    private val viewModel: BugeViewModel by viewModels()
    private var externalMedia by mutableStateOf<BugeMedia?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window.setDimAmount(0f)
        enableEdgeToEdge()
        if (!handleExternalIntent(intent)) {
            finish()
            return
        }
        setContent {
            val state by viewModel.uiState.collectAsStateWithLifecycle()
            val player by viewModel.player.collectAsStateWithLifecycle()
            LaunchedEffect(state.settings.keepScreenOn, state.player.isPlaying) {
                if (state.settings.keepScreenOn && state.player.isPlaying) {
                    window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                } else {
                    window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                }
            }
            BugeTheme(settings = state.settings, artworkSeed = state.artworkSeed) {
                externalMedia?.let { media ->
                    ExternalPlayerWindow(
                        media = media,
                        snapshot = state.player,
                        player = player,
                        onDismiss = ::stopPlaybackAndExit,
                        onTogglePlay = viewModel::togglePlay,
                        onSeek = viewModel::seekTo,
                        onSeekBy = viewModel::seekBy
                    )
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        if (!handleExternalIntent(intent)) stopPlaybackAndExit()
    }

    private fun handleExternalIntent(intent: Intent): Boolean {
        val media = ExternalMediaIntent.toBugeMedia(this, intent) ?: return false
        externalMedia = media
        viewModel.play(media)
        return true
    }

    private fun stopPlaybackAndExit() {
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        viewModel.stopPlaybackAndExit()
        getSystemService(ActivityManager::class.java)?.appTasks?.forEach { it.finishAndRemoveTask() }
        finishAndRemoveTask()
    }
}

@Composable
private fun ExternalPlayerWindow(
    media: BugeMedia,
    snapshot: PlayerSnapshot,
    player: Player?,
    onDismiss: () -> Unit,
    onTogglePlay: () -> Unit,
    onSeek: (Long) -> Unit,
    onSeekBy: (Long) -> Unit
) {
    val activity = LocalContext.current as? Activity
    val isVideo = media.kind == MediaKind.VIDEO || media.uri.lowercase().let {
        it.endsWith(".mp4") || it.endsWith(".mkv") || it.endsWith(".webm") || it.endsWith(".m3u8")
    }
    val activeSnapshot = if (snapshot.current?.uri == media.uri) snapshot else PlayerSnapshot(current = media)
    var fullscreen by rememberSaveable(media.id) { mutableStateOf(false) }

    BackHandler {
        if (fullscreen) fullscreen = false else onDismiss()
    }

    if (fullscreen && isVideo && player != null) {
        ExternalFullscreenPlayer(
            player = player,
            snapshot = activeSnapshot,
            title = media.title,
            onTogglePlay = onTogglePlay,
            onSeek = onSeek,
            onExit = { fullscreen = false }
        )
        return
    }

    Box(
        modifier = Modifier.fillMaxSize().background(ComposeColor.Transparent).clickable(onClick = onDismiss),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(.94f).widthIn(max = 720.dp).clickable(onClick = {}),
            shape = MaterialTheme.shapes.extraLarge,
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text(stringResource(R.string.opened_media), style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
                        Text(media.title, style = MaterialTheme.typography.titleLarge, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }
                    IconButton(onClick = onDismiss) { Icon(Icons.Filled.Close, stringResource(R.string.close_player)) }
                }
                Spacer(Modifier.height(16.dp))
                if (isVideo && player != null) {
                    ExternalVideoSurface(player, Modifier.fillMaxWidth().aspectRatio(16f / 9f))
                } else if (isVideo) {
                    Surface(
                        modifier = Modifier.fillMaxWidth().aspectRatio(16f / 9f),
                        shape = MaterialTheme.shapes.large,
                        color = MaterialTheme.colorScheme.inverseSurface
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Filled.VideoLibrary, null, modifier = Modifier.size(58.dp), tint = MaterialTheme.colorScheme.inverseOnSurface)
                        }
                    }
                } else {
                    Surface(
                        modifier = Modifier.size(176.dp),
                        shape = MaterialTheme.shapes.extraLarge,
                        color = MaterialTheme.colorScheme.primaryContainer
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Filled.MusicNote, null, modifier = Modifier.size(72.dp), tint = MaterialTheme.colorScheme.onPrimaryContainer)
                        }
                    }
                }
                Spacer(Modifier.height(14.dp))
                Text(media.artist, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Spacer(Modifier.height(8.dp))
                ExternalProgress(activeSnapshot, onSeek)
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(externalFormatTime(activeSnapshot.positionMs), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(if (activeSnapshot.durationMs > 0) externalFormatTime(activeSnapshot.durationMs) else stringResource(R.string.live), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Spacer(Modifier.height(4.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { onSeekBy(-10_000) }, modifier = Modifier.size(52.dp)) { Icon(Icons.Filled.Replay10, stringResource(R.string.back_10_seconds)) }
                    FilledIconButton(onClick = onTogglePlay, modifier = Modifier.size(68.dp)) {
                        Icon(if (activeSnapshot.isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow, if (activeSnapshot.isPlaying) stringResource(R.string.pause) else stringResource(R.string.play), modifier = Modifier.size(36.dp))
                    }
                    IconButton(onClick = { onSeekBy(10_000) }, modifier = Modifier.size(52.dp)) { Icon(Icons.Filled.Forward10, stringResource(R.string.forward_10_seconds)) }
                }
                if (isVideo && player != null) {
                    Spacer(Modifier.height(8.dp))
                    FilledTonalButton(onClick = { fullscreen = true }) {
                        Icon(Icons.Filled.Fullscreen, null)
                        Spacer(Modifier.width(8.dp))
                        Text(stringResource(R.string.fullscreen))
                    }
                }
            }
        }
    }
}

@Composable
private fun ExternalVideoSurface(player: Player, modifier: Modifier) {
    Surface(modifier = modifier, shape = MaterialTheme.shapes.large, color = ComposeColor.Black) {
        AndroidView(
            factory = { context ->
                PlayerView(context).apply {
                    useController = false
                    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
                    this.player = player
                }
            },
            update = { it.player = player }
        )
    }
}

@Composable
private fun ExternalProgress(snapshot: PlayerSnapshot, onSeek: (Long) -> Unit) {
    val duration = max(snapshot.durationMs, 1L)
    Slider(
        value = snapshot.positionMs.coerceIn(0, duration).toFloat(),
        onValueChange = { onSeek(it.toLong()) },
        valueRange = 0f..duration.toFloat()
    )
}

@Composable
private fun ExternalFullscreenPlayer(
    player: Player,
    snapshot: PlayerSnapshot,
    title: String,
    onTogglePlay: () -> Unit,
    onSeek: (Long) -> Unit,
    onExit: () -> Unit
) {
    val activity = LocalContext.current as? Activity
    var controlsVisible by rememberSaveable { mutableStateOf(true) }
    var controlsTick by rememberSaveable { mutableStateOf(0) }
    val reveal = {
        controlsVisible = true
        controlsTick += 1
    }

    DisposableEffect(activity) {
        if (activity != null) {
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            WindowCompat.getInsetsController(activity.window, activity.window.decorView).hide(WindowInsetsCompat.Type.systemBars())
        }
        onDispose {
            if (activity != null) {
                activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                WindowCompat.getInsetsController(activity.window, activity.window.decorView).show(WindowInsetsCompat.Type.systemBars())
            }
        }
    }
    LaunchedEffect(controlsTick) {
        if (controlsVisible) {
            kotlinx.coroutines.delay(3_000)
            controlsVisible = false
        }
    }

    Box(
        Modifier.fillMaxSize().background(ComposeColor.Black).clickable {
            if (controlsVisible) controlsVisible = false else reveal()
        }
    ) {
        ExternalVideoSurface(player, Modifier.fillMaxSize())
        AnimatedVisibility(visible = controlsVisible, enter = fadeIn(), exit = fadeOut()) {
            Box(Modifier.fillMaxSize()) {
                Surface(
                    modifier = Modifier.align(Alignment.TopCenter).padding(24.dp).fillMaxWidth(),
                    color = ComposeColor.Black.copy(alpha = .62f),
                    shape = MaterialTheme.shapes.large
                ) {
                    Row(Modifier.padding(horizontal = 16.dp, vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text(title, modifier = Modifier.weight(1f), color = ComposeColor.White, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        IconButton(onClick = onExit) { Icon(Icons.Filled.Close, stringResource(R.string.exit_fullscreen), tint = ComposeColor.White) }
                    }
                }
                FilledIconButton(onClick = { onTogglePlay(); reveal() }, modifier = Modifier.align(Alignment.Center).size(76.dp)) {
                    Icon(if (snapshot.isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow, null, modifier = Modifier.size(40.dp))
                }
                Surface(
                    modifier = Modifier.align(Alignment.BottomCenter).padding(start = 42.dp, end = 42.dp, bottom = 42.dp).fillMaxWidth(),
                    color = ComposeColor.Black.copy(alpha = .68f),
                    shape = MaterialTheme.shapes.large
                ) {
                    Column(Modifier.padding(horizontal = 16.dp, vertical = 10.dp)) {
                        ExternalProgress(snapshot) { onSeek(it); reveal() }
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(externalFormatTime(snapshot.positionMs), color = ComposeColor.White, style = MaterialTheme.typography.labelMedium)
                            Text(if (snapshot.durationMs > 0) externalFormatTime(snapshot.durationMs) else stringResource(R.string.live), color = ComposeColor.White, style = MaterialTheme.typography.labelMedium)
                        }
                    }
                }
            }
        }
    }
}

private fun externalFormatTime(ms: Long): String {
    if (ms <= 0) return "0:00"
    val total = ms / 1_000
    val hours = total / 3_600
    val minutes = (total % 3_600) / 60
    val seconds = total % 60
    return if (hours > 0) "%d:%02d:%02d".format(hours, minutes, seconds) else "%d:%02d".format(minutes, seconds)
}
