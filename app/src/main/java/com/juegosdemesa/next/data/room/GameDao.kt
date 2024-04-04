package com.juegosdemesa.next.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.juegosdemesa.next.data.model.Card
import com.juegosdemesa.next.data.model.Round
import com.juegosdemesa.next.data.model.RoundWithTeamAndModifier
import com.juegosdemesa.next.data.model.Team
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeam(team: Team)
    @Insert
    suspend fun insert(round: Round)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<Card>)

    @Update
    suspend fun update(card: Card)

    @Query("DELETE FROM rounds WHERE gameId = :gameId")
    suspend fun deleteAll(gameId: String)

    @Transaction
    @Query("SELECT * FROM rounds WHERE gameId = :gameId")
    fun getAllRoundsFromGame(gameId: String): Flow<List<RoundWithTeamAndModifier>>

    @Transaction
    @Query("SELECT * FROM rounds WHERE gameId = :gameId AND isRoundComplete = 0 ORDER BY `order` LIMIT 1")
    fun getNextRound(gameId: String): Flow<RoundWithTeamAndModifier>

    @Transaction
    @Query("SELECT * FROM rounds WHERE gameId = :gameId AND isRoundComplete = 0 ORDER BY `order` LIMIT 1 OFFSET 1")
    fun getNextRoundWithTeamAndModifier(gameId: String): Flow<RoundWithTeamAndModifier>

    @Query("SELECT count(*) FROM rounds WHERE gameId = :gameId AND isRoundComplete = 0")
    fun getIncompleteRounds(gameId: String): Flow<Int>

    @Query("UPDATE rounds SET isRoundComplete = 1, score = :score, miss = :miss WHERE id = :id")
    suspend fun markRoundAsCompleted(id: String, score: Int, miss: Int)

    @Query("DELETE FROM rounds WHERE gameId = :gameId")
    suspend fun deleteGame(gameId: String)

    @Query("DELETE FROM teams WHERE id = :teamId")
    suspend fun deleteTeam(teamId: Int)
}
