package com.juegosdemesa.saltaconejo.data.model

import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.juegosdemesa.saltaconejo.util.Utility

@Entity(tableName = "cards")
data class Card(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val text: String,
    val category: Category,
    val difficulty: Int, // A value from 1 to 5
    val points: Int = 1,
    val tags: List<String> = listOf(),
    val recentlyDisplayed: Boolean = false
){
    @Ignore
    constructor(text: String): this (
        id = 0,
        text = text,
        category = Category.COUNTRY,
        difficulty = 0,
        points = 0,
    )
    enum class Category(
        val value: Int,
        val text: String,
        val color: Color) {
        COVER(0, "", Color.White),
        COUNTRY(1, "País", Utility.hexColorToColor("#77dd77")),
        PEOPLE(2, "Personajes", Utility.hexColorToColor("#77dd77")),
        BRANDS(3, "Marcas", Utility.hexColorToColor("#77dd77")),
        SPORTS(4, "Deportes", Utility.hexColorToColor("#84b6f4")),
        MUSIC(5, "Música", Utility.hexColorToColor("#84b6f4")),
        ACTIONS(6, "Acciones", Utility.hexColorToColor("#84b6f4"))
    }
}