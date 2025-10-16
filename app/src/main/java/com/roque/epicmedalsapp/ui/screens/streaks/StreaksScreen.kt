package com.roque.epicmedalsapp.ui.screens.streaks

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.roque.domain.common.UIState
import com.roque.domain.model.Streak
import com.roque.epicmedalsapp.R
import com.roque.epicmedalsapp.ui.animations.LifecycleAwareLottieView
import com.roque.epicmedalsapp.ui.composables.ErrorScreen
import com.roque.epicmedalsapp.ui.composables.LoadingScreen

@Composable
fun StreaksScreen(
    viewModel: StreaksViewModel = hiltViewModel()
) {
    val streakState by viewModel.streakState.collectAsState()
    val streak by viewModel.streak.collectAsState()
    val isRecordingActivity by viewModel.isRecordingActivity.collectAsState()

    when (val state = streakState) {
        is UIState.Loading -> LoadingScreen()
        is UIState.Error -> ErrorScreen(
            message = state.message, onRetry = { viewModel.recordDailyActivity() })

        is UIState.Success -> {
            StreaksContent(
                streak = streak,
                weeklyProgress = viewModel.getWeeklyProgress(),
                isRecordingActivity = isRecordingActivity,
                onRecordActivity = { viewModel.recordDailyActivity() })
        }
    }
}

@Composable
private fun StreaksContent(
    streak: Streak,
    weeklyProgress: List<DayStatus>,
    isRecordingActivity: Boolean,
    onRecordActivity: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {

        StreakCounterSection(streak = streak)

        WeeklyProgressSection(weeklyProgress = weeklyProgress)

        StreakStatsSection(streak = streak)

        RecordActivityButton(
            isRecording = isRecordingActivity, onRecord = onRecordActivity
        )

        GoalsProgressSection(streak = streak)
    }
}

@Composable
private fun StreakCounterSection(streak: Streak) {
    val scale by animateFloatAsState(
        targetValue = if (streak.currentStreak > 0) 1f else 0.8f, animationSpec = tween(600)
    )

    Box(
        modifier = Modifier
            .size(280.dp)
            .graphicsLayer(scaleX = scale, scaleY = scale),
        contentAlignment = Alignment.Center
    ) {
        LifecycleAwareLottieView(
            resId = R.raw.streak, modifier = Modifier.fillMaxSize(), alignment = Alignment.Center
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 120.dp)
        ) {
            Text(
                text = streak.currentStreak.toString(),
                color = Color.White,
                fontSize = 64.sp,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                text = if (streak.currentStreak == 1) stringResource(R.string.txt_day)
                     else stringResource(R.string.txt_days),
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun WeeklyProgressSection(weeklyProgress: List<DayStatus>) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = stringResource(R.string.txt_progress_of_the_week),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(weeklyProgress) { day ->
                DayProgressItem(dayStatus = day)
            }
        }
    }
}

@Composable
private fun DayProgressItem(dayStatus: DayStatus) {
    val backgroundColor by animateColorAsState(
        targetValue = when {
            dayStatus.isActive -> MaterialTheme.colorScheme.primary
            dayStatus.isToday -> MaterialTheme.colorScheme.secondary
            else -> MaterialTheme.colorScheme.surfaceVariant
        }, animationSpec = tween(300)
    )

    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        if (dayStatus.isActive) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = stringResource(R.string.txt_completed),
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        } else {
            Text(
                text = dayStatus.dayName,
                color = if (dayStatus.isToday) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
private fun StreakStatsSection(streak: Streak) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            StatItem(
                label = stringResource(R.string.txt_current_streak),
                value = streak.currentStreak.toString(),
                icon = Icons.Default.LocalFireDepartment
            )
            StatItem(
                label = stringResource(R.string.txt_best_streak),
                value = streak.longestStreak.toString(),
                icon = Icons.Default.LocalFireDepartment
            )
            StatItem(
                label = stringResource(R.string.txt_total_days),
                value = streak.streakHistory.size.toString(),
                icon = Icons.Default.Check
            )
        }
    }
}

@Composable
private fun StatItem(
    label: String,
    value: String,
    icon: ImageVector
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(32.dp)
        )
        Text(
            text = value,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = label,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun RecordActivityButton(
    isRecording: Boolean,
    onRecord: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onRecord() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp), contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (isRecording) stringResource(R.string.txt_registering)
                        else stringResource(R.string.txt_record_activity_of_the_day),
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun GoalsProgressSection(streak: Streak) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(R.string.txt_progress_towards_goals),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        GoalProgressItem(
            label = stringResource(R.string.txt_weekly_goal_seven_days),
            progress = streak.weeklyProgress,
            current = streak.currentStreak,
            target = streak.weeklyGoal
        )

        GoalProgressItem(
            label = stringResource(R.string.txt_main_objective_days, streak.streakGoal),
            progress = streak.progressPercentage,
            current = streak.currentStreak,
            target = streak.streakGoal
        )
    }
}

@Composable
private fun GoalProgressItem(
    label: String, progress: Float, current: Int, target: Int
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "$current/$target",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        LinearProgressIndicator(
            progress = progress,
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
    }
}