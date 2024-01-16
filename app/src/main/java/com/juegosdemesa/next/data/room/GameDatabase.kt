package com.juegosdemesa.next.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
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

        @Volatile
        private var Instance: GameDatabase? = null

        fun getDatabase(context: Context): GameDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, GameDatabase::class.java, DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .createFromAsset("next.db")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}