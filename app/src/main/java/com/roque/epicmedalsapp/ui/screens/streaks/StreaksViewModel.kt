package com.roque.epicmedalsapp.ui.screens.streaks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roque.domain.common.UIState
import com.roque.domain.model.Streak
import com.roque.domain.usecase.GetStreakFlowUseCase
import com.roque.domain.usecase.RecordDailyActivityUseCase
import com.roque.domain.usecase.UpdateStreakMedalsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class StreaksViewModel @Inject constructor(
    private val getStreakFlowUseCase: GetStreakFlowUseCase,
    private val recordDailyActivityUseCase: RecordDailyActivityUseCase,
    private val updateStreakMedalsUseCase: UpdateStreakMedalsUseCase
) : ViewModel() {

    private val _isRecordingActivity = MutableStateFlow(false)
    val isRecordingActivity: StateFlow<Boolean> = _isRecordingActivity.asStateFlow()

    val streakState = getStreakFlowUseCase()
        .map<Streak, UIState<Streak>> { UIState.Success(it) }
        .onStart { emit(UIState.Loading) }
        .catch { e -> emit(UIState.Error("Error loading streak", e)) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, UIState.Loading)

    val streak = streakState
        .map { (it as? UIState.Success)?.data ?: Streak() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, Streak())

    fun recordDailyActivity() {
        if (_isRecordingActivity.value) return

        viewModelScope.launch {
            try {
                _isRecordingActivity.value = true
                recordDailyActivityUseCase()
                // Actualizar medallas basadas en la nueva racha
                updateStreakMedalsUseCase()
            } catch (_: Exception) {

            } finally {
                _isRecordingActivity.value = false
            }
        }
    }

    fun getWeeklyProgress(): List<DayStatus> {
        val currentStreak = streak.value
        val today = LocalDate.now()
        val weekDays = mutableListOf<DayStatus>()

        // Generar los últimos 7 días
        for (i in 6 downTo 0) {
            val date = today.minusDays(i.toLong())
            val dateString = date.format(DateTimeFormatter.ISO_LOCAL_DATE)
            val isActive = currentStreak.streakHistory.contains(dateString)
            val isToday = i == 0

            weekDays.add(
                DayStatus(
                    dayName = date.dayOfWeek.name.take(1), // S, M, T, W, T, F, S
                    isActive = isActive,
                    isToday = isToday,
                    date = date
                )
            )
        }

        return weekDays
    }

    init {
        // Registrar actividad automáticamente al inicializar el ViewModel
        recordDailyActivity()
    }
}

data class DayStatus(
    val dayName: String,
    val isActive: Boolean,
    val isToday: Boolean,
    val date: LocalDate
)