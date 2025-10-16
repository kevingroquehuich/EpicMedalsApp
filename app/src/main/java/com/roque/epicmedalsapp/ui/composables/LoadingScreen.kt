package com.roque.epicmedalsapp.ui.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.roque.epicmedalsapp.R
import com.roque.epicmedalsapp.ui.animations.LifecycleAwareLottieView

@Composable
fun LoadingScreen() {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        LifecycleAwareLottieView(
            resId = R.raw.loading,
            modifier = Modifier.size(200.dp),
            alignment = Alignment.Center
        )
    }
}
