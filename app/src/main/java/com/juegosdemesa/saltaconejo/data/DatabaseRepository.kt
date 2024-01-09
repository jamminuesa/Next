package com.juegosdemesa.saltaconejo.data

import com.juegosdemesa.saltaconejo.data.model.Card
import com.juegosdemesa.saltaconejo.data.room.CardDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DatabaseRepository @Inject constructor(private val cardDao: CardDao) {

    fun getAllItemsStream(): Flow<List<Card>> = cardDao.getAllCards()
}