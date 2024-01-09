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
    val tags: List<String> = listOf()
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
        ACTIONS(6, "Accionesw", Utility.hexColorToColor("#84b6f4"))
    }
    
    companion object{
        val CARD_DECK = listOf(
            Card(text = "Donald Trump", category = Category.PEOPLE, difficulty = 1, tags = listOf("América", "Política")),
            Card(text = "Lola Flores", category = Category.PEOPLE, difficulty = 1, tags = listOf("España")),
            Card(text = "Rocio Jurado", category = Category.PEOPLE, difficulty = 1, tags = listOf("España")),
            Card(text = "Torrente", category = Category.PEOPLE, difficulty = 2, tags = listOf("España", "Cine")),
            Card(text = "Mariano Rajoy", category = Category.PEOPLE, difficulty = 1, tags = listOf("España", "Política")),
            Card(text = "Javier Milei", category = Category.PEOPLE, difficulty = 1, tags = listOf("Argentina", "Latinoamérica", "Política")),
            Card(text = "Concha Velasco", category = Category.PEOPLE, difficulty = 1, tags = listOf("España","Cine")),
            Card(text = "Javier Bardem", category = Category.PEOPLE, difficulty = 1, tags = listOf("España","Cine")),
            Card(text = "Penélope Cruz", category = Category.PEOPLE, difficulty = 1, tags = listOf("España","Cine")),
            Card(text = "Pablo Motos", category = Category.PEOPLE, difficulty = 1, tags = listOf("España","Televisión")),
            Card(text = "John Wayne", category = Category.PEOPLE, difficulty = 2, tags = listOf("América")),
        )
    }
}