package com.roque.epicmedalsapp.ui.animations

import android.view.animation.OvershootInterpolator
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

@Composable
fun rememberAnimationState(isActive: Boolean): Boolean {
    val lifecycleOwner = LocalLifecycleOwner.current
    var play by remember { mutableStateOf(isActive) }

    DisposableEffect(lifecycleOwner, isActive) {
        val observer = LifecycleEventObserver { _, event ->
            play = when (event) {
                Lifecycle.Event.ON_RESUME -> isActive
                Lifecycle.Event.ON_PAUSE,
                Lifecycle.Event.ON_STOP -> false

                else -> play
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }
    return play
}

@Composable
fun PulseAnimation(isActive: Boolean = true, content: @Composable () -> Unit) {
    val play = rememberAnimationState(isActive)
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")

    val scale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            tween(1000, easing = LinearOutSlowInEasing),
            RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    Box(modifier = Modifier.scale(if (play) scale else 1f)) { content() }
}

@Composable
fun FlashAnimation(isActive: Boolean = true, content: @Composable () -> Unit) {
    val play = rememberAnimationState(isActive)
    val infiniteTransition = rememberInfiniteTransition(label = "flash")

    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            tween(400, easing = LinearEasing),
            RepeatMode.Reverse
        ),
        label = "flashAlpha"
    )

    Box(modifier = Modifier.alpha(if (play) alpha else 1f)) { content() }
}


@Composable
fun RotateAnimation(isActive: Boolean = true, content: @Composable () -> Unit) {
    val play = rememberAnimationState(isActive)
    val infiniteTransition = rememberInfiniteTransition(label = "rotate")

    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            tween(6000, easing = LinearEasing)
        ),
        label = "rotateAngle"
    )

    Box(modifier = Modifier.rotate(if (play) rotation else 0f)) { content() }
}

@Composable
fun ScalePopAnimation(isActive: Boolean = true, content: @Composable () -> Unit) {
    var start by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (start && isActive) 1f else 0f,
        animationSpec = tween(700) { OvershootInterpolator(2f).getInterpolation(it) },
        label = "scalePop"
    )
    LaunchedEffect(isActive) { start = isActive }
    Box(modifier = Modifier.scale(scale)) { content() }
}

@Composable
fun ConfettiAnimation(
    isActive: Boolean = true,
    particleCount: Int = 40,
    content: @Composable () -> Unit
) {
    val play = rememberAnimationState(isActive)
    val particles = remember {
        List(particleCount) {
            ConfettiParticle(
                x = Random.nextFloat(),
                y = Random.nextFloat(),
                size = Random.nextFloat() * 10f + 6f,
                color = Color(
                    red = Random.nextFloat(),
                    green = Random.nextFloat(),
                    blue = Random.nextFloat()
                ),
                speed = Random.nextFloat() * 1.2f + 0.4f,
                rotation = Random.nextFloat() * 360f
            )
        }
    }

    val transition = rememberInfiniteTransition(label = "confetti")
    val progress by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(5000, easing = LinearEasing)),
        label = "confettiProgress"
    )

    Box {
        content()

        if (play) {
            Canvas(modifier = Modifier.matchParentSize()) {
                val w = size.width
                val h = size.height
                particles.forEach { p ->
                    val y = (p.y * h + progress * h * p.speed) % h
                    drawCircle(
                        color = p.color.copy(alpha = 0.8f),
                        radius = p.size,
                        center = Offset(p.x * w, y)
                    )
                }
            }
        }
    }
}


@Composable
fun ExplosionAnimation(
    isActive: Boolean = true,
    durationMillis: Int = 1500,
    particleCount: Int = 60,
    colors: List<Color> = listOf(Color.Yellow, Color(0xFFFF9800), Color.White),
    content: @Composable () -> Unit
) {
    val play = rememberAnimationState(isActive)

    val time by animateFloatAsState(
        targetValue = if (play) 1f else 0f,
        animationSpec = tween(durationMillis, easing = LinearOutSlowInEasing),
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

    Box(contentAlignment = Alignment.Center) {
        content()

        if (play) {
            Canvas(modifier = Modifier.matchParentSize()) {
                val c = Offset(size.width / 2, size.height / 2)
                particles.forEach { p ->
                    val rad = Math.toRadians(p.angle.toDouble())
                    val offset = Offset(
                        x = c.x + cos(rad).toFloat() * p.speed * time,
                        y = c.y + sin(rad).toFloat() * p.speed * time
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
}

@Composable
fun CrownBurstAnimation(isActive: Boolean = true, content: @Composable () -> Unit) {
    val play = rememberAnimationState(isActive)
    val infiniteTransition = rememberInfiniteTransition(label = "crownBurst")

    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(6000, easing = LinearEasing)),
        label = "angle"
    )

    Box {
        if (play) {
            Canvas(modifier = Modifier.matchParentSize()) {
                val c = Offset(size.width / 2, size.height / 2)
                repeat(8) { i ->
                    val rot = angle + i * 45f
                    val endX = (c.x + cos(Math.toRadians(rot.toDouble())) * size.minDimension / 2).toFloat()
                    val endY = (c.y + sin(Math.toRadians(rot.toDouble())) * size.minDimension / 2).toFloat()
                    drawCircle(Color.Yellow, radius = 5f, center = Offset(endX, endY))
                }
            }
        }
        content()
    }
}

@Composable
fun ShineAnimation(
    isActive: Boolean = true,
    durationMillis: Int = 1500,
    minAlpha: Float = 0.5f,
    maxAlpha: Float = 1f,
    content: @Composable () -> Unit
) {
    val play = rememberAnimationState(isActive)

    if (!play) {
        Box { content() }
        return
    }

    val infiniteTransition = rememberInfiniteTransition(label = "shine")
    val alpha by infiniteTransition.animateFloat(
        initialValue = minAlpha,
        targetValue = maxAlpha,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shineAlpha"
    )

    Box(modifier = Modifier.alpha(alpha)) {
        content()
    }
}

@Composable
fun BounceAnimation(
    isActive: Boolean = true,
    travelDp: Dp = 12.dp,
    durationMillis: Int = 900,
    easing: Easing = FastOutLinearInEasing,
    content: @Composable () -> Unit
) {
    val play = rememberAnimationState(isActive)

    if (!play) {
        Box { content() }
        return
    }

    val infiniteTransition = rememberInfiniteTransition(label = "bounce")
    val offsetFraction by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis, easing = easing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bounceFraction"
    )

    val offsetDp = -travelDp * offsetFraction

    Box(modifier = Modifier.offset(y = offsetDp)) {
        content()
    }
}

