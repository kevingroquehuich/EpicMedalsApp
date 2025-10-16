package com.roque.epicmedalsapp.ui.screens.medals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roque.domain.config.GameConfig.POINTS_PER_LEVEL
import com.roque.domain.config.GameConfig.UPDATE_INTERVAL_MS
import com.roque.domain.model.Medal
import com.roque.domain.common.UIState
import com.roque.domain.usecase.GetMedalsFlowUseCase
import com.roque.domain.usecase.ResetAllMedalsUseCase
import com.roque.domain.usecase.SaveMedalsUseCase
import com.roque.domain.usecase.UpdateMedalsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MedalsViewModel @Inject constructor(
    private val getMedalsUseCase: GetMedalsFlowUseCase,
    private val resetAllMedalsUseCase: ResetAllMedalsUseCase,
    private val saveMedalsUseCase: SaveMedalsUseCase,
    private val updateMedalsUseCase: UpdateMedalsUseCase
) : ViewModel() {

    private var engineJob: Job? = null
    private val _isEngineRunning = MutableStateFlow(false)
    val isEngineRunning: StateFlow<Boolean> = _isEngineRunning.asStateFlow()

    private val _leveledUpMedal = MutableStateFlow<Medal?>(null)
    val leveledUpMedal: StateFlow<Medal?> = _leveledUpMedal.asStateFlow()

    val medalsState = getMedalsUseCase()
        .map<List<Medal>, UIState<List<Medal>>> { UIState.Success(it) }
        .onStart { emit(UIState.Loading) }
        .catch { e -> emit(UIState.Error("Error loading medals", e)) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, UIState.Loading)

    val medals = medalsState
        .map { (it as? UIState.Success)?.data.orEmpty() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    fun startEngine() {
        if (_isEngineRunning.value) return
        _isEngineRunning.value = true

        engineJob = viewModelScope.launch {
            while (isActive && _isEngineRunning.value) {
                val current = medals.value
                if (current.isEmpty()) {
                    delay(UPDATE_INTERVAL_MS)
                    continue
                }

                val allComplete = current.all { it.level >= it.maxLevel && it.points >= POINTS_PER_LEVEL }
                if (allComplete) {
                    stopEngine()
                    break
                }

                val updated = updateMedalsUseCase(current) { leveledUp ->
                    _leveledUpMedal.value = leveledUp
                }

                if (updated != current) saveMedalsUseCase(updated)
                delay(UPDATE_INTERVAL_MS)
            }
        }
    }

    fun stopEngine() {
        _isEngineRunning.value = false
        engineJob?.cancel()
        engineJob = null
    }

    fun resetAll() {
        viewModelScope.launch {
            stopEngine()
            resetAllMedalsUseCase()
            delay(150)
            startEngine()
        }
    }

    fun clearLeveledUp() {
        _leveledUpMedal.value = null
    }

    override fun onCleared() {
        stopEngine()
        super.onCleared()
    }
}

