package com.juegosdemesa.next.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.juegosdemesa.next.data.model.Card
import com.juegosdemesa.next.data.model.RoundModifier
import kotlinx.coroutines.flow.Flow

@Dao
interface RoundModifierDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(card: Card)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(list: List<Card>)

    @Update
    suspend fun update(card: Card)

    @Delete fun delete(card: Card)

    @Query("SELECT * from modifiers WHERE id = :id")
    fun getModifier(id: Int): Flow<RoundModifier>

    @Query("SELECT * FROM modifiers WHERE categories LIKE '%' || :type || '%' ORDER BY timesDisplayed ASC, RANDOM() LIMIT 1")
    suspend fun getOneRoundModifier(type: Int): RoundModifier

    @Query("UPDATE modifiers SET timesDisplayed = timesDisplayed + 1 WHERE id = :id")
    suspend fun increaseTimesSeen(id: Int)

}
