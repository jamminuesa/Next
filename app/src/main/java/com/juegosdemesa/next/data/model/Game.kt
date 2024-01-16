package com.juegosdemesa.next.data.model


import java.util.UUID

data class Game(
    val id: String = UUID.randomUUID().toString(),
    val rounds: MutableList<Round> = mutableListOf()
){
    fun addRound(round: Round){
        round.gameId = id
        rounds.add(round)
    }
}
