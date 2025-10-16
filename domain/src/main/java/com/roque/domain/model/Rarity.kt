package com.roque.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class Rarity(val displayName: String, val color: String) {
    COMMON("Común", "#9E9E9E"),
    RARE("Rara", "#2196F3"),
    EPIC("Épica", "#9C27B0"),
    LEGENDARY("Legendaria", "#FFD700")
}