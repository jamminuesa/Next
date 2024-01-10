package com.juegosdemesa.next.di

import android.content.Context
import com.juegosdemesa.next.NextApplication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Provides
    fun provideContext(@ApplicationContext appContext: Context) = appContext

    @Provides
    @Singleton
    fun provideExternalScope(): CoroutineScope = NextApplication().applicationScope
}