package com.roque.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Streak(
    val id: String = "main_streak",
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val lastActiveDate: String? = null,
    val streakGoal: Int = 30,
    val isActive: Boolean = false,
    val streakHistory: List<String> = emptyList(),
    val weeklyGoal: Int = 7,
    val monthlyGoal: Int = 30
) {
    val progressPercentage: Float
        get() = (currentStreak.toFloat() / streakGoal.toFloat()).coerceAtMost(1f)

    val weeklyProgress: Float
        get() = (currentStreak.toFloat() / weeklyGoal.toFloat()).coerceAtMost(1f)

    val isWeeklyGoalReached: Boolean
        get() = currentStreak >= weeklyGoal

    val isGoalReached: Boolean
        get() = currentStreak >= streakGoal

    val daysUntilGoal: Int
        get() = (streakGoal - currentStreak).coerceAtLeast(0)
}