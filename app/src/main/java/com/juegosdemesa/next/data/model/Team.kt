package com.juegosdemesa.next.data.model

import androidx.compose.ui.graphics.Color
import androidx.room.Ignore
import com.juegosdemesa.next.util.Utility

data class Team (
    val id: Int = 0,
    val name: String,
    val color: Color
){

    @Ignore
    constructor(id: Int): this(
        id = id,
        name = "Equipo $id",
        color = Utility.generateCreamColorRand(id)
    )
}