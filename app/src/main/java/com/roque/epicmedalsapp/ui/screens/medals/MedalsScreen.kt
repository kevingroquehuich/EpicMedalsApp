package com.roque.epicmedalsapp.ui.screens.medals

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import coil3.compose.AsyncImage
import com.roque.epicmedalsapp.R
import com.roque.epicmedalsapp.ui.composables.MedalCard
import com.roque.epicmedalsapp.ui.composables.animations.LevelPopupAnimation
import kotlinx.coroutines.awaitCancellation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedalsScreen(
    medalsViewModel: MedalsViewModel
) {

    val lifecycleOwner = LocalLifecycleOwner.current
    val medals by medalsViewModel.medals.collectAsState()
    val leveledUpMedal by medalsViewModel.leveledUpMedal.collectAsState()


    LaunchedEffect(Unit) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            medalsViewModel.startEngine()
            try {
                awaitCancellation()
            } finally {
                medalsViewModel.stopEngine()
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Spacer(modifier = Modifier.height(24.dp))
                ProfileSection(medalsViewModel)
                Spacer(modifier = Modifier.height(32.dp))
            }

            items(items = medals) { medal ->
                val extraSpacing by animateDpAsState(
                    targetValue = if (medal.isMaxLevel) 28.dp else 12.dp,
                    animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing),
                    label = "extraSpacing"
                )

                Column(
                    modifier = Modifier
                        .padding(top = extraSpacing / 1.5f, bottom = extraSpacing / 2)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    MedalCard(medal)
                }
            }

            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }

        AnimatedVisibility(
            visible = leveledUpMedal != null,
            enter = fadeIn() + scaleIn(initialScale = 0.8f),
            exit = fadeOut() + scaleOut(targetScale = 0.8f)
        ) {
            leveledUpMedal?.let {
                LevelPopupAnimation(
                    medal = it,
                    onDismiss = { medalsViewModel.clearLeveledUp() }
                )
            }
        }
    }

}

@Composable
fun ProfileSection(medalsViewModel: MedalsViewModel) {
    var tapCount by remember { mutableStateOf(0) }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        AsyncImage(
            model = "https://fiverr-res.cloudinary.com/images/q_auto,f_auto/gigs/165844201/original/c0aee438ce5e30b854ad37a9d90157f53cf3bb52/create-the-profile-cartoon-pictures.png",
            contentDescription = "Profile picture",
            modifier = Modifier
                .size(150.dp)
                .clip(CircleShape)
                .clickable {
                    tapCount += 1
                    if (tapCount >= 5) {
                        medalsViewModel.resetAll()
                        tapCount = 0
                    }
                },
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.txt_name_user),
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = stringResource(R.string.txt_email_user),
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 14.sp
        )
    }
}


