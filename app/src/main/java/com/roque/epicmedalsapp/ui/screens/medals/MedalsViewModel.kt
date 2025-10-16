package com.roque.epicmedalsapp.ui.screens.medals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roque.domain.config.GameConfig.POINTS_PER_LEVEL
import com.roque.domain.config.GameConfig.UPDATE_INTERVAL_MS
import com.roque.domain.model.Medal
import com.roque.domain.usecase.GetMedalsFlowUseCase
import com.roque.domain.usecase.ResetAllMedalsUseCase
import com.roque.domain.usecase.SaveMedalsUseCase
import com.roque.epicmedalsapp.domain.usecase.UpdateMedalsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
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
    private var running = false

    val medals = getMedalsUseCase()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    private val _leveledUpMedal = MutableStateFlow<Medal?>(null)
    val leveledUpMedal: StateFlow<Medal?> = _leveledUpMedal

    fun clearLeveledUp() {
        _leveledUpMedal.value = null
    }

    fun startEngine() {
        if (running) return
        running = true

        engineJob = viewModelScope.launch {
            while (running) {

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

                //Solo guarda si cambió algo
                val updated = updateMedalsUseCase(current) { leveledUp ->
                    _leveledUpMedal.value = leveledUp
                }

                if (updated != current) {
                    saveMedalsUseCase(updated)
                }

                delay(UPDATE_INTERVAL_MS)

            }
        }
    }

    fun stopEngine() {
        if (!running) return
        running = false
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
}
