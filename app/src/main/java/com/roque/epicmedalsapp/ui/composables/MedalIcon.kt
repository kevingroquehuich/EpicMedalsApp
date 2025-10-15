package com.roque.epicmedalsapp.ui.composables

import android.annotation.SuppressLint
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.roque.domain.model.Medal
import kotlin.math.min

@SuppressLint("LocalContextResourcesRead")
@Composable
fun MedalIcon(medal: Medal, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val iconResId = remember(medal.icon) {
        context.resources.getIdentifier(
            medal.icon,
            "drawable",
            context.packageName
        )
    }

    Icon(
        painter = painterResource(id = iconResId),
        contentDescription = medal.name,
        modifier = modifier,
        tint = if (medal.isLocked)
            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
        else
            Color.Unspecified
    )
}