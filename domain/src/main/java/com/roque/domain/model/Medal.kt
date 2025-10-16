package com.roque.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Medal(
    val id: String,
    val name: String,
    val description: String,
    val icon: String,
    val category: MedalCategory = MedalCategory.PROGRESS,
    val rarity: Rarity = Rarity.COMMON,
    val backgroundColor: String = "#FFFFFF",
    val progressColor: String = "#2196F3",
    val level: Int = 1,
    val points: Int = 0,
    val maxLevel: Int = 10,
    val reward: String = "",
    val unlockedAt: String = "",
    val nextLevelGoal: String = "",
    val isLocked: Boolean = false,
    val animationType: AnimationType = AnimationType.SHINE,
    val hasLeveledUp: Boolean = false
){
    val isMaxLevel: Boolean get() = level >= maxLevel

}
