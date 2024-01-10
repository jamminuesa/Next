package com.juegosdemesa.next.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juegosdemesa.next.data.DatabaseRepository
import com.juegosdemesa.next.data.model.Card
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CardViewModel @Inject constructor(
    private val repository: DatabaseRepository
): ViewModel() {
    private val cardCategory = MutableStateFlow(Card.Category.COVER)

    @OptIn(ExperimentalCoroutinesApi::class)
    val cardUiState: StateFlow<CardsUiState> = cardCategory
        .flatMapLatest { type ->
            repository.getCardsByType(type)
                .map {// Add a cover
                    val modifiedList = it.toMutableList()
                    modifiedList.add(0, Card(categoryToText(type)))
                    CardsUiState(modifiedList)
                }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = CardsUiState()
        )

    fun setCardCategory(category: Card.Category){
        viewModelScope.launch {
            cardCategory.emit(category)
        }
    }

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


    val cardSeen: MutableList<Card> = mutableListOf()

    fun addSeenCard(card: Card){
        cardSeen.add(card)
    }

    fun markSeenCardsAsRecentlyDisplay() = viewModelScope.launch {
        repository.markCardAsRecentlyDisplayed(cardSeen)
    }

    fun resetCardDisplay() = viewModelScope.launch {
        repository.markAllCardsAsNotRecentlyDisplay()
    }

    private fun categoryToText(type: Card.Category): String{
        return when(type){
            Card.Category.COUNTRY -> "Describe con palabras estas cosas relacionadas con tu país"
            Card.Category.PEOPLE -> "Describe a las siguientes personas y personajes"
            Card.Category.BRANDS -> "Describe las marcas"
            Card.Category.SPORTS -> "Representa con mímica estas acciones relacionadas con el deporte"
            Card.Category.MUSIC -> "Representa con mímica estas acciones relacionadas con la música"
            Card.Category.ACTIONS -> "Mediante mímica representa estas acciones"
            else -> "Dale a ✓ para comenzar"
        }
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}



data class CardsUiState(val itemList: List<Card> = listOf())