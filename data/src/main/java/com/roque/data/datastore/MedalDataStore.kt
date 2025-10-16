package com.roque.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore("medal_prefs")
private val KEY_MEDALS = stringPreferencesKey("medals_json")

class MedalDataStore @Inject constructor(private val context: Context) {

    // Cache local para evitar escrituras redundantes
    @Volatile
    private var lastSavedJson: String? = null

    fun medalsFlow(): Flow<String?> =
        context.dataStore.data
            .map { prefs -> prefs[KEY_MEDALS] }
            .distinctUntilChanged()

    suspend fun saveMedalsJson(jsonStr: String) {
        if (jsonStr == lastSavedJson) return

        context.dataStore.edit { prefs ->
            prefs[KEY_MEDALS] = jsonStr
        }

        lastSavedJson = jsonStr
    }
}
