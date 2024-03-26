package com.juegosdemesa.next.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "modifiers")
data class RoundModifier(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val text: String,
    val categories: List<Card.Category>,
    val timesDisplayed: Int = 0
)