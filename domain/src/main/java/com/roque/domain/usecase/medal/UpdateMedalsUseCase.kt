package com.roque.domain.usecase.medal

import com.roque.domain.config.GameConfig.MAX_INCREMENT
import com.roque.domain.config.GameConfig.MIN_INCREMENT
import com.roque.domain.config.GameConfig.POINTS_PER_LEVEL
import com.roque.domain.model.Medal
import kotlin.random.Random

class UpdateMedalsUseCase{

    operator fun invoke(
        currentMedals: List<Medal>,
        onLevelUp: (Medal) -> Unit
    ): List<Medal> {
        if (currentMedals.isEmpty()) return currentMedals

        val lastIndex = currentMedals.lastIndex
        val normalMedals = currentMedals.dropLast(1)

        // Medallas normales
        val updatedNormal = normalMedals.map { medal ->
            if (!medal.isLocked && !medal.isMaxLevel) {
                val inc = Random.nextInt(MIN_INCREMENT, MAX_INCREMENT + 1)
                var newPoints = medal.points + inc
                var newLevel = medal.level

                if (newPoints >= POINTS_PER_LEVEL) {
                    if (medal.level + 1 >= medal.maxLevel) {
                        newLevel = medal.maxLevel
                        newPoints = POINTS_PER_LEVEL
                    } else {
                        newLevel = medal.level + 1
                        newPoints = 0
                        onLevelUp(medal.copy(level = newLevel))
                    }
                }
                medal.copy(level = newLevel, points = newPoints)
            } else medal
        }

        // Última medalla (dependiente)
        val lastMedal = currentMedals[lastIndex]
        val completedCount = updatedNormal.count { it.level >= it.maxLevel }
        val dynamicMaxLevel = updatedNormal.size
        val isUnlocked = completedCount > 0 || !lastMedal.isLocked
        val newLevel = if (isUnlocked) completedCount.coerceAtMost(dynamicMaxLevel) else lastMedal.level

        var updatedLast = lastMedal.copy(
            isLocked = !isUnlocked,
            points = if (isUnlocked) newLevel else 0,
            level = newLevel,
            maxLevel = dynamicMaxLevel
        )

        if (updatedLast.level >= updatedLast.maxLevel && !updatedLast.hasLeveledUp && updatedLast.maxLevel > 0) {
            onLevelUp(updatedLast.copy(hasLeveledUp = true))
            updatedLast = updatedLast.copy(hasLeveledUp = true)
        }

        return updatedNormal + updatedLast
    }
}
