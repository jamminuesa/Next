package com.juegosdemesa.saltaconejo.di

import android.content.Context
import androidx.room.Room
import com.juegosdemesa.saltaconejo.data.room.GameDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {
    @Provides
    fun provideCardDao(database: GameDatabase) = database.cardDao()

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext appContext: Context,
        callback: GameDatabase.ExpensesDatabaseCallback
    ): GameDatabase {

        val builder =
            Room.databaseBuilder(appContext, GameDatabase::class.java,
                GameDatabase.DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .addCallback(callback)

        return builder.build()
    }
}
