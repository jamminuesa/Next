package com.juegosdemesa.saltaconejo.data

import com.juegosdemesa.saltaconejo.data.model.Card
import com.juegosdemesa.saltaconejo.data.room.CardDao
import javax.inject.Inject

class DatabaseRepository @Inject constructor(private val cardDao: CardDao) {
    fun getCardsByType(cardType: Card.Category) = cardDao.getAllByTypeCards(cardType.value)
}