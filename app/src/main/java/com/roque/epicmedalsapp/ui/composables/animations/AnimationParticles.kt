package com.roque.epicmedalsapp.ui.composables.animations

import androidx.compose.ui.graphics.Color

data class ConfettiParticle(
    val x: Float,
    val y: Float,
    val size: Float,
    val color: Color,
    val speed: Float,
    val rotation: Float
)

data class ExplosionParticle(
    val angle: Float,
    val speed: Float,
    val color: Color,
    val size: Float,
    val alpha: Float
)