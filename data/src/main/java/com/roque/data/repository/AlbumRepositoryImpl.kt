package com.roque.data.repository

import android.content.Context
import android.util.Log
import com.roque.domain.model.AlbumItem
import com.roque.domain.repository.AlbumRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json
import javax.inject.Inject

class AlbumRepositoryImpl @Inject constructor(
    private val context: Context
): AlbumRepository {

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
        isLenient = true
        coerceInputValues = true
    }

    private val medalItems : List<AlbumItem> by lazy {
        val defaultJson = context.assets.open("achievements_mock.json").bufferedReader().use { it.readText() }
        json.decodeFromString<List<AlbumItem>>(defaultJson)
    }

    override fun getAlbumItems(): Flow<List<AlbumItem>>  = flow { emit(medalItems) }
}