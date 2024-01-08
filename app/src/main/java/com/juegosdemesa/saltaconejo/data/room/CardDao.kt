package com.juegosdemesa.saltaconejo.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.juegosdemesa.saltaconejo.data.Card
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

    @Query("SELECT * from cards ORDER BY RANDOM() LIMIT 10")
    fun getAllCards(): Flow<List<Card>>
}
