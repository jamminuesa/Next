package com.juegosdemesa.next.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.juegosdemesa.next.data.model.Card
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

    @Query("SELECT * FROM cards WHERE category = :type ORDER BY timesDisplayed ASC, RANDOM()")
    fun getAllByTypeCards(type: Int): Flow<List<Card>>

    @Query("UPDATE cards SET timesDisplayed = timesDisplayed + 1 WHERE id IN (:ids)")
    suspend fun increaseTimesSeen(ids: List<Int>)
}
