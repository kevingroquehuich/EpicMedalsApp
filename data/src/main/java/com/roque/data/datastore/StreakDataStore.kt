package com.roque.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.streakDataStore by preferencesDataStore("streak_prefs")
private val KEY_STREAK = stringPreferencesKey("streak_json")

class StreakDataStore @Inject constructor(private val context: Context) {

    // Cache local para evitar escrituras redundantes
    @Volatile
    private var lastSavedJson: String? = null

    fun streakFlow(): Flow<String?> =
        context.streakDataStore.data
            .map { prefs -> prefs[KEY_STREAK] }
            .distinctUntilChanged()

    suspend fun saveStreakJson(jsonStr: String): Result<Unit> {
        return try {
            if (jsonStr == lastSavedJson) return Result.success(Unit)

            context.streakDataStore.edit { prefs ->
                prefs[KEY_STREAK] = jsonStr
            }
            lastSavedJson = jsonStr
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}