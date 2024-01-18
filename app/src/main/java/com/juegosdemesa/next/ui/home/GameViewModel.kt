package com.juegosdemesa.next.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juegosdemesa.next.data.DatabaseRepository
import com.juegosdemesa.next.data.model.Game
import com.juegosdemesa.next.data.model.Round
import com.juegosdemesa.next.data.model.RoundWithTeam
import com.juegosdemesa.next.data.model.Team
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val repository: DatabaseRepository
) : ViewModel() {

    private val _teamList = MutableStateFlow<List<Team>>(mutableListOf())
    val teamList: StateFlow<List<Team>> = _teamList

    private val _roundList = mutableListOf<Round>()

    private val _game = Game()

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
            _roundList.clear()
            _roundList.addAll(rounds)
        }
    }

    fun createNewGame(){
        _roundList.forEachIndexed { index, round ->
            round.order = index
            _game.addRound(round)
        }
        viewModelScope.launch {
            repository.addNewGame(_game)
        }
    }

    val isGameOver: StateFlow<Boolean> =
        repository.getIncompleteRounds(_game)
            .map { it <= 1}
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = false
            )

    val round: StateFlow<Round?> =
        repository.getRoundFromGame(_game)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = null
        )

    val nextRoundTeam: StateFlow<RoundWithTeam> =
        repository.getNextRoundTeam(_game)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = RoundWithTeam(Round(), Team(0))
        )

    fun markRoundAsCompleted(
        score: Int,
        miss: Int
    ){
        viewModelScope.launch {
            val round = round.value
            round?.isRoundComplete = true
            round?.score = score
            round?.miss = miss

            if (round != null){
                repository.markRoundAsComplete(round)
            }

        }
    }

    val gameResult: StateFlow<List<Team>> =
        repository.getRoundsFromGame(_game)
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

    val winner: StateFlow<String> =
        repository.getRoundsFromGame(_game)
        .map { andTheWinnerIs(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = ""
        )

    private fun andTheWinnerIs(round: List<Round>): String{
        return if (round.isNotEmpty()){
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
        } else {
            ""
        }
    }

    fun deleteGame() =
        viewModelScope.launch {
            repository.deleteGame(_game)
        }



    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}