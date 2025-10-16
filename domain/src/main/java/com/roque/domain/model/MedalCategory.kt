package com.roque.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class MedalCategory {
    PROGRESS, STREAK, PERFORMANCE, ACTIVITY, CASINO, STRATEGY, COLLECTION, MISSIONS, LEGEND
}
