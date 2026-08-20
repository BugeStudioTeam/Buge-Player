package com.buge.player.ui

import android.app.Activity
import android.graphics.Bitmap
import android.content.pm.ActivityInfo
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddLink
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Forward10
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.QueueMusic
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.RepeatOne
import androidx.compose.material.icons.filled.Replay10
import androidx.compose.material.icons.filled.ScreenRotation
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.media3.common.Player
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.buge.player.BuildConfig
import com.buge.player.data.AccentMode
import com.buge.player.data.AppLanguage
import com.buge.player.data.AppTheme
import com.buge.player.data.BugeMedia
import com.buge.player.data.MediaKind
import com.buge.player.data.PlayerSnapshot
import com.buge.player.data.UserSettings
import com.buge.player.util.ArtworkPalette
import com.buge.player.util.MediaFileSharer
import kotlin.math.max

enum class Destination { HOME, LIBRARY, SETTINGS }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BugeApp(
    state: BugeUiState,
    player: Player?,
    onPlay: (BugeMedia, List<BugeMedia>) -> Unit,
    onTogglePlay: () -> Unit,
    onSeek: (Long) -> Unit,
    onSeekBy: (Long) -> Unit,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    onSpeed: (Float) -> Unit,
    onShuffle: () -> Unit,
    onRepeat: () -> Unit,
    onFavorite: (BugeMedia) -> Unit,
    onAddQueue: (BugeMedia) -> Unit,
    onClearQueue: () -> Unit,
    onSaveSettings: (UserSettings) -> Unit,
    onCompleteOnboarding: () -> Unit,
    onScanLibrary: () -> Unit,
    onDismissExternalPlayer: () -> Unit
) {
    val text = remember(state.settings.language) { AppText(state.settings.language) }
    var destination by rememberSaveable { mutableStateOf(Destination.HOME) }
    var showPlayer by rememberSaveable { mutableStateOf(false) }
    var showNetworkDialog by rememberSaveable { mutableStateOf(false) }
    var showQueue by rememberSaveable { mutableStateOf(false) }
    val snackbar = remember { SnackbarHostState() }
    var welcomeExiting by rememberSaveable { mutableStateOf(false) }
    val playAndShow: (BugeMedia, List<BugeMedia>) -> Unit = { media, queue ->
        onPlay(media, queue)
        showPlayer = true
    }

    if (state.onboardingCompleted == false && state.externalOpenMedia == null) {
        LaunchedEffect(welcomeExiting) {
            if (welcomeExiting) {
                delay(520)
                onCompleteOnboarding()
            }
        }
        Box(Modifier.fillMaxSize()) {
            AnimatedVisibility(
                visible = !welcomeExiting,
                exit = fadeOut(animationSpec = tween(320)) + scaleOut(targetScale = .90f, animationSpec = tween(520)),
                label = "welcome-exit"
            ) {
                WelcomeScreen(text = text, onBegin = { welcomeExiting = true })
            }
            AnimatedVisibility(visible = welcomeExiting, label = "welcome-afterglow") {
                WelcomeAfterglow()
            }
        }
        return
    }

    BackHandler(enabled = showPlayer && state.externalOpenMedia == null && !showNetworkDialog && !showQueue) { showPlayer = false }
    LaunchedEffect(state.player.error) { state.player.error?.let { snackbar.showSnackbar(it) } }

    val recommendationPool = remember(state.favorites, state.recent, state.library, state.queue) {
        (state.favorites + state.recent + state.library + state.queue).distinctBy { it.uri }
    }

    if (showPlayer && state.player.current != null) {
        NowPlayingScreen(
            snapshot = state.player, player = player, videoFrame = state.videoFrame, favorite = state.favorites.any { it.uri == state.player.current.uri },
            recommendationPool = recommendationPool, onPlayRecommendation = onPlay,
            text = text, onBack = { showPlayer = false }, onPlayPause = onTogglePlay, onSeek = onSeek,
            onSeekBy = onSeekBy, onNext = onNext, onPrevious = onPrevious, onSpeed = onSpeed,
            onShuffle = onShuffle, onRepeat = onRepeat, onFavorite = { onFavorite(state.player.current) },
            onQueue = { showQueue = true }
        )
    } else {
        Scaffold(
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Row(verticalAlignment = Alignment.CenterVertically) { Icon(Icons.Filled.GraphicEq, null); Spacer(Modifier.width(8.dp)); Text("Buge Player", style = MaterialTheme.typography.titleLarge) } },
                    actions = { IconButton(onClick = { showNetworkDialog = true }) { Icon(Icons.Filled.AddLink, text.addStream) } },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent)
                )
            },
            bottomBar = {
                Column {
                    AnimatedVisibility(
                        visible = state.player.current != null,
                        enter = slideInVertically(animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)) { it / 2 } + fadeIn(),
                        exit = slideOutVertically(animationSpec = tween(180)) { it / 2 } + fadeOut()
                    ) {
                        MiniPlayer(snapshot = state.player, videoFrame = state.videoFrame, onOpen = { showPlayer = true }, onTogglePlay = onTogglePlay, onClose = onClearQueue)
                    }
                    NavigationBar {
                        listOf(
                            Destination.HOME to Pair(Icons.Filled.Home, text.home),
                            Destination.LIBRARY to Pair(Icons.Filled.LibraryMusic, text.library),
                            Destination.SETTINGS to Pair(Icons.Filled.Settings, text.settings)
                        ).forEach { (item, data) ->
                            NavigationBarItem(selected = destination == item, onClick = { destination = item }, icon = { Icon(data.first, data.second) }, label = { Text(data.second) })
                        }
                    }
                }
            },
            snackbarHost = { SnackbarHost(snackbar) },
            floatingActionButton = {
                if (destination != Destination.SETTINGS) LargeFloatingActionButton(onClick = { showNetworkDialog = true }) {
                    Icon(Icons.Filled.AddLink, text.addStream)
                }
            }
        ) { padding ->
            Box(Modifier.fillMaxSize().padding(padding)) {
                AnimatedContent(
                    targetState = destination,
                    transitionSpec = {
                        (fadeIn(animationSpec = tween(260)) +
                            slideInHorizontally(animationSpec = spring(dampingRatio = Spring.DampingRatioNoBouncy)) { it / 10 } +
                            scaleIn(initialScale = .975f, animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)))
                            .togetherWith(
                                fadeOut(animationSpec = tween(160)) +
                                    slideOutHorizontally(animationSpec = tween(220)) { -it / 14 }
                            )
                            .using(SizeTransform(clip = false))
                    },
                    label = "destination"
                ) { screen ->
                    when (screen) {
                        Destination.HOME -> HomeScreen(state, text, playAndShow, onFavorite, onAddQueue, { showNetworkDialog = true }, { showPlayer = true })
                        Destination.LIBRARY -> LibraryScreen(state, text, playAndShow, onFavorite, onAddQueue, onScanLibrary)
                        Destination.SETTINGS -> SettingsScreen(state.settings, text, onSaveSettings, onClearQueue, state.queue)
                    }
                }
            }
        }
    }

    if (showNetworkDialog) NetworkMediaDialog(text = text, onDismiss = { showNetworkDialog = false }) { media ->
        onPlay(media, state.queue + media)
        showNetworkDialog = false
        showPlayer = true
    }
    if (showQueue) QueueSheet(text, state.queue, state.player.current, { media, queue -> playAndShow(media, queue); showQueue = false }, onClearQueue) { showQueue = false }
    state.externalOpenMedia?.let { externalMedia ->
        ExternalMediaPlayerDialog(
            media = externalMedia,
            snapshot = state.player,
            player = player,
            text = text,
            onDismiss = onDismissExternalPlayer,
            onPlayPause = onTogglePlay,
            onSeek = onSeek,
            onSeekBy = onSeekBy
        )
    }
}

