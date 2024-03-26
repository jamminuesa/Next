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

    @TypeConverter
    fun toCategoryList(string: String): List<Card.Category>{
        return string.split(separator).map { it.toInt() }.map {
            Card.Category.entries.find { category ->
                category.value == it } ?: throw IllegalArgumentException("El valor '$it' no es un valor " +
                    "válido de la enumeración") }
    }

    @TypeConverter
    fun fromCategoryList(list: List<Card.Category>): String {
        return if (list.isEmpty()) ""
        else list.map { it.value }.joinToString(separator = separator )
    }

}
