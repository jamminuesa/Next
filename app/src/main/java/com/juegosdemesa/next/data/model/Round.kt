package com.juegosdemesa.next.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.util.UUID

@Entity(tableName = "rounds")
data class Round(
    @PrimaryKey
    var id: String = "r_${UUID.randomUUID()}",
    var gameId: String = "",
    var order: Int = 0,
    val teamId: Int = 0,
    var score: Int = 0,
    var miss: Int = 0,
    var modifierId: Int? = null,
    val type: Card.Category = Card.Category.COUNTRY,
    var isRoundComplete: Boolean = false,
){

    companion object{
        fun generateRounds(
            teams: List<Team>
        ): MutableList<RoundWithTeamAndModifier> {
            var roundCounter = 0
            val mutableList = mutableListOf<RoundWithTeamAndModifier>()
            DEFAULT_GAME_ROUNDS.forEach {type ->
                teams.forEach { team ->
                    val round = Round(order = roundCounter, teamId = team.id, type = type)
                    val roundWithTeamAndModifier = RoundWithTeamAndModifier(round, team = team)
                mutableList.add(roundWithTeamAndModifier)
                roundCounter++
                }
            }
            return mutableList
        }

        private val DEFAULT_GAME_ROUNDS = listOf(
//            Card.Category.COUNTRY,
//            Card.Category.ACTIONS,
//            Card.Category.HUM,
//            Card.Category.PEOPLE,
//            Card.Category.SPORTS,
//            Card.Category.IMITATE,
//            Card.Category.BRANDS,
//            Card.Category.MUSIC,
            Card.Category.FINISH_THE_SONG,
        )
    }
}

data class RoundWithTeamAndModifier(
    @Embedded val round: Round,
    @Relation(
        parentColumn = "teamId",
        entityColumn = "id"
    )
    val team: Team,
    @Relation(
        parentColumn = "modifierId",
        entityColumn = "id"
    )
    val modifier: RoundModifier? = null
)