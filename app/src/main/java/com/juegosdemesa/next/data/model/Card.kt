package com.juegosdemesa.next.data.model

import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.juegosdemesa.next.util.Utility

@Entity(tableName = "cards")
data class Card(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val text: String,
    val category: Category,
    val difficulty: Int, // A value from 1 to 5
    val points: Int = 1,
    val tags: List<String> = listOf(),
    val timesDisplayed: Int = 0
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
        val color: Color,
        val description: String) {
        DEFAULT(0, "", Color.White,""),
        COUNTRY(1, "País", Utility.hexColorToColor("#77dd77"), "Describe: Españita"),
        PEOPLE(2, "Personajes", Utility.hexColorToColor("#77dd77"), "Describe: Personas y personajes"),
        BRANDS(3, "Marcas", Utility.hexColorToColor("#77dd77"), "Describe: Marcas"),
        SPORTS(4, "Deportes", Utility.hexColorToColor("#84b6f4"), "Mímica: Deporte"),
        MUSIC(5, "Música", Utility.hexColorToColor("#84b6f4"), "Mímica: Música"),
        ACTIONS(6, "Acciones", Utility.hexColorToColor("#84b6f4"), "Mímica: Acciones"),
        IMITATE(7, "Imitar", Utility.hexColorToColor("#fdfd96"), "Sonido: Imita a personajes"),
        HUM(8, "Tararear", Utility.hexColorToColor("#fdfd96"), "Sonido: Tararea las canciones"),
        FINISH_THE_SONG(9, "Termina la canción", Utility.hexColorToColor("#fdfd96"), "Termina la canción"),
    }
}