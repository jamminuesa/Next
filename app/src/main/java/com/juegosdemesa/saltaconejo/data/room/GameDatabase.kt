package com.juegosdemesa.saltaconejo.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.juegosdemesa.saltaconejo.data.Card
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(
    entities = [Card::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class GameDatabase: RoomDatabase() {
    abstract fun cardDao(): CardDao


    class ExpensesDatabaseCallback @Inject constructor (
        private val database: Provider<GameDatabase>,
        private val applicationScope: CoroutineScope
    ) : Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            applicationScope.launch {
                prePopulateDatabase(database.get())
            }
        }

        private suspend fun prePopulateDatabase( db: GameDatabase) {
            val cardDao = db.cardDao()
            cardDao.insertAll(Card.CARD_DECK)
        }
    }

    class ExpensesDatabaseCallbackLegacy @Inject constructor (
        private val scope: CoroutineScope
    ) : Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)


            Instance?.let {
                scope.launch {
                    prePopulateDatabase(it)
                }
            }
        }

        private suspend fun prePopulateDatabase( db: GameDatabase) {
            val cardDao = db.cardDao()
            Card.CARD_DECK.forEach {
                cardDao.insert(it)
            }
        }
    }

    companion object{
        const val DATABASE_NAME = "salta_conejo.db"

        @Volatile
        private var Instance: GameDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): GameDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, GameDatabase::class.java, DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .addCallback(ExpensesDatabaseCallbackLegacy(scope))
                    .build()
                    .also { Instance = it }
            }
        }
    }
}