package com.buge.player

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.buge.player.media.ExternalMediaIntent
import com.buge.player.ui.BugeApp
import com.buge.player.ui.BugeViewModel
import com.buge.player.ui.theme.BugeTheme

class MainActivity : ComponentActivity() {
    private val viewModel: BugeViewModel by viewModels()
    private val mediaPermission = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { grants ->
        if (grants.values.any { it }) viewModel.scanLibrary()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        consumeExternalMediaIntent(intent)
        setContent {
            val state by viewModel.uiState.collectAsStateWithLifecycle()
            LaunchedEffect(state.settings.keepScreenOn, state.player.isPlaying) {
                if (state.settings.keepScreenOn && state.player.isPlaying) {
                    window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                } else {
                    window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                }
            }
            BugeTheme(settings = state.settings, artworkSeed = state.artworkSeed) {
                BugeApp(
                    state = state,
                    player = viewModel.player.collectAsStateWithLifecycle().value,
                    onPlay = viewModel::play,
                    onTogglePlay = viewModel::togglePlay,
                    onSeek = viewModel::seekTo,
                    onSeekBy = viewModel::seekBy,
                    onNext = viewModel::next,
                    onPrevious = viewModel::previous,
                    onSpeed = viewModel::setSpeed,
                    onShuffle = viewModel::toggleShuffle,
                    onRepeat = viewModel::cycleRepeat,
                    onFavorite = viewModel::toggleFavorite,
                    onAddQueue = viewModel::addToQueue,
                    onClearQueue = viewModel::clearQueue,
                    onSaveSettings = viewModel::saveSettings,
                    onCompleteOnboarding = viewModel::completeOnboarding,
                    onScanLibrary = ::requestMediaPermission,
                    onDismissExternalPlayer = viewModel::dismissExternalPlayer
                )
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        consumeExternalMediaIntent(intent)
    }

    private fun consumeExternalMediaIntent(intent: Intent?) {
        ExternalMediaIntent.toBugeMedia(this, intent)
            ?.takeIf { it.uri.startsWith("http://") || it.uri.startsWith("https://") }
            ?.let(viewModel::openExternalMedia)
    }

    private fun requestMediaPermission() {
        val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(Manifest.permission.READ_MEDIA_AUDIO, Manifest.permission.READ_MEDIA_VIDEO)
        } else arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        if (permissions.all { ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED }) {
            viewModel.scanLibrary()
        } else {
            mediaPermission.launch(permissions)
        }
    }
}
