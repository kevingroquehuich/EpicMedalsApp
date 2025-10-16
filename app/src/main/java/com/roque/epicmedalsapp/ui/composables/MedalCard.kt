package com.roque.epicmedalsapp.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import com.roque.domain.model.Medal
import com.roque.epicmedalsapp.R

@Composable
fun MedalCard(medal: Medal) {
    val progress = remember(medal.points, medal.maxLevel) {
        if (medal.id == "m10") {
            (medal.points.toFloat() / medal.maxLevel.toFloat()).coerceIn(0f, 1f)
        } else {
            (medal.points.toFloat() / 100f).coerceIn(0f, 1f)
        }
    }

    val bgColor = runCatching { Color(medal.backgroundColor.toColorInt()) }.getOrElse { Color.White }
    val progressColor = runCatching { Color(medal.progressColor.toColorInt()) }.getOrElse { Color.Blue }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(containerColor = bgColor),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    MedalIcon(medal = medal, modifier = Modifier.size(56.dp))
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Lvl. ${medal.level}",
                        style = MaterialTheme.typography.titleLarge,
                        color = if (medal.isLocked) Color.Gray else progressColor,
                        fontWeight = FontWeight.Bold
                    )
                }

                Column(
                    modifier = Modifier
                        .weight(3f)
                        .padding(start = 4.dp)
                        .align(Alignment.CenterVertically)
                ) {
                    Text(
                        text = medal.name,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = medal.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    LabeledLinearProgress(
                        progress = progress,
                        current = medal.points,
                        max = if (medal.id == "m10") 9 else 100,
                        progressColor = progressColor
                    )
                }
            }
        }

        if (medal.isMaxLevel) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = (-15).dp)
                    .clip(RoundedCornerShape(50))
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color(0xFFFF5F6D),
                                Color(0xFFFFC371)
                            )
                        )
                    )
                    .border(2.dp, Color.White, RoundedCornerShape(50))
                    .padding(horizontal = 24.dp, vertical = 4.dp)
            ) {
                Text(
                    text = stringResource(R.string.txt_max_level),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}