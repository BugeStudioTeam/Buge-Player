package com.buge.player.data

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.bugeDataStore by preferencesDataStore(name = "buge_player")

class PlayerRepository(private val context: Context) {
    private object Keys {
        val theme = stringPreferencesKey("theme")
        val accent = stringPreferencesKey("accent")
        val language = stringPreferencesKey("language")
        val keepScreenOn = booleanPreferencesKey("keep_screen_on")
        val autoplay = booleanPreferencesKey("autoplay")
        val favorites = stringPreferencesKey("favorites")
        val recent = stringPreferencesKey("recent")
        val queue = stringPreferencesKey("queue")
        val onboardingCompleted = booleanPreferencesKey("onboarding_completed")
    }

    val settings: Flow<UserSettings> = context.bugeDataStore.data.map { preferences ->
        UserSettings(
            theme = enumOrDefault(preferences[Keys.theme], AppTheme.SYSTEM),
            accent = enumOrDefault(preferences[Keys.accent], AccentMode.SYSTEM),
            language = enumOrDefault(preferences[Keys.language], AppLanguage.ENGLISH),
            keepScreenOn = preferences[Keys.keepScreenOn] ?: false,
            autoplay = preferences[Keys.autoplay] ?: true
        )
    }

    val favorites: Flow<List<BugeMedia>> = mediaFlow(Keys.favorites)
    val recent: Flow<List<BugeMedia>> = mediaFlow(Keys.recent)
    val queue: Flow<List<BugeMedia>> = mediaFlow(Keys.queue)
    val onboardingCompleted: Flow<Boolean> = context.bugeDataStore.data.map { it[Keys.onboardingCompleted] ?: false }

    suspend fun saveSettings(settings: UserSettings) = context.bugeDataStore.edit {
        it[Keys.theme] = settings.theme.name
        it[Keys.accent] = settings.accent.name
        it[Keys.language] = settings.language.name
        it[Keys.keepScreenOn] = settings.keepScreenOn
        it[Keys.autoplay] = settings.autoplay
    }

    suspend fun toggleFavorite(media: BugeMedia) = context.bugeDataStore.edit { prefs ->
        val list = decode(prefs[Keys.favorites])
        prefs[Keys.favorites] = encode(if (list.any { it.uri == media.uri }) list.filterNot { it.uri == media.uri } else list + media)
    }

    suspend fun remember(media: BugeMedia) = context.bugeDataStore.edit { prefs ->
        val updated = (listOf(media) + decode(prefs[Keys.recent]).filterNot { it.uri == media.uri }).take(30)
        prefs[Keys.recent] = encode(updated)
    }

    suspend fun saveQueue(items: List<BugeMedia>) = context.bugeDataStore.edit { it[Keys.queue] = encode(items) }
    suspend fun completeOnboarding() = context.bugeDataStore.edit { it[Keys.onboardingCompleted] = true }

    private fun mediaFlow(key: Preferences.Key<String>): Flow<List<BugeMedia>> = context.bugeDataStore.data.map { decode(it[key]) }
    private fun decode(value: String?): List<BugeMedia> = value.orEmpty().split("~").mapNotNull(BugeMedia::deserialize)
    private fun encode(items: List<BugeMedia>): String = items.joinToString("~") { it.serialize() }

    private inline fun <reified T : Enum<T>> enumOrDefault(value: String?, fallback: T): T =
        value?.let { runCatching { enumValueOf<T>(it) }.getOrNull() } ?: fallback
}
