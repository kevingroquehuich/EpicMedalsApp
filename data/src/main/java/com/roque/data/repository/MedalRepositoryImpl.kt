package com.roque.data.repository

import android.content.Context
import android.util.Log
import com.roque.data.datastore.MedalDataStore
import com.roque.domain.model.Medal
import com.roque.domain.repository.MedalRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import javax.inject.Inject

class MedalRepositoryImpl @Inject constructor(
    private val context: Context,
    private val dataStore: MedalDataStore
) : MedalRepository {
    private val json = Json { encodeDefaults = true; ignoreUnknownKeys = true }

    override fun medalsFlow(): Flow<List<Medal>> = flow {
        val storedJson = dataStore.medalsFlow().first()
        if (storedJson.isNullOrBlank()) {
            val defaultJson = withContext(Dispatchers.IO) {
                context.assets.open("medals_mock.json").bufferedReader().use { it.readText() }
            }
            val defaultList = json.decodeFromString<List<Medal>>(defaultJson)
            dataStore.saveMedalsJson(json.encodeToString(defaultList))
            emit(defaultList)
        } else {
            emit(json.decodeFromString(storedJson))
        }

        emitAll(dataStore.medalsFlow().map { jsonStr ->
            json.decodeFromString<List<Medal>>(jsonStr ?: return@map emptyList())
        })
    }

    override suspend fun saveMedals(medals: List<Medal>) {
        if (medals.isEmpty()) return
        val jsonStr = json.encodeToString(medals)
        dataStore.saveMedalsJson(jsonStr)
    }

    override suspend fun resetAllMedals() {
        val defaultJson = withContext(Dispatchers.IO) {
            context.assets.open("medals_mock.json").bufferedReader().use { it.readText() }
        }
        val resetList = json.decodeFromString<List<Medal>>(defaultJson).map {
            it.copy(level = 1, points = 0)
        }
        saveMedals(resetList)
    }
}
