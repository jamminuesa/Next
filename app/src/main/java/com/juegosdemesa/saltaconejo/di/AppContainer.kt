/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.juegosdemesa.saltaconejo.di

import android.content.Context
import com.juegosdemesa.saltaconejo.data.DatabaseRepository
import com.juegosdemesa.saltaconejo.data.room.GameDatabase

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val databaseRepository: DatabaseRepository
}

/**
 * [AppContainer] implementation that provides instance of [DatabaseRepository]
 */
class AppDataContainer(private val context: Context) : AppContainer {
    /**
     * Implementation for [DatabaseRepository]
     */
    override val databaseRepository: DatabaseRepository by lazy {
        DatabaseRepository(GameDatabase.getDatabase(context).cardDao())
    }
}
