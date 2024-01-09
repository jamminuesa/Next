package com.juegosdemesa.saltaconejo.data.model

import androidx.room.Ignore

data class Round(
    val id: Int,
    val team: Team,
    val score: Int = 0,
    val miss: Int = 0,
    val type: Card.Category
){
    @Ignore
    constructor(id: Int): this(
        id = id,
        team = Team(id = 0),
        type = Card.Category.COUNTRY
    )

    companion object{
        fun generateRounds(teams: List<Team>): MutableList<Round> {
            var roundCounter = 0
            val mutableList = mutableListOf<Round>()
            teams.forEach { team ->
                DEFAULT_GAME_ROUNDS.forEach {type ->
                    val round = Round(id = roundCounter, team = team, type = type)
                    mutableList.add(round)
                    roundCounter++
                }
            }
            return mutableList
        }

        private val DEFAULT_GAME_ROUNDS = listOf(
            Card.Category.COUNTRY,
            Card.Category.ACTIONS,
            Card.Category.PEOPLE,
            Card.Category.SPORTS,
            Card.Category.BRANDS,
            Card.Category.MUSIC,
        )
    }
}