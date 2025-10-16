package com.roque.epicmedalsapp.ui.animations

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieConstants
import com.roque.domain.model.AnimationType
import com.roque.domain.model.Medal
import com.roque.epicmedalsapp.R
import com.roque.epicmedalsapp.ui.composables.MedalIcon
import com.roque.epicmedalsapp.ui.composables.LevelUpPopup

@Composable
fun LevelPopupAnimation(
    medal: Medal,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit
) {
    val composeAnimations: Map<AnimationType, @Composable () -> Unit> = mapOf(
        AnimationType.PULSE to { PulseAnimation { MedalIcon(medal, Modifier.size(250.dp)) } },
        AnimationType.SCALE_POP to { ScalePopAnimation { MedalIcon(medal, Modifier.size(250.dp)) } },
        AnimationType.FLASH to { FlashAnimation { MedalIcon(medal, Modifier.size(250.dp)) } },
        AnimationType.ROTATE to { RotateAnimation { MedalIcon(medal, Modifier.size(250.dp)) } },
        AnimationType.SHINE to { ShineAnimation { MedalIcon(medal, Modifier.size(250.dp)) } },
        AnimationType.BOUNCE to { BounceAnimation { MedalIcon(medal, Modifier.size(250.dp)) } },
        AnimationType.EXPLOSION to { PulseAnimation { MedalIcon(medal, Modifier.size(250.dp)) } },
        AnimationType.CROWN_BURST to { CrownBurstAnimation { MedalIcon(medal, Modifier.size(250.dp)) } },
        AnimationType.SPARKLE to { PulseAnimation { MedalIcon(medal, Modifier.size(250.dp)) } },
        AnimationType.CONFETTI to { ConfettiAnimation { MedalIcon(medal, Modifier.size(250.dp)) } },
    )

    val lottieMap: Map<AnimationType, Int> = mapOf(
        AnimationType.SPARKLE to R.raw.sparkle,
        AnimationType.CONFETTI to R.raw.confetti,
        AnimationType.PULSE to R.raw.sparkle2,
        AnimationType.SCALE_POP to R.raw.shine,
        AnimationType.FLASH to R.raw.shine,
        AnimationType.ROTATE to R.raw.sparkle,
        AnimationType.SHINE to R.raw.shine,
        AnimationType.BOUNCE to R.raw.shine,
        AnimationType.EXPLOSION to R.raw.flamefire,
        AnimationType.CROWN_BURST to R.raw.shine
    )

    val animationType = medal.animationType
    val iterations = if (animationType == AnimationType.EXPLOSION) 1 else LottieConstants.IterateForever

    val (composition, progress) = lifecycleAwareLottieAnimation(
        resId = lottieMap[animationType] ?: R.raw.sparkle,
        iterations = iterations
    )

    LevelUpPopup(
        medal = medal,
        composition = composition,
        progress = progress,
        content = composeAnimations[animationType] ?: { MedalIcon(medal = medal, modifier = modifier) },
        onDismiss = onDismiss
    )
}

