package com.roque.domain.repository

import com.roque.domain.model.Streak
import kotlinx.coroutines.flow.Flow

interface StreakRepository {

    fun getStreakFlow(): Flow<Streak>

    suspend fun updateStreak(streak: Streak)

    suspend fun recordDailyActivity()

    suspend fun resetStreak()

    suspend fun getStreak(): Streak
}