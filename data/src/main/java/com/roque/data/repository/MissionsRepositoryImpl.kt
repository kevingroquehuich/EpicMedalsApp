package com.roque.data.repository

import android.content.Context
import com.roque.domain.model.Mission
import com.roque.domain.repository.MissionsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json
import javax.inject.Inject

class MissionsRepositoryImpl @Inject constructor(
    private val context: Context,
): MissionsRepository {

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
        isLenient = true
        coerceInputValues = true
    }

    private val missionsList : List<Mission> by lazy {
        val defaultJson = context.assets.open("missions.json").bufferedReader().use { it.readText() }
        json.decodeFromString<List<Mission>>(defaultJson)
    }

    override fun getMissions(): Flow<List<Mission>> = flow {
        emit(missionsList)
    }
}