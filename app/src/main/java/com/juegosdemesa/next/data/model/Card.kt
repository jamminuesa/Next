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
        val description: String,
        val helpInfo: String
    ) {
        DEFAULT(0, "", Color.White,"", ""),
        COUNTRY(1, "País", Utility.hexColorToColor("#77dd77"), "Describe: Españita",
            "Describe con palabras las siguientes cosas sobre España, puedes usar cualquier palabra menos la de la tarjeta y aquellas que se deriven de la misma"),
        PEOPLE(2, "Personajes", Utility.hexColorToColor("#77dd77"), "Describe: Personas y personajes",
            "Describe con palabras los siguientes personajes y personas, puedes usar cualquier palabra menos la de la tarjeta y aquellas que se deriven de la misma"),
        BRANDS(3, "Marcas", Utility.hexColorToColor("#77dd77"), "Describe: Marcas",
            "Describe con palabras las siguientes marcas sobre España, puedes usar cualquier palabra menos la de la tarjeta y aquellas que se deriven de la misma"),
        SPORTS(4, "Deportes", Utility.hexColorToColor("#84b6f4"), "Mímica: Deporte",
            "Mediante mímica representa las siguientes cosas/acciones relacionadas con el deporte. Puedes hacer onomatopeyas pero no puedes usar objetos"),
        MUSIC(5, "Música", Utility.hexColorToColor("#84b6f4"), "Mímica: Música",
            "Mediante mímica representa las siguientes cosas/acciones relacionadas con la música. Puedes hacer onomatopeyas pero no puedes usar objetos"),
        ACTIONS(6, "Acciones", Utility.hexColorToColor("#84b6f4"), "Mímica: Acciones",
            "Mediante mímica representa las siguientes acciones del día a día. Puedes hacer onomatopeyas pero no puedes usar objetos"),
        IMITATE(7, "Imitar", Utility.hexColorToColor("#fdfd96"), "Sonido: Imita a personajes",
            "Imita mediante sonidos a las siguientes personas/animales. Si es una persona imita su forma de hablar, no vale hacer gestos, mímica ni describir quien eres. Los animales saben hablar"),
        HUM(8, "Tararear", Utility.hexColorToColor("#fdfd96"), "Sonido: Tararea las canciones",
            "Tararea las canciones, en serio tenías que mirar esto, yo pensaba que aquí había nivel. No vale cantar la letra solo tararear"),
        FINISH_THE_SONG(9, "Termina la canción", Utility.hexColorToColor("#fdfd96"), "Termina la canción",
            "A continuación tendrás letras de canciones, puedes leer con el ritmo de la música hasta los puntos suspensivos, a partir de ahí tu equipo tendrá que decir las palabras exactas"),
    }
}