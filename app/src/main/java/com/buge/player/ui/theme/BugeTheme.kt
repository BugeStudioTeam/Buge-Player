package com.buge.player.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.buge.player.R
import com.buge.player.data.AccentMode
import com.buge.player.data.AppTheme
import com.buge.player.data.UserSettings

private val LightScheme = lightColorScheme(
    primary = Color(0xFF6750A4), onPrimary = Color.White,
    primaryContainer = Color(0xFFE9DDFF), onPrimaryContainer = Color(0xFF21005D),
    secondary = Color(0xFF625B71), secondaryContainer = Color(0xFFE8DEF8),
    tertiary = Color(0xFF7D5260), tertiaryContainer = Color(0xFFFFD8E4)
)
private val DarkScheme = darkColorScheme(
    primary = Color(0xFFD0BCFF), onPrimary = Color(0xFF381E72),
    primaryContainer = Color(0xFF4F378B), onPrimaryContainer = Color(0xFFE9DDFF),
    secondary = Color(0xFFCCC2DC), secondaryContainer = Color(0xFF4A4458),
    tertiary = Color(0xFFEFB8C8), tertiaryContainer = Color(0xFF633B48)
)

@OptIn(ExperimentalTextApi::class)
private fun flexFont(weight: FontWeight, width: Float, roundedness: Float = 0f): Font = Font(
    resId = R.font.google_sans_flex,
    weight = weight,
    variationSettings = FontVariation.Settings(
        FontVariation.weight(weight.weight),
        FontVariation.width(width),
        FontVariation.Setting("ROND", roundedness)
    )
)

/** Google Sans Flex families tuned for expressive hierarchy: compact labels, neutral prose, and expansive display moments. */
private val GoogleSansFlexDisplay = FontFamily(
    flexFont(FontWeight.W700, 114f, 100f), flexFont(FontWeight.W800, 114f, 100f), flexFont(FontWeight.W900, 114f, 100f)
)
private val GoogleSansFlexText = FontFamily(
    flexFont(FontWeight.W300, 100f, 20f), flexFont(FontWeight.W400, 100f, 20f), flexFont(FontWeight.W500, 100f, 20f), flexFont(FontWeight.W600, 100f, 20f), flexFont(FontWeight.W700, 100f, 20f)
)
private val GoogleSansFlexCompact = FontFamily(
    flexFont(FontWeight.W500, 88f, 36f), flexFont(FontWeight.W600, 88f, 36f), flexFont(FontWeight.W700, 88f, 36f), flexFont(FontWeight.W800, 88f, 36f)
)

private val BugeShapes = Shapes(
    extraSmall = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
    small = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
    medium = androidx.compose.foundation.shape.RoundedCornerShape(18.dp),
    large = androidx.compose.foundation.shape.RoundedCornerShape(28.dp),
    extraLarge = androidx.compose.foundation.shape.RoundedCornerShape(36.dp)
)

private val BugeTypography = Typography().run {
    copy(
        displayLarge = displayLarge.copy(fontFamily = GoogleSansFlexDisplay, fontWeight = FontWeight.W900, fontSize = 57.sp, lineHeight = 61.sp, letterSpacing = (-0.025).em),
        displayMedium = displayMedium.copy(fontFamily = GoogleSansFlexDisplay, fontWeight = FontWeight.W800, fontSize = 45.sp, lineHeight = 50.sp, letterSpacing = (-0.02).em),
        displaySmall = displaySmall.copy(fontFamily = GoogleSansFlexDisplay, fontWeight = FontWeight.W700, fontSize = 36.sp, lineHeight = 41.sp, letterSpacing = (-0.018).em),
        headlineLarge = headlineLarge.copy(fontFamily = GoogleSansFlexDisplay, fontWeight = FontWeight.W700, letterSpacing = (-0.015).em),
        headlineMedium = headlineMedium.copy(fontFamily = GoogleSansFlexDisplay, fontWeight = FontWeight.W700, letterSpacing = (-0.012).em),
        headlineSmall = headlineSmall.copy(fontFamily = GoogleSansFlexDisplay, fontWeight = FontWeight.W800, letterSpacing = (-0.012).em),
        titleLarge = titleLarge.copy(fontFamily = GoogleSansFlexDisplay, fontWeight = FontWeight.W800, letterSpacing = (-0.008).em),
        titleMedium = titleMedium.copy(fontFamily = GoogleSansFlexText, fontWeight = FontWeight.W700, letterSpacing = (-0.003).em),
        titleSmall = titleSmall.copy(fontFamily = GoogleSansFlexText, fontWeight = FontWeight.W700, letterSpacing = (-0.002).em),
        bodyLarge = bodyLarge.copy(fontFamily = GoogleSansFlexText, fontWeight = FontWeight.W400, lineHeight = 24.sp, letterSpacing = 0.006.em),
        bodyMedium = bodyMedium.copy(fontFamily = GoogleSansFlexText, fontWeight = FontWeight.W400, letterSpacing = 0.008.em),
        bodySmall = bodySmall.copy(fontFamily = GoogleSansFlexText, fontWeight = FontWeight.W400, letterSpacing = 0.012.em),
        labelLarge = labelLarge.copy(fontFamily = GoogleSansFlexCompact, fontWeight = FontWeight.W700, letterSpacing = 0.045.em),
        labelMedium = labelMedium.copy(fontFamily = GoogleSansFlexCompact, fontWeight = FontWeight.W600, letterSpacing = 0.055.em),
        labelSmall = labelSmall.copy(fontFamily = GoogleSansFlexCompact, fontWeight = FontWeight.W600, letterSpacing = 0.06.em)
    )
}

@Composable
fun BugeTheme(settings: UserSettings, artworkSeed: Int?, content: @Composable () -> Unit) {
    val systemDark = isSystemInDarkTheme()
    val dark = when (settings.theme) {
        AppTheme.SYSTEM -> systemDark
        AppTheme.DARK -> true
        AppTheme.LIGHT -> false
    }
    val useSystem = settings.accent == AccentMode.SYSTEM && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    val base = when {
        useSystem && dark -> dynamicDarkColorScheme(androidx.compose.ui.platform.LocalContext.current)
        useSystem -> dynamicLightColorScheme(androidx.compose.ui.platform.LocalContext.current)
        dark -> DarkScheme
        else -> LightScheme
    }
    val seed = when (settings.accent) {
        AccentMode.SYSTEM -> null
        AccentMode.ARTWORK -> artworkSeed
        AccentMode.VIOLET -> 0xFF6750A4.toInt()
        AccentMode.OCEAN -> 0xFF006C84.toInt()
        AccentMode.SUNSET -> 0xFF9C4230.toInt()
        AccentMode.FOREST -> 0xFF276A3D.toInt()
    }
    MaterialTheme(
        colorScheme = seed?.let { base.withAccent(Color(it), dark) } ?: base,
        typography = BugeTypography,
        shapes = BugeShapes,
        content = content
    )
}

private fun ColorScheme.withAccent(seed: Color, dark: Boolean): ColorScheme {
    val foreground = if (seed.luminance() > 0.42f) Color(0xFF151217) else Color.White
    val container = lerp(seed, if (dark) Color(0xFF141218) else Color.White, if (dark) 0.46f else 0.78f)
    val onContainer = if (container.luminance() > 0.42f) Color(0xFF17131B) else Color.White
    return copy(
        primary = seed, onPrimary = foreground,
        primaryContainer = container, onPrimaryContainer = onContainer,
        secondary = lerp(seed, tertiary, 0.35f),
        secondaryContainer = lerp(container, surfaceVariant, 0.5f)
    )
}
