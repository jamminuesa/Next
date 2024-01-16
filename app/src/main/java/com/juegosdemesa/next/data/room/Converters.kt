package com.juegosdemesa.next.data.room

import androidx.compose.ui.graphics.Color
import androidx.room.TypeConverter
import com.juegosdemesa.next.data.model.Card
import com.juegosdemesa.next.data.model.Team
import com.juegosdemesa.next.util.Utility

/**
 * Created by Juan on 13/6/18.
 */

class Converters {
    private val separator = "; "
    @TypeConverter
    fun toStringList(value: String): List<String> {
        return if (value.isBlank()) listOf()
        else value.split(separator)
    }

    @TypeConverter
    fun toString(list: List<String>): String {
        return if (list.isEmpty()) ""
        else list.joinToString(separator = separator )
    }

    @TypeConverter
    fun toCategory(value: Int) = enumValues<Card.Category>()[value]

    @TypeConverter
    fun fromCategory(value: Card.Category) = value.ordinal

    @TypeConverter
    fun fromTeam(value: Team) = value.id

    @TypeConverter
    fun toTeam(value: Int) = Team(value)


    @TypeConverter
    fun fromColor(value: Color): String = Utility.colorToHexColor(value)

    @TypeConverter
    fun toColor(value: String): Color = Utility.hexColorToColor(value)
}
