package com.juegosdemesa.next.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juegosdemesa.next.data.DatabaseRepository
import com.juegosdemesa.next.data.model.Card
import com.juegosdemesa.next.util.info
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CardViewModel @Inject constructor(
    private val repository: DatabaseRepository
): ViewModel() {

    private val _cardListState = MutableStateFlow<List<Card>>(listOf())
    val cardListState: StateFlow<List<Card>> = _cardListState

    fun loadCardsCategory(category: Card.Category) {
        viewModelScope.launch {
            val cards = repository.getCardsByType(category)
            val modifiedList = cards.toMutableList()
            modifiedList.add(0, Card(category.description))

            _cardListState.value = modifiedList
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

    fun addSeenCard(card: Card){
        //cardsSeen.add(card)
        viewModelScope.launch {
            info("Card ${card.text} has been seen, add +1 view")
            repository.increaseTimesSeenCard(card)
        }
    }
}