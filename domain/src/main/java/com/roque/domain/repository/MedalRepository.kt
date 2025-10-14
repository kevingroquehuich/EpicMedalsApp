package com.roque.domain.repository

import com.roque.domain.model.Medal
import kotlinx.coroutines.flow.Flow

interface MedalRepository {

    fun medalsFlow(): Flow<List<Medal>>

    suspend fun saveMedals(medals: List<Medal>)

    suspend fun resetAllMedals()

}