package com.juegosdemesa.next.di

import android.content.Context
import androidx.room.Room
import com.juegosdemesa.next.data.room.GameDatabase
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
    fun provideGameDao(database: GameDatabase) = database.gameDao()

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext appContext: Context
    ): GameDatabase {
        return Room.databaseBuilder(appContext, GameDatabase::class.java,
                GameDatabase.DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .createFromAsset("next.db")
                .build()
    }
}
