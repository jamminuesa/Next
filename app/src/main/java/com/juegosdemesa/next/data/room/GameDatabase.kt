package com.juegosdemesa.next.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.juegosdemesa.next.data.model.Card
import com.juegosdemesa.next.data.model.Round
import com.juegosdemesa.next.data.model.Team

@Database(
    entities = [Card::class, Round::class, Team::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class GameDatabase: RoomDatabase() {
    abstract fun cardDao(): CardDao
    abstract fun gameDao(): GameDao

    companion object{
        const val DATABASE_NAME = "next.db"
    }
}