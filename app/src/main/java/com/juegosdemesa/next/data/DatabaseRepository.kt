package com.juegosdemesa.next.data

import com.juegosdemesa.next.data.model.Card
import com.juegosdemesa.next.data.room.CardDao
import javax.inject.Inject

class DatabaseRepository @Inject constructor(private val cardDao: CardDao) {
    fun getCardsByType(cardType: Card.Category) = cardDao.getAllByTypeCards(cardType.value)

    suspend fun markCardAsRecentlyDisplayed(cards: List<Card>) {
        cards.forEach { cardDao.markAsRecentlyDisplayed(it.id) }
    }
    suspend fun markAllCardsAsNotRecentlyDisplay()  = cardDao.markAllAsNotRecentlyDisplay()
}