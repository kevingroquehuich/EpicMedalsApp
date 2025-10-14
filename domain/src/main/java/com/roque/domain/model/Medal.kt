package com.roque.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Medal(
    val id: String,
    val name: String,
    val description: String,
    val icon: String,
    val category: String,
    val rarity: String,
    val backgroundColor: String,
    val progressColor: String,
    val level: Int,
    val points: Int,
    val maxLevel: Int,
    val reward: String,
    val unlockedAt: String,
    val nextLevelGoal: String,
    val isLocked: Boolean,
    val animationType: String
)