@Composable
private fun WelcomeAfterglow() {
    val primary = MaterialTheme.colorScheme.primary
    val secondary = MaterialTheme.colorScheme.secondary
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.surface) {
        Box(contentAlignment = Alignment.Center) {
            Canvas(Modifier.fillMaxSize().padding(44.dp)) {
                val radius = size.minDimension / 3.2f
                drawCircle(primary.copy(alpha = .18f), radius = radius)
                drawCircle(secondary.copy(alpha = .15f), radius = radius * .56f, center = androidx.compose.ui.geometry.Offset(size.width * .68f, size.height * .36f))
            }
            Icon(Icons.Filled.GraphicEq, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(72.dp))
        }
    }
}

@Composable
private fun WelcomeScreen(text: AppText, onBegin: () -> Unit) {
    val primary = MaterialTheme.colorScheme.primary
    val tertiaryContainer = MaterialTheme.colorScheme.tertiaryContainer
    val secondary = MaterialTheme.colorScheme.secondary
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.surface) {
        Column(modifier = Modifier.fillMaxSize().padding(28.dp), verticalArrangement = Arrangement.SpaceBetween) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(color = MaterialTheme.colorScheme.primaryContainer, shape = MaterialTheme.shapes.large, modifier = Modifier.size(58.dp)) {
                    Box(contentAlignment = Alignment.Center) { Icon(Icons.Filled.GraphicEq, null, tint = MaterialTheme.colorScheme.onPrimaryContainer, modifier = Modifier.size(31.dp)) }
                }
                Spacer(Modifier.width(14.dp))
                Column {
                    Text("Buge Player", style = MaterialTheme.typography.headlineSmall)
                    Text(text.welcomeEyebrow, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary, letterSpacing = 1.2.sp)
                }
            }
            Column {
                Box(Modifier.fillMaxWidth().aspectRatio(1.15f).clip(MaterialTheme.shapes.extraLarge).background(MaterialTheme.colorScheme.primaryContainer)) {
                    Canvas(Modifier.fillMaxSize().padding(30.dp)) {
                        val radius = size.minDimension / 2.7f
                        drawCircle(primary, radius = radius, center = center)
                        drawCircle(tertiaryContainer, radius = radius * .63f, center = androidx.compose.ui.geometry.Offset(size.width * .31f, size.height * .35f))
                        drawCircle(secondary, radius = radius * .32f, center = androidx.compose.ui.geometry.Offset(size.width * .74f, size.height * .68f))
                    }
                    Icon(Icons.Filled.PlayArrow, null, modifier = Modifier.align(Alignment.Center).size(76.dp), tint = MaterialTheme.colorScheme.onPrimary)
                }
                Spacer(Modifier.height(30.dp))
                Text(text.welcomeTitle, style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Bold, lineHeight = 45.sp)
                Spacer(Modifier.height(14.dp))
                Text(text.welcomeBody, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant, lineHeight = 25.sp)
            }
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    AssistChip(onClick = {}, label = { Text(text.welcomeFeatureOne) }, leadingIcon = { Icon(Icons.Filled.AddLink, null, modifier = Modifier.size(18.dp)) })
                    AssistChip(onClick = {}, label = { Text(text.welcomeFeatureTwo) }, leadingIcon = { Icon(Icons.Filled.GraphicEq, null, modifier = Modifier.size(18.dp)) })
                }
                Button(onClick = onBegin, modifier = Modifier.fillMaxWidth().height(56.dp), shape = MaterialTheme.shapes.large) {
                    Icon(Icons.Filled.PlayArrow, null); Spacer(Modifier.width(8.dp)); Text(text.welcomeStart)
                }
            }
        }
    }
}

