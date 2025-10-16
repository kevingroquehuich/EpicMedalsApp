package com.roque.data.repository

import android.content.Context
import com.roque.data.datastore.MedalDataStore
import com.roque.domain.model.Medal
import com.roque.domain.repository.MedalRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import javax.inject.Inject

class MedalRepositoryImpl @Inject constructor(
    private val context: Context,
    private val dataStore: MedalDataStore
) : MedalRepository {
    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
        isLenient = true
        coerceInputValues = true
    }

    private val defaultMedals: List<Medal> by lazy {
        val defaultJson =
            context.assets.open("medals_mock.json").bufferedReader().use { it.readText() }
        json.decodeFromString<List<Medal>>(defaultJson)
    }

    override fun medalsFlow(): Flow<List<Medal>> = flow {
        val stored = dataStore.medalsFlow().firstOrNull()
        val initial = if (stored.isNullOrBlank()) {
            val initialized = defaultMedals.map {
                it.copy(level = 1, points = 0, isLocked = it.id == "m10")
            }
            dataStore.saveMedalsJson(json.encodeToString(initialized))
            initialized
        } else {
            json.decodeFromString(stored)
        }
        emit(initial)
        emitAll(dataStore.medalsFlow().mapNotNull { jsonStr ->
            jsonStr?.let { json.decodeFromString<List<Medal>>(it) }
        })
    }.flowOn(Dispatchers.IO)

    override suspend fun saveMedals(medals: List<Medal>) {
        if (medals.isEmpty()) return
        withContext(Dispatchers.IO) {
            dataStore.saveMedalsJson(json.encodeToString(medals))
        }
    }

    override suspend fun resetAllMedals() {
        withContext(Dispatchers.IO) {
            val resetList = defaultMedals.map {
                it.copy(level = 1, points = 0, isLocked = it.id == "m10")
            }
            saveMedals(resetList)
        }
    }
}
