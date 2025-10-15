package com.roque.epicmedalsapp.ui.screens.medals

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roque.domain.model.Medal
import com.roque.domain.usecase.GetMedalsFlowUseCase
import com.roque.domain.usecase.ResetAllMedalsUseCase
import com.roque.domain.usecase.SaveMedalsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class MedalsViewModel @Inject constructor(
    private val getMedalsUseCase: GetMedalsFlowUseCase,
    private val resetAllMedalsUseCase: ResetAllMedalsUseCase,
    private val saveMedalsUseCase: SaveMedalsUseCase
): ViewModel() {

    private var engineJob: Job? = null
    private var running = false
    private val updateIntervalMs = 1000L
    private val minIncrement = 1
    private val maxIncrement = 10
    private val pointsPerLevel = 100

    val medals = getMedalsUseCase()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    fun startEngine() {
        if (running) return
        running = true

        engineJob = viewModelScope.launch {
            while (running) {
                val current = medals.value
                if (current.isNotEmpty()) {
                    val updated = current.map { medal ->
                        if (medal.level >= medal.maxLevel || medal.isLocked) medal
                        else {
                            val inc = Random.nextInt(minIncrement, maxIncrement + 1)
                            var newPoints = medal.points + inc
                            var newLevel = medal.level
                            if (newPoints >= pointsPerLevel) {
                                newLevel = (medal.level + 1).coerceAtMost(medal.maxLevel)
                                newPoints = 0
                            }
                            medal.copy(level = newLevel, points = newPoints)
                        }
                    }
                    saveMedalsUseCase(updated)
                }
                delay(updateIntervalMs)
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
        viewModelScope.launch { resetAllMedalsUseCase() }
    }
}