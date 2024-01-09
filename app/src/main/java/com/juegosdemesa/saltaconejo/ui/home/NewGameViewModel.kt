package com.juegosdemesa.saltaconejo.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juegosdemesa.saltaconejo.data.model.Round
import com.juegosdemesa.saltaconejo.data.model.Team
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NewGameViewModel : ViewModel() {

    private val _teamList = MutableStateFlow<List<Team>>(mutableListOf())
    val teamList: StateFlow<List<Team>> = _teamList

    fun addNewAutoGenerateTeam(){
        viewModelScope.launch {
            val temp = _teamList.value.toMutableList()
            val id = temp.size + 1
            temp.add(Team(id))
            autoGenerateRounds(temp)
            _teamList.emit(temp)
        }
    }

    private val _roundList = MutableStateFlow<List<Round>>(mutableListOf())
    val roundList: StateFlow<List<Round>> = _roundList

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
}