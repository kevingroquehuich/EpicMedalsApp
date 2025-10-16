package com.roque.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AlbumItem(
    val id: String,
    val name: String,
    val description: String,
    @SerialName("iconName") val icon: String,
    val rarity: Rarity,
    val isUnlocked: Boolean,
    val unlockedDate: String? = null,
    val category: String
)
