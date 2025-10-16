package com.roque.epicmedalsapp.ui.screens.album

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roque.domain.common.UIState
import com.roque.domain.model.AlbumItem
import com.roque.domain.usecase.album.GetAlbumItemsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel @Inject constructor(
    private val getAlbumItemsUseCase: GetAlbumItemsUseCase
): ViewModel() {

    init {
        Log.d("AlbumViewModel", "AlbumViewModel initialized")
    }

    val itemsState = getAlbumItemsUseCase()
        .map<List<AlbumItem>, UIState<List<AlbumItem>>> { items ->
            Log.d("AlbumViewModel", "Mapping ${items.size} items to Success state")
            UIState.Success(items)
        }
        .onStart {
            Log.d("AlbumViewModel", "Starting flow - emitting Loading state")
            emit(UIState.Loading)
        }
        .catch { e ->
            Log.e("AlbumViewModel", "Error in flow", e)
            emit(UIState.Error("Error loading album items: ${e.message}", e))
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, UIState.Loading)

    val items = itemsState
        .map { state ->
            when (state) {
                is UIState.Success -> {
                    Log.d(
                        "AlbumViewModel",
                        "Extracting ${state.data.size} items from Success state"
                    )
                    state.data
                }

                else -> {
                    Log.d("AlbumViewModel", "State is not Success, returning empty list")
                    emptyList()
                }
            }
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

}