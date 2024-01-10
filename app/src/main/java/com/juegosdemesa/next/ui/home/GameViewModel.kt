package com.juegosdemesa.next.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juegosdemesa.next.data.model.Round
import com.juegosdemesa.next.data.model.Team
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class GameViewModel : ViewModel() {

    private val _teamList = MutableStateFlow<List<Team>>(mutableListOf())
    val teamList: StateFlow<List<Team>> = _teamList

    private val _roundList = MutableStateFlow<List<Round>>(mutableListOf())

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

    val round: StateFlow<Round> = _roundList
        .map { getRound(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = Round(0)
        )

    private fun getRound(list: List<Round>): Round{
        val round = list.firstOrNull { !it.isRoundComplete }
        return if (round == null && list.isNotEmpty()){
            gameIsOver()
            Round(0)
        } else round ?: Round(0) // Dummy
    }

    val nextRoundTeam: StateFlow<Team?> = _roundList
        .map { getNextRoundTeam(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = Team(0)
        )

    private fun getNextRoundTeam(list: List<Round>): Team?{
        // Get the name of the Team in the next round
        return try {
            val round = list.firstOrNull { !it.isRoundComplete }
            val index = list.indexOf(round)
            list[index + 1].team
        } catch (e: IndexOutOfBoundsException){
            null
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
            //_round.emit(getRound(list))
        }
    }

    val gameResult: StateFlow<List<Team>> = _roundList
        .map { calculateTotalResults(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = listOf()
        )

    private fun calculateTotalResults(round: List<Round>): List<Team>{
        val list = round.map { it.team }.distinctBy { it.id }
        list.forEach { team ->
            val filter = round
                .filter { it.team.id == team.id }
            team.totalScore = filter
                .sumOf { it.score }
            team.totalMiss = filter
                .sumOf { it.miss }
        }
        return list
    }

    val winner: StateFlow<String> = _roundList
        .map { andTheWinnerIs(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = ""
        )

    private fun andTheWinnerIs(round: List<Round>): String{
        val list = calculateTotalResults(round)
        val maximumScore = list.maxBy { it.totalScore }.totalScore
        val numberOfWinners = list.filter { it.totalScore == maximumScore }
        return if (numberOfWinners.size == 1) {
            "El ganador es: ${numberOfWinners[0].name}"
        } else {
            val stringBuilder = StringBuilder ("Ha habido un empate de ")
            list.forEach {
                stringBuilder.append("${it.name}, ")
            }
            stringBuilder.delete(stringBuilder.length - 2, stringBuilder.length)
            stringBuilder.toString()
        }

    }



    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}