package com.roque.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore("medal_prefs")
private val KEY_MEDALS = stringPreferencesKey("medals_json")

class MedalDataStore @Inject constructor(private val context: Context) {
    fun medalsFlow(): Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[KEY_MEDALS]
    }
    suspend fun saveMedalsJson(jsonStr: String) {
        context.dataStore.edit { prefs ->
            prefs[KEY_MEDALS] = jsonStr
        }
    }
}