package com.roque.epicmedalsapp.ui.composables.animations

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random


@Composable
fun ConfettiAnimation(content: @Composable () -> Unit) {
    val confettiCount = 80
    val confetti = remember {
        List(confettiCount) {
            ConfettiParticle(
                x = Random.nextFloat(),
                y = Random.nextFloat(),
                size = Random.nextFloat() * 10f + 6f,
                color = Color(
                    red = Random.nextFloat(),
                    green = Random.nextFloat(),
                    blue = Random.nextFloat()
                ),
                speed = Random.nextFloat() * 1.5f + 0.5f,
                rotation = Random.nextFloat() * 360f
            )
        }
    }

    val infiniteTransition = rememberInfiniteTransition()
    val progress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            tween(durationMillis = 4000, easing = LinearEasing),
            RepeatMode.Restart
        )
    )

    Box {
        content()

        Canvas(modifier = Modifier.matchParentSize()) {
            val width = size.width
            val height = size.height

            confetti.forEach { particle ->
                val newY = (particle.y * height + progress * height * particle.speed) % height
                val rotationOffset = (particle.rotation + progress * 360f) % 360f

                drawCircle(
                    color = particle.color.copy(alpha = 0.9f),
                    radius = particle.size,
                    center = Offset(
                        x = particle.x * width,
                        y = newY
                    )
                )

                drawCircle(
                    color = Color.White.copy(alpha = 0.3f),
                    radius = particle.size / 2,
                    center = Offset(
                        x = particle.x * width + cos(Math.toRadians(rotationOffset.toDouble())).toFloat() * 3,
                        y = newY + sin(Math.toRadians(rotationOffset.toDouble())).toFloat() * 3
                    )
                )
            }
        }
    }
}

@Composable
fun PulseAnimation(content: @Composable () -> Unit) {
    val scale by rememberInfiniteTransition().animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            tween(1000, easing = LinearOutSlowInEasing),
            RepeatMode.Reverse
        )
    )
    Box(modifier = Modifier.scale(scale)) { content() }
}

@Composable
fun ScalePopAnimation(content: @Composable () -> Unit) {
    var start by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (start) 1f else 0f,
        animationSpec = tween(
            durationMillis = 700,
            easing = {
                OvershootInterpolator(2f).getInterpolation(it)
            }
        ),
        label = "scalePop"
    )
    LaunchedEffect(Unit) { start = true }
    Box(modifier = Modifier.scale(scale)) { content() }
}

class OvershootInterpolator(private val tension: Float = 2.0f) {
    fun getInterpolation(input: Float): Float {
        val t = input - 1.0f
        return t * t * ((tension + 1) * t + tension) + 1.0f
    }
}

@Composable
fun FlashAnimation(content: @Composable () -> Unit) {
    val alpha by rememberInfiniteTransition().animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            tween(400, easing = LinearEasing),
            RepeatMode.Reverse
        )
    )
    Box(modifier = Modifier.alpha(alpha)) { content() }
}

@Composable
fun RotateAnimation(content: @Composable () -> Unit) {
    val rotation by rememberInfiniteTransition().animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            tween(6000, easing = LinearEasing)
        )
    )
    Box(modifier = Modifier.rotate(rotation)) { content() }
}

@Composable
fun ShineAnimation(content: @Composable () -> Unit) {
    val alpha by rememberInfiniteTransition().animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            tween(1500, easing = LinearOutSlowInEasing),
            RepeatMode.Reverse
        )
    )
    Box(modifier = Modifier.alpha(alpha)) { content() }
}

@Composable
fun BounceAnimation(content: @Composable () -> Unit) {
    val offsetY by rememberInfiniteTransition().animateFloat(
        initialValue = 0f,
        targetValue = -20f,
        animationSpec = infiniteRepeatable(
            tween(900, easing = FastOutLinearInEasing),
            RepeatMode.Reverse
        )
    )
    Box(modifier = Modifier.offset(y = offsetY.dp)) { content() }
}

@Composable
fun ExplosionAnimation(
    modifier: Modifier = Modifier,
    durationMillis: Int = 1500,
    particleCount: Int = 100,
    colors: List<Color> = listOf(
        Color(0xFFFFC107), // Dorado
        Color(0xFFFF9800), // Naranja
        Color(0xFFFFEB3B), // Amarillo brillante
        Color(0xFFFFFFFF)  // Blanco chispeante
    ),
    content: @Composable () -> Unit
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        content()

        val infiniteTransition = rememberInfiniteTransition(label = "explosion")
        val time by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis, easing = LinearOutSlowInEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "explosionProgress"
        )

        val particles = remember {
            List(particleCount) {
                ExplosionParticle(
                    angle = Random.nextFloat() * 360f,
                    speed = Random.nextFloat() * 300f + 100f,
                    color = colors.random(),
                    size = Random.nextFloat() * 6f + 3f,
                    alpha = Random.nextFloat() * 0.6f + 0.4f
                )
            }
        }

        Canvas(
            modifier = Modifier
                .matchParentSize()
                .graphicsLayer { alpha = 1f - time }
        ) {
            val center = Offset(size.width / 2, size.height / 2)

            particles.forEach { p ->
                val radians = Math.toRadians(p.angle.toDouble())
                val distance = p.speed * time
                val offset = Offset(
                    x = center.x + cos(radians).toFloat() * distance,
                    y = center.y + sin(radians).toFloat() * distance
                )

                drawCircle(
                    color = p.color.copy(alpha = p.alpha * (1 - time)),
                    radius = p.size * (1 - time / 2),
                    center = offset
                )
            }
        }
    }
}

@Composable
fun CrownBurstAnimation(content: @Composable () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition()
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(6000, easing = LinearEasing))
    )
    Box {
        Canvas(modifier = Modifier.matchParentSize()) {
            val center = Offset(size.width / 2, size.height / 2)
            repeat(8) { i ->
                val rotation = angle + i * 45f
                val endX = (center.x + cos(Math.toRadians(rotation.toDouble())) * size.minDimension / 2).toFloat()
                val endY = (center.y + sin(Math.toRadians(rotation.toDouble())) * size.minDimension / 2).toFloat()
                drawCircle(Color.Yellow, radius = 6f, center = Offset(endX, endY))
            }
        }
        content()
    }
}