@Composable
private fun HomeScreen(
    state: BugeUiState, text: AppText,
    onPlay: (BugeMedia, List<BugeMedia>) -> Unit,
    onFavorite: (BugeMedia) -> Unit,
    onAddQueue: (BugeMedia) -> Unit,
    onAddStream: () -> Unit,
    onOpenPlayer: () -> Unit
) {
    LazyColumn(Modifier.fillMaxSize(), contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item { HeroCard(snapshot = state.player, text = text, onPlay = onOpenPlayer, onAddStream = onAddStream) }
        item { SectionHeading(text.favorites, state.favorites.size) }
        if (state.favorites.isEmpty()) item { EmptyCard(text.noFavorites) }
        items(state.favorites, key = { "home-fav-${it.id}" }) { media ->
            MediaRow(media, state.player.current?.uri == media.uri, true, onPlay = { onPlay(media, state.favorites) }, onFavorite = { onFavorite(media) }, onAddQueue = { onAddQueue(media) })
        }
        item { SectionHeading(text.recent, state.recent.size) }
        if (state.recent.isEmpty()) item { EmptyCard(text.noRecent) }
        items(state.recent.take(10), key = { "recent-${it.id}" }) { media ->
            MediaRow(media, state.player.current?.uri == media.uri, state.favorites.any { it.uri == media.uri }, onPlay = { onPlay(media, state.recent) }, onFavorite = { onFavorite(media) }, onAddQueue = { onAddQueue(media) })
        }
        item { Spacer(Modifier.height(92.dp)) }
    }
}

@Composable
private fun RandomPicksGrid(picks: List<BugeMedia>, text: AppText, activeUri: String?, onPlay: (BugeMedia) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        SectionHeading(text.forYou, picks.size)
        Text(text.randomMediaHint, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        picks.chunked(2).forEach { row ->
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                row.forEach { media ->
                    RandomMediaCard(media, media.uri == activeUri, Modifier.weight(1f), onClick = { onPlay(media) })
                }
                if (row.size == 1) Spacer(Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun RandomMediaCard(media: BugeMedia, active: Boolean, modifier: Modifier, onClick: () -> Unit) {
    val context = LocalContext.current
    val isVideo = media.kind == MediaKind.VIDEO || media.uri.lowercase().let { it.contains(".mp4") || it.contains(".mkv") || it.contains(".webm") || it.contains(".m3u8") }
    var firstFrame by remember(media.id, media.uri) { mutableStateOf<Bitmap?>(null) }
    LaunchedEffect(media.id, media.uri, isVideo) {
        firstFrame = if (isVideo) ArtworkPalette.firstVideoFrame(context, media.uri) else null
    }
    Card(
        modifier = modifier.aspectRatio(.78f).clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = if (active) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surfaceContainerLow),
        shape = MaterialTheme.shapes.large
    ) {
        Box(Modifier.fillMaxSize()) {
            if (firstFrame != null) {
                Image(firstFrame!!.asImageBitmap(), contentDescription = "${media.title} first frame", contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
            } else {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.primaryContainer) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(if (isVideo) Icons.Filled.VideoLibrary else Icons.Filled.MusicNote, null, modifier = Modifier.size(42.dp), tint = MaterialTheme.colorScheme.onPrimaryContainer)
                    }
                }
            }
            Surface(
                modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth(),
                color = Color.Black.copy(alpha = .62f)
            ) {
                Column(Modifier.padding(horizontal = 10.dp, vertical = 8.dp)) {
                    Text(media.title, style = MaterialTheme.typography.titleSmall, color = Color.White, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Text(media.artist, style = MaterialTheme.typography.bodySmall, color = Color.White.copy(alpha = .78f), maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
            }
        }
    }
}

@Composable
private fun HeroCard(snapshot: PlayerSnapshot, text: AppText, onPlay: () -> Unit, onAddStream: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().heightIn(min = 210.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        shape = MaterialTheme.shapes.extraLarge
    ) {
        Column(Modifier.padding(24.dp), verticalArrangement = Arrangement.SpaceBetween) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(color = MaterialTheme.colorScheme.primary, shape = CircleShape, modifier = Modifier.size(52.dp)) {
                    Box(contentAlignment = Alignment.Center) { Icon(Icons.Filled.GraphicEq, null, tint = MaterialTheme.colorScheme.onPrimary) }
                }
                Spacer(Modifier.width(16.dp))
                Column { Text("Buge Music", style = MaterialTheme.typography.headlineSmall); Text(text.playerReady, color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = .7f)) }
            }
            Spacer(Modifier.height(20.dp))
            Text(snapshot.current?.title ?: "Every sound. Every screen.", style = MaterialTheme.typography.headlineSmall)
            Text(snapshot.current?.artist ?: "HTTP · HTTPS · HLS / M3U8 · device media", color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = .78f))
            Spacer(Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilledTonalButton(onClick = onAddStream) { Icon(Icons.Filled.AddLink, null); Spacer(Modifier.width(8.dp)); Text(text.addStream) }
                if (snapshot.current != null) FilledTonalButton(onClick = onPlay) { Icon(Icons.Filled.PlayArrow, null); Spacer(Modifier.width(6.dp)); Text(text.nowPlaying) }
            }
        }
    }
}

