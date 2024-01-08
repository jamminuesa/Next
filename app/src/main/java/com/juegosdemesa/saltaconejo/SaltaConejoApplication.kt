package com.juegosdemesa.saltaconejo

import android.app.Application
import com.juegosdemesa.saltaconejo.di.AppContainer
import com.juegosdemesa.saltaconejo.di.AppDataContainer
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

//@HiltAndroidApp
class SaltaConejoApplication: Application() {
    val applicationScope = CoroutineScope(SupervisorJob())

    /**
     * AppContainer instance used by the rest of classes to obtain dependencies
     */
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}