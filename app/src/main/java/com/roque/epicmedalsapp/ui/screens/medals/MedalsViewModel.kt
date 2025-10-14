package com.roque.epicmedalsapp.ui.screens.medals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roque.domain.model.Medal
import com.roque.domain.usecase.GetMedalsFlowUseCase
import com.roque.domain.usecase.ResetAllMedalsUseCase
import com.roque.domain.usecase.SaveMedalsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
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

    val medals: StateFlow<List<Medal>> = getMedalsUseCase()
        .map { it }
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    private var running = false


    fun startEngine() {
        if (running) return
        running = true
        viewModelScope.launch {
            while (running) {
                val updated = medals.value.map { medal ->
                    if (medal.level >= medal.maxLevel || medal.isLocked) return@map medal
                    val inc = Random.nextInt(1, 11)
                    var newPoints = medal.points + inc
                    var newLevel = medal.level
                    if (newPoints >= 100) {
                        newLevel = (medal.level + 1).coerceAtMost(medal.maxLevel)
                        newPoints = 0
                    }
                    medal.copy(level = newLevel, points = newPoints)
                }
                saveMedalsUseCase(updated)
                delay(700L)
            }
        }
    }

    fun stopEngine() { running = false }

    fun resetAll() {
        viewModelScope.launch { resetAllMedalsUseCase() }
    }
}