package com.juegosdemesa.saltaconejo.data

import com.juegosdemesa.saltaconejo.data.model.Card
import com.juegosdemesa.saltaconejo.data.room.CardDao
import javax.inject.Inject

class DatabaseRepository @Inject constructor(private val cardDao: CardDao) {
    fun getCardsByType(cardType: Card.Category) = cardDao.getAllByTypeCards(cardType.value)

    suspend fun markCardAsRecentlyDisplayed(cards: List<Card>) {
        cards.forEach { cardDao.markAsRecentlyDisplayed(it.id) }
    }
    suspend fun markAllCardsAsNotRecentlyDisplay()  = cardDao.markAllAsNotRecentlyDisplay()
}