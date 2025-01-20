package org.example.model

import java.util.*

data class Juego(
    var titulo:String,
    val genero:String,
    val precio:Double,
    val fechaLanzamiento: Date? = null
){
    override fun toString(): String {
        return "título: $titulo , género: $genero, precio: $precio, fecha de lanzamiento: $fechaLanzamiento"
    }
}