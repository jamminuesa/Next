package com.juegosdemesa.saltaconejo.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cards")
data class Card(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val text: String,
    val category: Category,
    val difficulty: Int, // A value from 1 to 5
    val tags: List<String> = listOf()
){
    enum class Category(val value: Int) {
        COUNTRY (1),
        PEOPLE(2),
        BRANDS (3),
        SPORTS(4),
        MUSIC(5),
        ACTIONS(6)
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