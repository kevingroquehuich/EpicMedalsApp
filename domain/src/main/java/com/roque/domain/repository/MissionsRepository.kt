package com.roque.domain.repository

import com.roque.domain.model.Mission
import kotlinx.coroutines.flow.Flow

interface MissionsRepository {

    fun getMissions(): Flow<List<Mission>>
}