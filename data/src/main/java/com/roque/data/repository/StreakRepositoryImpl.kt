package com.roque.data.repository

import com.roque.data.datastore.StreakDataStore
import com.roque.domain.model.Streak
import com.roque.domain.repository.StreakRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import javax.inject.Inject

class StreakRepositoryImpl @Inject constructor(
    private val dataStore: StreakDataStore
) : StreakRepository {

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
        isLenient = true
        coerceInputValues = true
    }

    private val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE

    private val defaultStreak = Streak()

    override fun getStreakFlow(): Flow<Streak> = flow {
        val stored = dataStore.streakFlow().firstOrNull()
        val initial = if (stored.isNullOrBlank()) {
            dataStore.saveStreakJson(json.encodeToString(defaultStreak))
            defaultStreak
        } else {
            json.decodeFromString(stored)
        }
        emit(initial)
        emitAll(dataStore.streakFlow().mapNotNull { jsonStr ->
            jsonStr?.let { json.decodeFromString<Streak>(it) }
        })
    }.flowOn(Dispatchers.IO)

    override suspend fun updateStreak(streak: Streak) {
        withContext(Dispatchers.IO) {
            dataStore.saveStreakJson(json.encodeToString(streak))
        }
    }

    override suspend fun recordDailyActivity() {
        withContext(Dispatchers.IO) {
            val currentStreak = getStreak()
            val today = LocalDate.now().format(dateFormatter)

            // Si ya registramos actividad hoy, no hacer nada
            if (currentStreak.lastActiveDate == today) return@withContext

            val newStreak = calculateNewStreak(currentStreak, today)
            updateStreak(newStreak)
        }
    }

    override suspend fun resetStreak() {
        withContext(Dispatchers.IO) {
            updateStreak(defaultStreak)
        }
    }

    override suspend fun getStreak(): Streak {
        return dataStore.streakFlow().firstOrNull()?.let { jsonStr ->
            if (jsonStr.isNullOrBlank()) defaultStreak
            else json.decodeFromString(jsonStr)
        } ?: defaultStreak
    }

    private fun calculateNewStreak(currentStreak: Streak, today: String): Streak {
        val todayDate = LocalDate.parse(today, dateFormatter)

        val newStreakCount = when {
            currentStreak.lastActiveDate.isNullOrBlank() -> 1
            else -> {
                val lastActiveDate = LocalDate.parse(currentStreak.lastActiveDate, dateFormatter)
                val daysBetween = ChronoUnit.DAYS.between(lastActiveDate, todayDate)

                when {
                    daysBetween == 1L -> currentStreak.currentStreak + 1
                    daysBetween == 0L -> currentStreak.currentStreak
                    else -> 1
                }
            }
        }

        val newHistory = if (currentStreak.streakHistory.contains(today)) {
            currentStreak.streakHistory
        } else {
            currentStreak.streakHistory + today
        }

        return currentStreak.copy(
            currentStreak = newStreakCount,
            longestStreak = maxOf(currentStreak.longestStreak, newStreakCount),
            lastActiveDate = today,
            isActive = true,
            streakHistory = newHistory
        )
    }
}