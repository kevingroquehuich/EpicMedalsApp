package com.roque.epicmedalsapp.ui.screens.missions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roque.domain.common.UIState
import com.roque.domain.model.Mission
import com.roque.domain.usecase.missions.GetMissionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class MissionsViewModel @Inject constructor(
    private val getMissionsUseCase: GetMissionsUseCase
) : ViewModel() {

    val missionsState = getMissionsUseCase()
        .map<List<Mission>, UIState<List<Mission>>> { UIState.Success(it) }
        .onStart { emit(UIState.Loading) }
        .catch { e -> emit(UIState.Error("Error loading missions", e)) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, UIState.Loading)

    val missions = missionsState
        .map { (it as? UIState.Success)?.data ?: emptyList() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

}