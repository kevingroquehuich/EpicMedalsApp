package com.roque.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Mission(
    val id: String,
    val title: String,
    val description: String,
    val progress: Float,
    val maxProgress: Int,
    val currentProgress: Int,
    val reward: String,
    @SerialName("iconName")
    val icon: String,
    @SerialName("backgroundColorHex")
    val backgroundColor: String,
    val isCompleted: Boolean = false,
    val difficulty: String
)