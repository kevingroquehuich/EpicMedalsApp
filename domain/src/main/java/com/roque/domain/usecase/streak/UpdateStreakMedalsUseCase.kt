package com.roque.domain.usecase.streak

import com.roque.domain.model.Medal
import com.roque.domain.model.Streak
import com.roque.domain.repository.MedalRepository
import com.roque.domain.repository.StreakRepository
import kotlinx.coroutines.flow.first

class UpdateStreakMedalsUseCase(
    private val medalRepository: MedalRepository,
    private val streakRepository: StreakRepository
) {
    suspend operator fun invoke() {
        val streak = streakRepository.getStreak()
        val currentMedals = medalRepository.medalsFlow().first()

        val updatedMedals = updateConstantPlayerMedal(currentMedals, streak)

        if (updatedMedals != currentMedals) {
            medalRepository.saveMedals(updatedMedals)
        }
    }

    private fun updateConstantPlayerMedal(medals: List<Medal>, streak: Streak): List<Medal> {
        return medals.map { medal ->
            if (medal.id == "m4") {
                // Calcular puntos basados en la racha: 15 puntos por día de racha consecutiva
                val streakBonusPoints = streak.currentStreak * 15

                // Puntos adicionales por logros específicos
                val bonusPoints = when {
                    streak.currentStreak >= 30 -> 100 // Bonus por 30 días
                    streak.currentStreak >= 14 -> 50  // Bonus por 2 semanas
                    streak.currentStreak >= 7 -> 25   // Bonus por 1 semana
                    else -> 0
                }

                val totalPoints = streakBonusPoints + bonusPoints
                val newLevel = minOf((totalPoints / 100) + 1, medal.maxLevel)

                medal.copy(
                    level = newLevel,
                    points = totalPoints % 100,
                    isLocked = false,
                    hasLeveledUp = newLevel > medal.level,
                    nextLevelGoal = when {
                        newLevel >= medal.maxLevel -> "¡Nivel máximo alcanzado!"
                        else -> "Mantén ${100 - (totalPoints % 100)} días más de racha para subir de nivel"
                    }
                )
            } else {
                medal
            }
        }
    }
}