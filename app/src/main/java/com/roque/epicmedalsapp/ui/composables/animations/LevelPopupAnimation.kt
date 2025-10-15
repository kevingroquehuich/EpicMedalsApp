package com.roque.epicmedalsapp.ui.composables.animations

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.roque.domain.model.Medal
import com.roque.epicmedalsapp.R
import com.roque.epicmedalsapp.ui.composables.MedalIcon
import com.roque.epicmedalsapp.ui.screens.medals.LevelUpPopup

@Composable
fun LevelPopupAnimation(
    medal: Medal,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit
) {
    val composeAnimations: Map<String, @Composable () -> Unit> = mapOf(
        "pulse" to { PulseAnimation { MedalIcon(medal = medal, modifier = Modifier.size(250.dp)) } },
        "scalepop" to { ScalePopAnimation { MedalIcon(medal = medal, modifier = Modifier.size(250.dp)) } },
        "flash" to { FlashAnimation { MedalIcon(medal = medal, modifier = Modifier.size(250.dp)) } },
        "rotate" to { RotateAnimation { MedalIcon(medal = medal, modifier = Modifier.size(250.dp)) } },
        "shine" to { ShineAnimation { MedalIcon(medal = medal, modifier = Modifier.size(250.dp)) } },
        "bounce" to { BounceAnimation { MedalIcon(medal = medal, modifier = Modifier.size(250.dp)) } },
        "explosion" to { PulseAnimation { MedalIcon(medal = medal, modifier = Modifier.size(250.dp)) } },
        "crownburst" to { CrownBurstAnimation { MedalIcon(medal = medal, modifier = Modifier.size(250.dp)) } },
        "sparkle" to { PulseAnimation { MedalIcon(medal = medal, modifier = Modifier.size(250.dp)) } },
        "confetti" to { ConfettiAnimation { MedalIcon(medal = medal, modifier = Modifier.size(250.dp)) } }
    )

    val lottieMap: Map<String, Int> = mapOf(
        "sparkle" to R.raw.sparkle,
        "confetti" to R.raw.confetti,
        "pulse" to R.raw.sparkle2,
        "scalepop" to R.raw.shine,
        "flash" to R.raw.shine,
        "rotate" to R.raw.sparkle,
        "shine" to R.raw.shine,
        "bounce" to R.raw.shine,
        "explosion" to R.raw.flamefire,
        "crownburst" to R.raw.shine
    )

    val animType = medal.animationType.lowercase()
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(lottieMap[animType] ?: R.raw.sparkle))

    val iterations = if (animType == "explosion") 1 else LottieConstants.IterateForever

    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = iterations
    )

    // LevelUpPopup final
    LevelUpPopup(
        medal = medal,
        composition = composition,
        progress = progress,
        content = composeAnimations[animType] ?: { MedalIcon(medal = medal, modifier = modifier) },
        onDismiss = onDismiss
    )
}
