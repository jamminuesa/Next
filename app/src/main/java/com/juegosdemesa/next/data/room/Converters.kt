package com.juegosdemesa.next.data.room

import androidx.room.TypeConverter
import com.juegosdemesa.next.data.model.Card

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
}
