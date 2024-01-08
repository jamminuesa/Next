package com.juegosdemesa.saltaconejo.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juegosdemesa.saltaconejo.data.Card
import com.juegosdemesa.saltaconejo.data.DatabaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CardViewModel @Inject constructor(
    repository: DatabaseRepository
): ViewModel() {
    val cardUiState: StateFlow<CardsUiState> =
        repository.getAllItemsStream()
            .map {// Add a cover
                val modifiedList = it.toMutableList()
                modifiedList.add(0,Card("Dale al botón derecho para comenzar"))
                modifiedList
            }
            .map { CardsUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = CardsUiState()
            )

    //region States
    private val _score = MutableStateFlow(0)
    val score: StateFlow<Int> = _score

    private val _miss = MutableStateFlow(0)
    val miss: StateFlow<Int> = _miss

    fun addPointsToScore(points: Int){
        viewModelScope.launch {
            val temp = score.value + points
            _score.emit(temp)
        }
    }

    fun addMissCard(){
        viewModelScope.launch {
            val temp = miss.value + 1
            _miss.emit(temp)
        }
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class CardsUiState(val itemList: List<Card> = listOf())