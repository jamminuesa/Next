package com.juegosdemesa.saltaconejo.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juegosdemesa.saltaconejo.data.Card
import com.juegosdemesa.saltaconejo.data.DatabaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class CardViewModel @Inject constructor(
    repository: DatabaseRepository
): ViewModel() {
    val cardUiState: StateFlow<CardsUiState> =
        repository.getAllItemsStream().map { CardsUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = CardsUiState()
            )
    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class CardsUiState(val itemList: List<Card> = listOf())