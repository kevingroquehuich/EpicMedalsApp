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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class MedalsViewModel @Inject constructor(
    private val getMedalsUseCase: GetMedalsFlowUseCase,
    private val resetAllMedalsUseCase: ResetAllMedalsUseCase,
    private val saveMedalsUseCase: SaveMedalsUseCase
) : ViewModel() {

    private var engineJob: Job? = null
    private var running = false
    private val isResetting = AtomicBoolean(false)


    private val updateIntervalMs = 10_000L
    private val minIncrement = 1
    private val maxIncrement = 20
    private val pointsPerLevel = 100

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

                if (isResetting.get()) {
                    delay(300)
                    continue
                }

                val current = medals.value
                if (current.isEmpty()) {
                    delay(updateIntervalMs)
                    continue
                }

                val allComplete = current.all { it.level >= it.maxLevel && it.points >= pointsPerLevel }
                if (allComplete) {
                    stopEngine()
                    break
                }

                //Solo guarda si cambió algo
                val updated = updateMedals(current)
                if (updated != current) {
                    saveMedalsUseCase(updated)
                }

                delay(updateIntervalMs)

            }
        }
    }

    private fun updateMedals(currentMedals: List<Medal>): List<Medal> {
        if (currentMedals.isEmpty()) return currentMedals

        val lastIndex = currentMedals.lastIndex
        val normalMedals = currentMedals.dropLast(1)

        //Actualizar medallas normales
        val updatedNormal = normalMedals.map { medal ->
            if (!medal.isLocked && medal.level < medal.maxLevel) {
                val inc = Random.nextInt(minIncrement, maxIncrement + 1)
                var newPoints = medal.points + inc
                var newLevel = medal.level

                if (newPoints >= pointsPerLevel) {
                    if (medal.level + 1 >= medal.maxLevel) {
                        newLevel = medal.maxLevel
                        newPoints = pointsPerLevel
                    } else {
                        newLevel = medal.level + 1
                        newPoints = 0
                        _leveledUpMedal.value = medal.copy(level = newLevel)
                    }
                }
                medal.copy(level = newLevel, points = newPoints)
            } else medal
        }

        //Última medalla
        val lastMedal = currentMedals[lastIndex]
        val completedCount = updatedNormal.count { it.level >= it.maxLevel }
        val dynamicMaxLevel = updatedNormal.size
        val isUnlocked = completedCount > 0 || !lastMedal.isLocked
        val newLevel = if (isUnlocked) {
            completedCount.coerceAtMost(dynamicMaxLevel)
        } else {
            lastMedal.level
        }

        var updatedLast = lastMedal.copy(
            isLocked = !isUnlocked,
            points = if (isUnlocked) newLevel else 0,
            level = newLevel,
            maxLevel = dynamicMaxLevel
        )

        if (updatedLast.level >= updatedLast.maxLevel && !updatedLast.hasLeveledUp && updatedLast.maxLevel > 0) {
            _leveledUpMedal.value = updatedLast.copy(hasLeveledUp = true)
            updatedLast = updatedLast.copy(hasLeveledUp = true)
        }

        return updatedNormal + updatedLast
    }

    fun stopEngine() {
        if (!running) return
        running = false
        engineJob?.cancel()
        engineJob = null
    }

    fun resetAll() {
        viewModelScope.launch {
            isResetting.set(true)
            stopEngine()

            resetAllMedalsUseCase()
            delay(150)

            isResetting.set(false)
            startEngine()
        }
    }
}