@Composable
private fun LibraryScreen(
    state: BugeUiState, text: AppText,
    onPlay: (BugeMedia, List<BugeMedia>) -> Unit,
    onFavorite: (BugeMedia) -> Unit,
    onAddQueue: (BugeMedia) -> Unit,
    onScan: () -> Unit
) {
    LazyColumn(Modifier.fillMaxSize(), contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item { SectionHeading(text.deviceMedia, state.library.size) }
        item {
            FilledTonalButton(onClick = onScan, enabled = !state.isScanning, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Filled.Search, null); Spacer(Modifier.width(8.dp)); Text(if (state.isScanning) text.scanning else text.scanDevice)
            }
        }
        if (state.library.isEmpty() && !state.isScanning) item { EmptyCard(text.emptyLibrary) }
        items(state.library, key = { it.id }) { media ->
            MediaRow(media, state.player.current?.uri == media.uri, state.favorites.any { it.uri == media.uri }, onPlay = { onPlay(media, state.library) }, onFavorite = { onFavorite(media) }, onAddQueue = { onAddQueue(media) })
        }
        if (state.favorites.isNotEmpty()) {
            item { Spacer(Modifier.height(6.dp)); SectionHeading(text.favorites, state.favorites.size) }
            items(state.favorites, key = { "fav-${it.id}" }) { media ->
                MediaRow(media, state.player.current?.uri == media.uri, true, onPlay = { onPlay(media, state.favorites) }, onFavorite = { onFavorite(media) }, onAddQueue = { onAddQueue(media) })
            }
        }
        item { Spacer(Modifier.height(92.dp)) }
    }
}

@Composable
private fun SettingsScreen(settings: UserSettings, text: AppText, onSave: (UserSettings) -> Unit, onClearQueue: () -> Unit, queue: List<BugeMedia>) {
    LazyColumn(Modifier.fillMaxSize(), contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item { Text(text.settings, style = MaterialTheme.typography.headlineMedium); Text("Buge Player ${BuildConfig.VERSION_NAME}", color = MaterialTheme.colorScheme.onSurfaceVariant) }
        item { PreferenceCard(text.theme) {
            OptionChips(listOf(AppTheme.SYSTEM to text.system, AppTheme.LIGHT to text.light, AppTheme.DARK to text.dark), settings.theme) { onSave(settings.copy(theme = it)) }
        } }
        item { PreferenceCard(text.accent) {
            OptionChips(listOf(
                AccentMode.SYSTEM to text.system, AccentMode.ARTWORK to text.artworkColor, AccentMode.VIOLET to text.violet,
                AccentMode.OCEAN to text.ocean, AccentMode.SUNSET to text.sunset, AccentMode.FOREST to text.forest
            ), settings.accent) { onSave(settings.copy(accent = it)) }
            Text(text.dynamicNote, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 8.dp))
        } }
        item { LanguagePreference(settings = settings, text = text, onSave = onSave) }
        item { ToggleRow(text.keepScreenOn, settings.keepScreenOn) { onSave(settings.copy(keepScreenOn = it)) } }
        item { ToggleRow(text.autoplay, settings.autoplay) { onSave(settings.copy(autoplay = it)) } }
        item { PreferenceCard(text.queue) {
            Text("${queue.size} ${if (queue.size == 1) "item" else "items"}", color = MaterialTheme.colorScheme.onSurfaceVariant)
            TextButton(onClick = onClearQueue, enabled = queue.isNotEmpty()) { Text(text.clearQueue) }
        } }
        item { Text("© 2026 Buge Studio", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(8.dp)) }
    }
}

@Composable
private fun LanguagePreference(settings: UserSettings, text: AppText, onSave: (UserSettings) -> Unit) {
    var showLanguageDialog by rememberSaveable { mutableStateOf(false) }
    PreferenceCard(text.language) {
        Surface(
            onClick = { showLanguageDialog = true },
            shape = MaterialTheme.shapes.large,
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(Modifier.padding(horizontal = 16.dp, vertical = 14.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Language, null, tint = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.width(12.dp))
                Text(settings.language.displayName, modifier = Modifier.weight(1f), style = MaterialTheme.typography.titleSmall)
                Icon(Icons.Filled.ArrowBack, "Open language list", modifier = Modifier.rotate(180f))
            }
        }
    }
    if (showLanguageDialog) {
        AlertDialog(
            onDismissRequest = { showLanguageDialog = false },
            title = { Text(text.language, style = MaterialTheme.typography.titleLarge) },
            text = {
                Column(
                    Modifier.fillMaxWidth().heightIn(max = 420.dp).verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AppLanguage.entries.forEach { language ->
                        val selected = language == settings.language
                        Surface(
                            onClick = { onSave(settings.copy(language = language)); showLanguageDialog = false },
                            shape = if (selected) MaterialTheme.shapes.extraLarge else MaterialTheme.shapes.large,
                            color = if (selected) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surfaceContainerHigh,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(Modifier.padding(horizontal = 16.dp, vertical = 13.dp), verticalAlignment = Alignment.CenterVertically) {
                                Text(language.displayName, modifier = Modifier.weight(1f), style = MaterialTheme.typography.titleMedium)
                                if (selected) Icon(Icons.Filled.GraphicEq, "Selected", tint = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }
                }
            },
            confirmButton = { TextButton(onClick = { showLanguageDialog = false }) { Text(text.cancel) } }
        )
    }
}

@Composable
private fun <T> OptionChips(options: List<Pair<T, String>>, selected: T, onSelect: (T) -> Unit) {
    Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        options.forEach { (value, label) ->
            val isSelected = selected == value
            Surface(
                onClick = { onSelect(value) },
                shape = if (isSelected) MaterialTheme.shapes.extraLarge else MaterialTheme.shapes.large,
                color = if (isSelected) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surfaceContainerHigh,
                contentColor = if (isSelected) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.height(40.dp)
            ) {
                Text(label, modifier = Modifier.padding(horizontal = 18.dp, vertical = 10.dp), style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}

@Composable
private fun PreferenceCard(title: String, content: @Composable () -> Unit) {
    Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow), shape = MaterialTheme.shapes.large, modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(18.dp)) { Text(title, style = MaterialTheme.typography.titleMedium); Spacer(Modifier.height(12.dp)); content() }
    }
}

@Composable
private fun ToggleRow(title: String, checked: Boolean, onChange: (Boolean) -> Unit) {
    Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow), shape = MaterialTheme.shapes.large, modifier = Modifier.fillMaxWidth().clickable { onChange(!checked) }) {
        Row(Modifier.padding(horizontal = 18.dp, vertical = 13.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(title, Modifier.weight(1f), style = MaterialTheme.typography.titleMedium)
            Switch(checked = checked, onCheckedChange = onChange)
        }
    }
}

@Composable
private fun SectionHeading(title: String, count: Int) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(title, style = MaterialTheme.typography.titleLarge, modifier = Modifier.weight(1f))
        if (count > 0) AssistChip(onClick = {}, label = { Text(count.toString()) })
    }
}

