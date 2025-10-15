package com.roque.epicmedalsapp.ui.screens.medals

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.roque.epicmedalsapp.R
import com.roque.domain.model.Medal
import kotlinx.coroutines.awaitCancellation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedalsScreen(
    medalsViewModel: MedalsViewModel
) {

    val lifecycleOwner = LocalLifecycleOwner.current
    val medals by medalsViewModel.medals.collectAsState()
    var tapCount by remember { mutableStateOf(0) }

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


    Scaffold(topBar = { TopAppBar(title = { Text("Perfil de Usuario") }) }) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp), verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_launcher_foreground),
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(72.dp)
                        .clickable {
                            tapCount += 1
                            if (tapCount >= 5) {
                                medalsViewModel.resetAll()
                                tapCount = 0
                            }
                        },
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text("Usuario Ejemplo", style = MaterialTheme.typography.labelSmall)
            }


            Text(
                "Medallas",
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.labelSmall
            )
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(medals) { medal -> MedalCard(medal) }
            }
        }
    }
}

@Composable
fun MedalCard(medal: Medal) {
    val progress = remember(medal.points, medal.maxLevel) {
        if (medal.id == "m10") {
            (medal.points.toFloat() / medal.maxLevel.toFloat()).coerceIn(0f, 1f)
        } else {
            (medal.points.toFloat() / 100f).coerceIn(0f, 1f)
        }
    }

    val bgColor = try {
        Color(medal.backgroundColor.toColorInt())
    } catch (e: Exception) {
        Color.White
    }
    val progressColor = try {
        Color(medal.progressColor.toColorInt())
    } catch (e: Exception) {
        Color.Blue
    }

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 1000, easing = LinearOutSlowInEasing),
        label = "progressAnimation"
    )


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .background(bgColor)
                .padding(12.dp)
        ) {
            Text(medal.name, fontWeight = FontWeight.Bold)
            Text(medal.description, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(6.dp))
            LinearProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
                    .clip(RoundedCornerShape(50)),
                color = progressColor,
                trackColor = ProgressIndicatorDefaults.linearTrackColor,
                strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = if (medal.isLocked) "Bloqueada"
                else "${medal.level}/${medal.maxLevel}",
                style = MaterialTheme.typography.labelSmall,
                color = if (medal.isLocked) Color.Gray else Color.Black
            )
            Text("Recompensa: ${medal.reward}", style = MaterialTheme.typography.labelSmall)
            Text("Rareza: ${medal.rarity}", style = MaterialTheme.typography.labelSmall)
        }
    }
}