package com.juegosdemesa.next.util

import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt
import java.util.concurrent.TimeUnit
import kotlin.random.Random


object Utility {

    //time to countdown - 1hr - 60secs
    const val TIME_COUNTDOWN: Long = 45000L
    private const val TIME_FORMAT = "%02d:%02d"


    //convert time to milli seconds
    fun Long.formatTime(): String = String.format(
        TIME_FORMAT,
        TimeUnit.MILLISECONDS.toMinutes(this),
        TimeUnit.MILLISECONDS.toSeconds(this) % 60
    )

    fun hexColorToColor(hexColor: String): Color {
        return try {
            Color(hexColor.toColorInt())
        } catch (e: NumberFormatException) {
            // Manejo de la excepción en caso de que la cadena no sea un color hexadecimal válido
            // Puedes imprimir un mensaje de error, lanzar una excepción personalizada, o manejarlo de otra manera según tus necesidades.
            Color.Blue  // Valor predeterminado en caso de error
        }
    }

    fun colorToHexColor(color: Color): String {
        // Convierte el color a su representación entera ARGB
        val argb = (color.alpha * 255.0f + 0.5f).toInt() shl 24 or
                (color.red * 255.0f + 0.5f).toInt() shl 16 or
                (color.green * 255.0f + 0.5f).toInt() shl 8 or
                (color.blue * 255.0f + 0.5f).toInt()

        // Convierte el valor entero ARGB a su representación hexadecimal como una cadena
        return String.format("#%08X", argb)
    }

    fun generateCreamColorRand(seed: Int): String {
        val random = Random(seed)

        val red = random.nextInt(100, 201)
        val green = random.nextInt(100, 201)
        val blue = random.nextInt(100, 201)

        return colorToHexColor(Color(red, green, blue))
    }

}