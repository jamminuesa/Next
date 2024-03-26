package com.juegosdemesa.next.data.model


import java.util.UUID

data class Game(
    val id: String = UUID.randomUUID().toString(),
    val rounds: MutableList<RoundWithTeamAndModifier> = mutableListOf()
){
    fun addRound(round: RoundWithTeamAndModifier){
        round.round.gameId = id
        rounds.add(round)
    }

    fun reset(){
        rounds.clear()
    }
}
