package com.roque.epicmedalsapp.ui.screens.medals

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import coil3.compose.AsyncImage
import com.roque.domain.common.UIState
import com.roque.epicmedalsapp.R
import com.roque.epicmedalsapp.ui.animations.LevelPopupAnimation
import com.roque.epicmedalsapp.ui.composables.ErrorScreen
import com.roque.epicmedalsapp.ui.composables.LoadingScreen
import com.roque.epicmedalsapp.ui.composables.MedalCard
import kotlinx.coroutines.awaitCancellation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedalsScreen(
    medalsViewModel: MedalsViewModel
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val medalsState by medalsViewModel.medalsState.collectAsState()
    val leveledUpMedal by medalsViewModel.leveledUpMedal.collectAsState()

    // Mantiene el engine activo mientras el ciclo de vida está en STARTED
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
        when (val state = medalsState) {
            is UIState.Loading -> LoadingScreen()

            is UIState.Error -> ErrorScreen(
                message = state.message,
                onRetry = { medalsViewModel.startEngine() }
            )

            is UIState.Success -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        ProfileCard(medalsViewModel)
                        Spacer(modifier = Modifier.height(16.dp))

                        StatsSection(state.data)
                        Spacer(modifier = Modifier.height(24.dp))
                    }

                    items(items = state.data) { medal ->
                        MedalCard(medal)
                    }

                    item {
                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }
            }
        }

        //Popup de nivel desbloqueado
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
fun ProfileCard(medalsViewModel: MedalsViewModel) {
    var tapCount by remember { mutableStateOf(0) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize().padding(24.dp),
        ) {

            Box(
                modifier = Modifier
                    .size(120.dp)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color(0xFF667eea),
                                Color(0xFF764ba2)
                            )
                        ),
                        shape = CircleShape
                    )
                    .padding(4.dp)
            ) {
                AsyncImage(
                    model = "https://fiverr-res.cloudinary.com/images/q_auto,f_auto/gigs/165844201/original/c0aee438ce5e30b854ad37a9d90157f53cf3bb52/create-the-profile-cartoon-pictures.png",
                    contentDescription = "Profile picture",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .clickable {
                            tapCount++
                            if (tapCount >= 5) {
                                medalsViewModel.resetAll()
                                tapCount = 0
                            }
                        },
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.txt_name_user),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = stringResource(R.string.txt_email_user),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
fun StatsSection(medals: List<com.roque.domain.model.Medal>) {
    val totalMedals = medals.size
    val completedMedals = medals.count { it.isMaxLevel }
    val progressPercentage = if (totalMedals > 0) (completedMedals * 100) / totalMedals else 0

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatItem(
                title = "Total",
                value = totalMedals.toString(),
                icon = Icons.Default.EmojiEvents,
                color = Color(0xFF2196F3)
            )

            StatItem(
                title = "Completadas",
                value = completedMedals.toString(),
                icon = Icons.Default.EmojiEvents,
                color = Color(0xFF4CAF50)
            )

            StatItem(
                title = "Progreso",
                value = "$progressPercentage%",
                icon = Icons.Default.EmojiEvents,
                color = Color(0xFFFF9800)
            )
        }
    }
}

@Composable
fun StatItem(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(
                    color = color.copy(alpha = 0.1f),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = value,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onSurface
        )

        Text(
            text = title,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    }
}