@Composable
private fun EmptyCard(message: String) {
    Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow), shape = MaterialTheme.shapes.large, modifier = Modifier.fillMaxWidth()) {
        Text(message, modifier = Modifier.padding(20.dp), color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun MediaRow(media: BugeMedia, active: Boolean, favorite: Boolean, onPlay: () -> Unit, onFavorite: () -> Unit, onAddQueue: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onPlay),
        colors = CardDefaults.cardColors(containerColor = if (active) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surfaceContainerLow),
        shape = MaterialTheme.shapes.large
    ) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            MediaGlyph(media.kind, Modifier.size(52.dp))
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(media.title, style = MaterialTheme.typography.titleMedium, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text("${media.artist} · ${formatTime(media.durationMs)}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
            IconButton(onClick = onFavorite) { Icon(if (favorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder, null, tint = if (favorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant) }
            IconButton(onClick = onAddQueue) { Icon(Icons.Filled.QueueMusic, null) }
        }
    }
}

@Composable
private fun MediaGlyph(kind: MediaKind, modifier: Modifier = Modifier) {
    Surface(modifier = modifier, shape = MaterialTheme.shapes.medium, color = MaterialTheme.colorScheme.primaryContainer) {
        Box(contentAlignment = Alignment.Center) { Icon(if (kind == MediaKind.VIDEO) Icons.Filled.VideoLibrary else Icons.Filled.MusicNote, null, tint = MaterialTheme.colorScheme.onPrimaryContainer) }
    }
}

@Composable
private fun MiniPlayer(snapshot: PlayerSnapshot, videoFrame: Bitmap?, onOpen: () -> Unit, onTogglePlay: () -> Unit, onClose: () -> Unit) {
    val media = snapshot.current ?: return
    Surface(color = MaterialTheme.colorScheme.surfaceContainerHigh, modifier = Modifier.fillMaxWidth().clickable(onClick = onOpen)) {
        Row(Modifier.padding(horizontal = 10.dp, vertical = 9.dp), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onClose) { Icon(Icons.Filled.Close, "Close current playback") }
            MiniProgressArtwork(media, videoFrame, snapshot, Modifier.size(48.dp))
            Spacer(Modifier.width(10.dp))
            Column(Modifier.weight(1f)) { Text(media.title, style = MaterialTheme.typography.titleSmall, maxLines = 1, overflow = TextOverflow.Ellipsis); Text(media.artist, style = MaterialTheme.typography.bodySmall, maxLines = 1, overflow = TextOverflow.Ellipsis) }
            if (snapshot.isBuffering) LinearProgressIndicator(Modifier.width(28.dp)) else IconButton(onClick = onTogglePlay) { Icon(if (snapshot.isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow, if (snapshot.isPlaying) "Pause" else "Play") }
        }
    }
}

@Composable
private fun MiniProgressArtwork(media: BugeMedia, videoFrame: Bitmap?, snapshot: PlayerSnapshot, modifier: Modifier) {
    val progress = if (snapshot.durationMs > 0) (snapshot.positionMs.toFloat() / snapshot.durationMs.toFloat()).coerceIn(0f, 1f) else 0f
    val trackColor = MaterialTheme.colorScheme.primary.copy(alpha = .20f)
    val progressColor = MaterialTheme.colorScheme.primary
    Box(modifier, contentAlignment = Alignment.Center) {
        if (media.kind == MediaKind.VIDEO && videoFrame != null) VideoPoster(videoFrame, Modifier.size(42.dp)) else MediaGlyph(media.kind, Modifier.size(42.dp))
        Canvas(Modifier.fillMaxSize()) {
            val stroke = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
            drawArc(trackColor, startAngle = -90f, sweepAngle = 360f, useCenter = false, style = stroke)
            if (progress > 0f) drawArc(progressColor, startAngle = -90f, sweepAngle = 360f * progress, useCenter = false, style = stroke)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NowPlayingScreen(
    snapshot: PlayerSnapshot, player: Player?, videoFrame: Bitmap?, favorite: Boolean,
    recommendationPool: List<BugeMedia>, onPlayRecommendation: (BugeMedia, List<BugeMedia>) -> Unit,
    text: AppText, onBack: () -> Unit,
    onPlayPause: () -> Unit, onSeek: (Long) -> Unit, onSeekBy: (Long) -> Unit, onNext: () -> Unit,
    onPrevious: () -> Unit, onSpeed: (Float) -> Unit, onShuffle: () -> Unit, onRepeat: () -> Unit,
    onFavorite: () -> Unit, onQueue: () -> Unit
) {
    val media = snapshot.current ?: return
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var isSharing by remember(media.id) { mutableStateOf(false) }
    var shareError by remember(media.id) { mutableStateOf<String?>(null) }
    val randomPicks = remember(media.id, recommendationPool) {
        recommendationPool.filter { it.uri != media.uri }.shuffled().take(6)
    }
    var speedSheet by rememberSaveable { mutableStateOf(false) }
    var showFullscreen by rememberSaveable { mutableStateOf(false) }
    val isVideo = media.kind == MediaKind.VIDEO || media.uri.lowercase().let { it.contains(".mp4") || it.contains(".mkv") || it.contains(".m3u8") }
    if (showFullscreen && isVideo && player != null) {
        FullscreenVideoScreen(
            player = player,
            snapshot = snapshot,
            title = media.title,
            onPlayPause = onPlayPause,
            onSeek = onSeek,
            onExit = { showFullscreen = false }
        )
        return
    }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text.nowPlaying, style = MaterialTheme.typography.titleLarge) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Filled.ArrowBack, "Back") } },
                actions = {
                    IconButton(
                        enabled = !isSharing,
                        onClick = {
                            scope.launch {
                                isSharing = true
                                shareError = runCatching { MediaFileSharer.share(context, media) }.exceptionOrNull()?.let { error ->
                                    if (error is MediaFileSharer.HlsStreamException) {
                                        "HLS/M3U8 streams are segmented and cannot be shared as one media file."
                                    } else {
                                        error.message ?: "Unable to prepare this media file for sharing."
                                    }
                                }
                                isSharing = false
                            }
                        }
                    ) { Icon(if (isSharing) Icons.Filled.GraphicEq else Icons.Filled.Share, if (isSharing) "Preparing media file" else "Share media file") }
                    IconButton(onClick = onFavorite) { Icon(if (favorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder, null) }
                }
            )
        }
    ) { inset ->
        Column(
            Modifier.fillMaxSize().padding(inset).padding(horizontal = 20.dp).verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isVideo && player != null) {
                if (videoFrame != null && !snapshot.isPlaying && snapshot.positionMs <= 0L) {
                    VideoPoster(videoFrame, Modifier.fillMaxWidth().aspectRatio(16f / 9f))
                } else {
                    VideoSurface(player, Modifier.fillMaxWidth().aspectRatio(16f / 9f))
                }
                IconButton(onClick = { showFullscreen = true }) { Icon(Icons.Filled.Fullscreen, "Fullscreen") }
            } else {
                ArtworkOrb(media, snapshot.isPlaying, Modifier.fillMaxWidth().aspectRatio(1f))
            }
            Spacer(Modifier.height(16.dp))
            Text(media.title, style = MaterialTheme.typography.headlineSmall, maxLines = 2, overflow = TextOverflow.Ellipsis)
            Text(media.artist, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Spacer(Modifier.height(12.dp))
            PlayerProgress(snapshot, onSeek)
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(formatTime(snapshot.positionMs), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(if (snapshot.durationMs > 0) formatTime(snapshot.durationMs) else "LIVE", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround, verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onShuffle, colors = IconButtonDefaults.iconButtonColors(contentColor = if (snapshot.shuffleEnabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant)) { Icon(Icons.Filled.Shuffle, "Shuffle") }
                IconButton(onClick = onPrevious, modifier = Modifier.size(56.dp)) { Icon(Icons.Filled.SkipPrevious, "Previous", Modifier.size(34.dp)) }
                FilledIconButton(onClick = onPlayPause, modifier = Modifier.size(72.dp)) { Icon(if (snapshot.isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow, null, Modifier.size(38.dp)) }
                IconButton(onClick = onNext, modifier = Modifier.size(56.dp)) { Icon(Icons.Filled.SkipNext, "Next", Modifier.size(34.dp)) }
                IconButton(onClick = onRepeat, colors = IconButtonDefaults.iconButtonColors(contentColor = if (snapshot.repeatMode != 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant)) { Icon(if (snapshot.repeatMode == 1) Icons.Filled.RepeatOne else Icons.Filled.Repeat, "Repeat") }
            }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                TextButton(onClick = { onSeekBy(-10_000) }) { Icon(Icons.Filled.Replay10, null); Spacer(Modifier.width(4.dp)); Text("10") }
                TextButton(onClick = { speedSheet = true }) { Text("${snapshot.speed}×") }
                TextButton(onClick = { onSeekBy(10_000) }) { Icon(Icons.Filled.Forward10, null); Spacer(Modifier.width(4.dp)); Text("10") }
                TextButton(onClick = onQueue) { Icon(Icons.Filled.QueueMusic, null); Spacer(Modifier.width(4.dp)); Text(text.queue) }
            }
            Spacer(Modifier.height(24.dp))
            if (randomPicks.isNotEmpty()) {
                RandomPicksGrid(
                    picks = randomPicks,
                    text = text,
                    activeUri = media.uri,
                    onPlay = { selected -> onPlayRecommendation(selected, recommendationPool) }
                )
            }
            Spacer(Modifier.height(28.dp))
        }
    }
    if (speedSheet) SpeedSheet(snapshot.speed, onSpeed) { speedSheet = false }
    shareError?.let { message ->
        AlertDialog(
            onDismissRequest = { shareError = null },
            title = { Text("Unable to share media") },
            text = { Text(message) },
            confirmButton = { TextButton(onClick = { shareError = null }) { Text(text.cancel) } }
        )
    }
}

@Composable
private fun ExternalMediaPlayerDialog(
    media: BugeMedia,
    snapshot: PlayerSnapshot,
    player: Player?,
    text: AppText,
    onDismiss: () -> Unit,
    onPlayPause: () -> Unit,
    onSeek: (Long) -> Unit,
    onSeekBy: (Long) -> Unit
) {
    val isVideo = media.kind == MediaKind.VIDEO || media.uri.lowercase().let {
        it.contains(".mp4") || it.contains(".mkv") || it.contains(".webm") || it.contains(".m3u8")
    }
    val activeSnapshot = if (snapshot.current?.uri == media.uri) snapshot else PlayerSnapshot(current = media)
    var showFullscreen by rememberSaveable(media.id) { mutableStateOf(false) }

    if (showFullscreen && isVideo && player != null) {
        FullscreenVideoScreen(
            player = player,
            snapshot = activeSnapshot,
            title = media.title,
            onPlayPause = onPlayPause,
            onSeek = onSeek,
            onExit = { showFullscreen = false }
        )
        return
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false, dismissOnClickOutside = false)
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth().widthIn(max = 680.dp).padding(20.dp),
            shape = MaterialTheme.shapes.extraLarge,
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
            tonalElevation = 6.dp
        ) {
            Column(Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text(text.nowPlaying, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
                        Text(media.title, style = MaterialTheme.typography.titleLarge, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }
                    IconButton(onClick = onDismiss) { Icon(Icons.Filled.Close, text.cancel) }
                }
                Spacer(Modifier.height(16.dp))
                if (isVideo && player != null) {
                    VideoSurface(player, Modifier.fillMaxWidth().aspectRatio(16f / 9f))
                } else if (isVideo) {
                    Surface(
                        modifier = Modifier.fillMaxWidth().aspectRatio(16f / 9f),
                        shape = MaterialTheme.shapes.large,
                        color = MaterialTheme.colorScheme.inverseSurface
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Filled.VideoLibrary, null, modifier = Modifier.size(56.dp), tint = MaterialTheme.colorScheme.inverseOnSurface)
                        }
                    }
                } else {
                    ArtworkOrb(media, activeSnapshot.isPlaying, Modifier.fillMaxWidth().aspectRatio(1f).widthIn(max = 300.dp))
                }
                Spacer(Modifier.height(16.dp))
                Text(media.artist, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Spacer(Modifier.height(8.dp))
                PlayerProgress(activeSnapshot, onSeek)
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(formatTime(activeSnapshot.positionMs), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(if (activeSnapshot.durationMs > 0) formatTime(activeSnapshot.durationMs) else "LIVE", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Spacer(Modifier.height(4.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { onSeekBy(-10_000) }, modifier = Modifier.size(52.dp)) { Icon(Icons.Filled.Replay10, "-10") }
                    FilledIconButton(onClick = onPlayPause, modifier = Modifier.size(68.dp)) {
                        Icon(if (activeSnapshot.isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow, text.playNow, modifier = Modifier.size(36.dp))
                    }
                    IconButton(onClick = { onSeekBy(10_000) }, modifier = Modifier.size(52.dp)) { Icon(Icons.Filled.Forward10, "+10") }
                }
                if (isVideo && player != null) {
                    Spacer(Modifier.height(8.dp))
                    FilledTonalButton(onClick = { showFullscreen = true }) {
                        Icon(Icons.Filled.Fullscreen, null)
                        Spacer(Modifier.width(8.dp))
                        Text(text.video)
                    }
                }
            }
        }
    }
}

@Composable
private fun VideoPoster(frame: Bitmap, modifier: Modifier) {
    Surface(modifier = modifier, shape = MaterialTheme.shapes.large, color = Color.Black) {
        Image(bitmap = frame.asImageBitmap(), contentDescription = "Video first frame", contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
    }
}

@Composable
private fun VideoSurface(player: Player, modifier: Modifier) {
    Surface(modifier = modifier, shape = MaterialTheme.shapes.large, color = Color.Black) {
        AndroidView(factory = { context -> PlayerView(context).apply { useController = false; resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT; this.player = player } }, update = { it.player = player })
    }
}

@Composable
private fun FullscreenVideoScreen(
    player: Player,
    snapshot: PlayerSnapshot,
    title: String,
    onPlayPause: () -> Unit,
    onSeek: (Long) -> Unit,
    onExit: () -> Unit
) {
    val context = LocalContext.current
    val activity = context as? Activity
    var controlsVisible by rememberSaveable { mutableStateOf(true) }
    var controlsNonce by rememberSaveable { mutableStateOf(0) }
    val revealControls = {
        controlsVisible = true
        controlsNonce += 1
    }

    BackHandler(onBack = onExit)
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
    LaunchedEffect(controlsNonce) {
        if (controlsVisible) {
            delay(3_000)
            controlsVisible = false
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
            .background(Color.Black)
            .clickable {
                if (controlsVisible) controlsVisible = false else revealControls()
            }
    ) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { viewContext -> PlayerView(viewContext).apply {
                useController = false
                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
                this.player = player
            } },
            update = { it.player = player }
        )
        AnimatedVisibility(
            visible = controlsVisible,
            modifier = Modifier.fillMaxSize(),
            enter = fadeIn(animationSpec = tween(180)) + scaleIn(initialScale = .96f, animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)),
            exit = fadeOut(animationSpec = tween(220)) + scaleOut(targetScale = .98f, animationSpec = tween(220))
        ) {
            Box(Modifier.fillMaxSize()) {
                Surface(
                    modifier = Modifier.align(Alignment.TopCenter).padding(top = 28.dp, start = 28.dp, end = 28.dp).fillMaxWidth(),
                    color = Color.Black.copy(alpha = .58f),
                    shape = MaterialTheme.shapes.large
                ) {
                    Row(Modifier.padding(horizontal = 16.dp, vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text(title, modifier = Modifier.weight(1f), color = Color.White, maxLines = 1, overflow = TextOverflow.Ellipsis, style = MaterialTheme.typography.titleMedium)
                        IconButton(onClick = onExit) { Icon(Icons.Filled.Close, "Exit fullscreen", tint = Color.White) }
                    }
                }
                FilledIconButton(
                    onClick = { onPlayPause(); revealControls() },
                    modifier = Modifier.align(Alignment.Center).size(76.dp)
                ) {
                    Icon(if (snapshot.isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow, if (snapshot.isPlaying) "Pause" else "Play", modifier = Modifier.size(40.dp))
                }
                Surface(
                    modifier = Modifier.align(Alignment.BottomCenter).padding(start = 42.dp, end = 42.dp, bottom = 42.dp).fillMaxWidth(),
                    color = Color.Black.copy(alpha = .68f),
                    shape = MaterialTheme.shapes.large
                ) {
                    Column(Modifier.padding(horizontal = 16.dp, vertical = 10.dp)) {
                        Slider(
                            value = snapshot.positionMs.coerceIn(0, max(snapshot.durationMs, 1L)).toFloat(),
                            onValueChange = { onSeek(it.toLong()); revealControls() },
                            valueRange = 0f..max(snapshot.durationMs, 1L).toFloat()
                        )
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(formatTime(snapshot.positionMs), color = Color.White, style = MaterialTheme.typography.labelMedium)
                            Text(if (snapshot.durationMs > 0) formatTime(snapshot.durationMs) else "LIVE", color = Color.White, style = MaterialTheme.typography.labelMedium)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ArtworkOrb(media: BugeMedia, playing: Boolean, modifier: Modifier) {
    val rotation by animateFloatAsState(
        targetValue = if (playing) 4f else 0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "artwork-tilt"
    )
    val primary = MaterialTheme.colorScheme.primary
    val secondaryContainer = MaterialTheme.colorScheme.secondaryContainer
    Surface(modifier = modifier.graphicsLayer { rotationZ = rotation }.clip(MaterialTheme.shapes.extraLarge), color = MaterialTheme.colorScheme.primaryContainer) {
        Box(contentAlignment = Alignment.Center) {
            Canvas(Modifier.fillMaxSize().padding(34.dp)) {
                val r = size.minDimension / 2f
                drawCircle(primary, radius = r)
                drawCircle(secondaryContainer, radius = r * .72f)
                drawCircle(primary, radius = r * .17f)
            }
            Icon(if (media.kind == MediaKind.VIDEO) Icons.Filled.VideoLibrary else Icons.Filled.MusicNote, null, modifier = Modifier.size(70.dp), tint = MaterialTheme.colorScheme.onPrimary)
        }
    }
}

@Composable
private fun PlayerProgress(snapshot: PlayerSnapshot, onSeek: (Long) -> Unit) {
    val safeDuration = max(snapshot.durationMs, 1L)
    Slider(value = snapshot.positionMs.coerceIn(0, safeDuration).toFloat(), onValueChange = { onSeek(it.toLong()) }, valueRange = 0f..safeDuration.toFloat())
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SpeedSheet(current: Float, onSpeed: (Float) -> Unit, onDismiss: () -> Unit) {
    ModalBottomSheet(onDismissRequest = onDismiss, sheetState = rememberModalBottomSheetState()) {
        Column(Modifier.padding(24.dp).padding(bottom = 24.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Playback speed", style = MaterialTheme.typography.headlineSmall)
            listOf(.5f, .75f, 1f, 1.25f, 1.5f, 2f).forEach { speed ->
                FilterChip(selected = current == speed, onClick = { onSpeed(speed); onDismiss() }, label = { Text("${speed}×") })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun QueueSheet(text: AppText, queue: List<BugeMedia>, current: BugeMedia?, onPlay: (BugeMedia, List<BugeMedia>) -> Unit, onClear: () -> Unit, onDismiss: () -> Unit) {
    ModalBottomSheet(onDismissRequest = onDismiss, sheetState = rememberModalBottomSheetState()) {
        Column(Modifier.padding(bottom = 24.dp)) {
            Row(Modifier.fillMaxWidth().padding(horizontal = 24.dp), verticalAlignment = Alignment.CenterVertically) {
                Text(text.queue, style = MaterialTheme.typography.headlineSmall, modifier = Modifier.weight(1f))
                TextButton(onClick = { onClear(); onDismiss() }, enabled = queue.isNotEmpty()) { Text(text.clearQueue) }
            }
            if (queue.isEmpty()) EmptyCard("No queued media") else LazyColumn(Modifier.heightIn(max = 430.dp)) {
                items(queue, key = { it.id }) { media -> MediaRow(media, media.uri == current?.uri, false, onPlay = { onPlay(media, queue); onDismiss() }, onFavorite = {}, onAddQueue = {}) }
            }
        }
    }
}

@Composable
private fun NetworkMediaDialog(text: AppText, onDismiss: () -> Unit, onPlay: (BugeMedia) -> Unit) {
    var url by rememberSaveable { mutableStateOf("") }
    var title by rememberSaveable { mutableStateOf("") }
    var artist by rememberSaveable { mutableStateOf("") }
    var artwork by rememberSaveable { mutableStateOf("") }
    var kind by rememberSaveable { mutableStateOf(MediaKind.AUTO) }
    val valid = url.startsWith("http://") || url.startsWith("https://")
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text.addStream) },
        text = {
            Column(Modifier.verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(value = url, onValueChange = { url = it }, label = { Text(text.streamHint) }, isError = url.isNotBlank() && !valid, singleLine = true, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text(text.title) }, singleLine = true, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = artist, onValueChange = { artist = it }, label = { Text(text.artist) }, singleLine = true, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = artwork, onValueChange = { artwork = it }, label = { Text(text.artwork) }, singleLine = true, modifier = Modifier.fillMaxWidth())
                Text(text.mediaType, style = MaterialTheme.typography.labelLarge)
                OptionChips(listOf(MediaKind.AUTO to text.automatic, MediaKind.AUDIO to text.audio, MediaKind.VIDEO to text.video), kind) { kind = it }
            }
        },
        confirmButton = { TextButton(enabled = valid, onClick = { onPlay(BugeMedia(uri = url.trim(), title = title.ifBlank { url.substringAfterLast('/').ifBlank { "Network media" } }, artist = artist.ifBlank { "Network stream" }, artworkUri = artwork.ifBlank { null }, kind = kind)) }) { Text(text.playNow) } },
        dismissButton = { TextButton(onClick = onDismiss) { Text(text.cancel) } }
    )
}

private fun formatTime(ms: Long): String {
    if (ms <= 0) return "0:00"
    val total = ms / 1000
    val hours = total / 3600
    val minutes = (total % 3600) / 60
    val seconds = total % 60
    return if (hours > 0) "%d:%02d:%02d".format(hours, minutes, seconds) else "%d:%02d".format(minutes, seconds)
}
