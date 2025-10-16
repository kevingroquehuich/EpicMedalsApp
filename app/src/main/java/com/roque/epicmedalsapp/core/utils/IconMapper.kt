package com.roque.epicmedalsapp.core.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.ui.graphics.vector.ImageVector

fun getIconFromName(iconName: String): ImageVector {
    return when (iconName) {
        "LocalFireDepartment" -> Icons.Default.LocalFireDepartment
        "FitnessCenter" -> Icons.Default.FitnessCenter
        "Schedule" -> Icons.Default.Schedule
        "TrendingUp" -> Icons.Default.TrendingUp
        "EmojiEvents" -> Icons.Default.EmojiEvents
        else -> Icons.Default.Star
    }
}