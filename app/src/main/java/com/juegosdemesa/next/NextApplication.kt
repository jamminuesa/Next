package com.juegosdemesa.next

import android.app.Application
import com.juegosdemesa.next.di.AppContainer
import com.juegosdemesa.next.di.AppDataContainer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

//@HiltAndroidApp
class NextApplication: Application() {
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