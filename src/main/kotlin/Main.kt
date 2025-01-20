package org.example

import org.example.service.JuegosService

fun main() {
    val juegosService = JuegosService()

    while (true) {
        println("\n--- Menú de Opciones ---")
        println("1. Crear un juego")
        println("2. Modificar un juego")
        println("3. Listar todos los juegos")
        println("4. Buscar juegos por género")
        println("5. Eliminar juegos por género")
        println("6. Salir")
        println("Selecciona una opción (1-6):")

        when (readln()) {
            "1" -> juegosService.crearJuego()
            "2" -> juegosService.modificarJuego()
            "3" -> juegosService.listarJuegos()
            "4" -> {
                println("Introduce el género de los juegos que deseas buscar:")
                val genero = readln()
                juegosService.juegosPorGeneros(genero)
            }
            "5" -> {
                println("Introduce el género de los juegos que deseas eliminar:")
                val genero = readln()
                juegosService.eliminarJuegosPorGenero(genero)
            }
            "6" -> {
                println("cerrando conexion a la base de datos y programa")
                juegosService.close()
                break
            }
            else -> println("Opción no válida. Por favor, selecciona un número entre 1 y 6.")
        }
    }
}