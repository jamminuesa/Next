package com.juegosdemesa.saltaconejo.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.juegosdemesa.saltaconejo.data.model.Card
import kotlinx.coroutines.flow.Flow

@Dao
interface CardDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(card: Card)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(list: List<Card>)

    @Update
    suspend fun update(card: Card)

    @Delete fun delete(card: Card)

    @Query("SELECT * from cards WHERE id = :id")
    fun getCard(id: Int): Flow<Card>

    @Query("SELECT * FROM (SELECT * FROM cards WHERE category = :type AND recentlyDisplayed = 0 ORDER BY RANDOM()) " +
            "UNION ALL " +
            "SELECT * FROM (SELECT * FROM cards WHERE category = :type AND recentlyDisplayed = 1 ORDER BY RANDOM())")
    fun getAllByTypeCards(type: Int): Flow<List<Card>>

    @Query("UPDATE cards SET recentlyDisplayed = 1 WHERE id = :id")
    suspend fun markAsRecentlyDisplayed(id: Int)

    @Query("UPDATE cards SET recentlyDisplayed = 0")
    suspend fun markAllAsNotRecentlyDisplay()
}
