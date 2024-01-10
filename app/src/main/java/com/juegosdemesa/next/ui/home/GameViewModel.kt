package com.juegosdemesa.next.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juegosdemesa.next.data.model.Round
import com.juegosdemesa.next.data.model.Team
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GameViewModel : ViewModel() {

    private val _teamList = MutableStateFlow<List<Team>>(mutableListOf())
    val teamList: StateFlow<List<Team>> = _teamList

    private val _roundList = MutableStateFlow<List<Round>>(mutableListOf())
//    val roundList: StateFlow<List<Round>> = _roundList

    fun addNewAutoGenerateTeam(){
        viewModelScope.launch {
            val temp = _teamList.value.toMutableList()
            val id = temp.size + 1
            temp.add(Team(id))
            autoGenerateRounds(temp)
            _teamList.emit(temp)
        }
    }

    private val _simpleRoundList = MutableStateFlow<List<Round>>(mutableListOf())
    val simpleRoundList: StateFlow<List<Round>> = _simpleRoundList

    private fun autoGenerateRounds(teams: List<Team>){
        viewModelScope.launch {
            val simpleRounds = Round.generateRounds(listOf(teams[0]))
            val rounds = Round.generateRounds(teams)
            _simpleRoundList.emit(simpleRounds)
            _roundList.emit(rounds)

        }
    }

    private val _isGameOver = MutableStateFlow(false)
    val isGameOver: StateFlow<Boolean> = _isGameOver


//    @OptIn(ExperimentalCoroutinesApi::class)
//    val nextRound: StateFlow<Round?> = _roundList
//        .map {
//             getNextRound(it)
//        }.stateIn(
//            scope = viewModelScope,
//            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
//            initialValue = Round(0)
//        )



    private val _nextRound = MutableStateFlow(getNextRound(_roundList.value))
    val nextRound: StateFlow<Round> = _nextRound

    private fun getNextRound(list: List<Round>): Round{
        val round = list.firstOrNull { !it.isRoundComplete }
        return if (round == null && list.isNotEmpty()){
            gameIsOver()
            Round(0)
        } else if (round != null) {
            round
        } else {
            Round(0) // Dummy
        }
    }

    private fun gameIsOver(){
        viewModelScope.launch {
            _isGameOver.emit(true)
        }
    }

    fun markRoundAsCompleted(round: Round){
        viewModelScope.launch {
            val list = _roundList.value.toMutableList()
            val newRound = list[round.id]
            newRound.score = round.score
            newRound.miss = round.miss
            newRound.isRoundComplete = true
            list[round.id] = newRound
            _roundList.emit(list)
            _nextRound.emit(getNextRound(list))
        }
    }
}