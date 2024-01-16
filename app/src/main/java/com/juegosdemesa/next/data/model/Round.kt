package com.juegosdemesa.next.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.util.UUID

@Entity(tableName = "rounds")
data class Round(
    @PrimaryKey
    var id: String = "r_${UUID.randomUUID()}",
    var gameId: String = "",
    var order: Int = 0,
    val team: Team,
    var score: Int = 0,
    var miss: Int = 0,
    val type: Card.Category,
    var isRoundComplete: Boolean = false,
){
    @Ignore
    constructor(): this(
        team = Team(id = 0),
        type = Card.Category.COUNTRY
    )

    companion object{
        fun generateRounds(
            teams: List<Team>
        ): MutableList<Round> {
            var roundCounter = 0
            val mutableList = mutableListOf<Round>()
            DEFAULT_GAME_ROUNDS.forEach {type ->
                teams.forEach { team ->
                val round = Round(order = roundCounter, team = team, type = type)
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

data class RoundWithTeam(
    @Embedded val round: Round,
    @Relation(
        parentColumn = "team",
        entityColumn = "id"
    )
    val team: Team
)