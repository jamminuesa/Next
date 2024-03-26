package com.juegosdemesa.next.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juegosdemesa.next.data.DatabaseRepository
import com.juegosdemesa.next.data.model.Game
import com.juegosdemesa.next.data.model.Round
import com.juegosdemesa.next.data.model.RoundWithTeamAndModifier
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

    private val _roundList = mutableListOf<RoundWithTeamAndModifier>()

    private val _withModifiedRounds = MutableStateFlow(false)
    val withModifiedRounds: StateFlow<Boolean> = _withModifiedRounds

    private val _game = Game()
    fun isRoundModificationsChecked(modifications: Boolean){
        _withModifiedRounds.value = modifications
    }

    fun addTeam(name: String){
        viewModelScope.launch {
            val temp = _teamList.value.toMutableList()
            val id = temp.size + 1
            temp.add(Team(id, name))
            autoGenerateRounds(temp)
            _teamList.emit(temp)
        }
    }

    private val _simpleRoundList = MutableStateFlow<List<Round>>(mutableListOf())
    val simpleRoundList: StateFlow<List<Round>> = _simpleRoundList

    private fun autoGenerateRounds(teams: List<Team>){
        viewModelScope.launch {
            val rounds = Round.generateRounds(teams)

            _simpleRoundList.emit(rounds.filter { it.team.id == teams[0].id }.map { it.round })
            _roundList.clear()
            _roundList.addAll(rounds)
        }
    }

    fun createNewGame(){
        viewModelScope.launch {
            _roundList.forEachIndexed { index, round ->
                round.round.order = index
                if (withModifiedRounds.value){
                    val mod = repository.getRoundModification(round.round.type.value)
                    round.round.modifierId = mod.id
                    repository.increaseSeenCountRoundModifier(mod.id)
                }
                _game.addRound(round)
            }
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

    val round: StateFlow<RoundWithTeamAndModifier?> =
        repository.getRoundFromGame(_game)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = null
        )

    val nextRoundTeam: StateFlow<RoundWithTeamAndModifier> =
        repository.getNextRoundTeam(_game)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = RoundWithTeamAndModifier(Round(), Team(0))
        )

    fun markRoundAsCompleted(
        score: Int,
        miss: Int
    ){
        viewModelScope.launch {
            val round = round.value
            round?.round?.isRoundComplete = true
            round?.round?.score = score
            round?.round?.miss = miss

            if (round != null){
                repository.markRoundAsComplete(round.round)
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

    private fun calculateTotalResults(round: List<RoundWithTeamAndModifier>): List<Team>{
        val list = round.map { it.team }.distinctBy{ it.id }
        list.forEach { team ->
            val filter = round
                .filter { it.team.id == team.id }
            team.totalScore = filter
                .sumOf { it.round.score }
            team.totalMiss = filter
                .sumOf { it.round.miss }
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

    private fun andTheWinnerIs(round: List<RoundWithTeamAndModifier>): String{
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
            _teamList.emit(mutableListOf())
            _roundList.clear()
            _withModifiedRounds.emit(false)
            _simpleRoundList.emit(mutableListOf())
            _game.reset()
            repository.deleteGame(_game)
        }



    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}