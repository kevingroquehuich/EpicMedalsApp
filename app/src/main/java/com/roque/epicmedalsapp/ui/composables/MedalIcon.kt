package com.roque.epicmedalsapp.ui.composables

import android.annotation.SuppressLint
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.roque.domain.model.Medal

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