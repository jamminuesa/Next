package com.juegosdemesa.next.data.model

import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.juegosdemesa.next.util.Utility

@Entity(tableName = "teams")
data class Team (
    @PrimaryKey (autoGenerate = true)
    var id: Int = 0,
    var name: String = "",
    var color: String = Utility.colorToHexColor(Color.Blue)
){
    @Ignore
    var totalScore = 0
    @Ignore
    var totalMiss = 0

    @Ignore
    constructor(id: Int, name: String): this(
        id = id,
        name = name,
        color = Utility.generateCreamColorRand(id)
    )

    fun toColor(): Color{
        return Utility.hexColorToColor(color)
    }
}