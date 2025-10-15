package com.roque.epicmedalsapp.ui.screens.medals

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
    private val updateIntervalMs = 500L
    private val minIncrement = 1
    private val maxIncrement = 5
    private val pointsPerLevel = 100

    val medals = getMedalsUseCase()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    fun startEngine() {
        if (running) return
        running = true

        engineJob = viewModelScope.launch {
            while (running) {
                val current = medals.value.toMutableList()
                if (current.isNotEmpty()) {
                    val updated = updateMedals(current)
                    saveMedalsUseCase(updated)
                }
                delay(updateIntervalMs)
            }
        }
    }

    private fun updateMedals(medals: MutableList<Medal>): List<Medal> {
        if (medals.isEmpty()) return medals

        val lastIndex = medals.lastIndex
        val normalMedals = medals.dropLast(1)

        //Actualizar progreso normal
        for (i in 0 until lastIndex) {
            val medal = medals[i]
            if (!medal.isLocked && medal.level < medal.maxLevel) {
                val inc = Random.nextInt(minIncrement, maxIncrement + 1)
                var newPoints = medal.points + inc
                var newLevel = medal.level

                if (newPoints >= pointsPerLevel) {
                    if (medal.level + 1 >= medal.maxLevel) {
                        //Mantener lleno si llegó al nivel máximo
                        newLevel = medal.maxLevel
                        newPoints = pointsPerLevel
                    } else {
                        //Avanzar al siguiente nivel normal
                        newLevel = medal.level + 1
                        newPoints = 0
                    }
                }

                medals[i] = medal.copy(level = newLevel, points = newPoints)
            }
        }

        //Calcular progreso de la última medalla
        val completedCount = normalMedals.count { it.level >= it.maxLevel }
        val lastMedal = medals[lastIndex]

        //Desbloquear si al menos una está completa
        val shouldUnlock = completedCount > 0
        val isNowUnlocked = shouldUnlock || !lastMedal.isLocked

        //Calcular dinámicamente el maxProgress real (total de medallas normales)
        val dynamicMaxLevel = normalMedals.size

        val newLevel = if (isNowUnlocked) {
            completedCount.coerceAtMost(dynamicMaxLevel)
        } else 0

        val updatedLast = lastMedal.copy(
            isLocked = !isNowUnlocked,
            points = newLevel,
            level = newLevel,
            maxLevel = dynamicMaxLevel
        )

        medals[lastIndex] = updatedLast

        return medals
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