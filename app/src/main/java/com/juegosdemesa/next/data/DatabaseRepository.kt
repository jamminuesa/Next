package com.juegosdemesa.next.data

import com.juegosdemesa.next.data.model.Card
import com.juegosdemesa.next.data.model.Game
import com.juegosdemesa.next.data.model.Round
import com.juegosdemesa.next.data.room.CardDao
import com.juegosdemesa.next.data.room.GameDao
import javax.inject.Inject

class DatabaseRepository @Inject constructor(
    private val cardDao: CardDao,
    private val gameDao: GameDao
    ) {
    fun getCardsByType(cardType: Card.Category) = cardDao.getAllByTypeCards(cardType.value)

    suspend fun markCardAsRecentlyDisplayed(cards: List<Card>) {
        cards.forEach { cardDao.markAsRecentlyDisplayed(it.id) }
    }
    suspend fun markAllCardsAsNotRecentlyDisplay()  = cardDao.markAllAsNotRecentlyDisplay()


    fun getRoundFromGame(game: Game) =
        gameDao.getNextRound(game.id)

    fun getNextRoundTeam(game: Game) =
        gameDao.getNextTeamRound(game.id)

    fun getRoundsFromGame(game: Game) =
        gameDao.getAllRoundsFromGame(game.id)

    fun getIncompleteRounds(game: Game) =
        gameDao.getIncompleteRounds(game.id)
    suspend fun addNewGame(game: Game){
        val teams = game.rounds.distinctBy { it.team.name }.map { it.team }
        teams.forEach {
            gameDao.insertTeam(it)
        }
        game.rounds.forEach {
            gameDao.insert(it)
        }
    }

    suspend fun deleteGame(game: Game){
        val teams = game.rounds.distinctBy { it.team.name }.map { it.team }
        teams.forEach {
            gameDao.deleteTeam(it.id)
        }
        gameDao.deleteGame(game.id)
    }

    suspend fun markRoundAsComplete(round: Round) =
        gameDao.markRoundAsCompleted(round.id, round.score, round.miss)
}