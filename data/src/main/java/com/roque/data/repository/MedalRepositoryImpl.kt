package com.roque.data.repository

import android.content.Context
import com.roque.data.datastore.MedalDataStore
import com.roque.domain.model.Medal
import com.roque.domain.repository.MedalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import javax.inject.Inject

class MedalRepositoryImpl @Inject constructor(
    private val context: Context,
    private val dataStore: MedalDataStore
) : MedalRepository {
    private val json = Json { encodeDefaults = true; ignoreUnknownKeys = true }

    override fun medalsFlow(): Flow<List<Medal>> = dataStore.medalsFlow().map { jsonStr ->
        if (jsonStr.isNullOrBlank()) {
            val defaultJson = context.assets.open("medals_mock.json").bufferedReader().use { it.readText() }
            val defaultList = json.decodeFromString<List<Medal>>(defaultJson)
            dataStore.saveMedalsJson(defaultJson)
            defaultList
        } else {
            json.decodeFromString(jsonStr)
        }
    }

    override suspend fun saveMedals(medals: List<Medal>) {
        val jsonStr = json.encodeToString(medals)
        dataStore.saveMedalsJson(jsonStr)
    }

    override suspend fun resetAllMedals() {
        val defaultJson =
            context.assets.open("medals_mock.json").bufferedReader().use { it.readText() }
        val defaultList = json.decodeFromString<List<Medal>>(defaultJson).map {
            it.copy(level = 1, points = 0)
        }
        saveMedals(defaultList)
    }
}
