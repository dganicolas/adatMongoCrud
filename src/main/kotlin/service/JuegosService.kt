package org.example.service

import com.mongodb.client.MongoCollection
import org.bson.Document
import org.example.model.Juego
import org.example.repositorio.ConexionMongo
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*

class JuegosService() {

    fun crearJuego() {
        var nombre: String = ""
        var genero: String = ""
        var precio: Double
        var fechaLanzamiento: Date = Date.from(Instant.now())

        do {
            try {
                println("¿Dime el nombre del juego?")
                nombre = readln()
                if (existeJuego(nombre)) {
                    println("Ya existe un juego con el nombre $nombre")
                    nombre = ""
                }
            } catch (e: Exception) {
                println("Error al leer el nombre del juego: ${e.message}")
            }
        } while (nombre.isBlank())

        do {
            try {
                println("¿Dime el género del juego?")
                genero = readln()
            } catch (e: Exception) {
                println("Error al leer el género del juego: ${e.message}")
            }
        } while (genero.isBlank())
        do {
            try {
                println("¿Dime el precio del juego?")
                precio = readln().toDouble()
            } catch (e: Exception) {
                precio = 0.0
                println("Error al leer el precio del juego: ${e.message}")
            }
        } while (precio == 0.0)

        try {
            println("¿Dime la fecha de lanzamiento del juego?")
            val inputFecha = readln()
            fechaLanzamiento = SimpleDateFormat("dd/MM/yyyy").parse(inputFecha)
        } catch (e: Exception) {
            println("Error al leer la fecha de lanzamiento: ${e.message}")
        }
        val juego = Juego(nombre,genero,precio,fechaLanzamiento)
        println("Información del juego:")
        println("Nombre: $nombre")
        println("Género: $genero")
        println("Precio: $precio")
        println("Fecha de Lanzamiento: $fechaLanzamiento")
        subirJuego(juego)
    }

    private fun existeJuego(nombre: String): Boolean {
        return try {
            val database = ConexionMongo.getDatabase("nicolasDga")
            val collection = database.getCollection("juegos", Juego::class.java)

            // Busca un documento con el campo "nombre" igual al proporcionado
            val count = collection.countDocuments(Document("nombre", nombre))
            count > 0
        } catch (e: Exception) {
            println("Error al verificar si existe un juego con el nombre \"$nombre\": ${e.message}")
            false
        }
    }

    private fun subirJuego(juego: Juego) {
        val database = ConexionMongo.getDatabase("nicolasDga")
        val collection: MongoCollection<Juego> = database.getCollection("juegos", Juego::class.java)
        collection.insertOne(juego)
    }

    fun listarJuegos(){
        val database = ConexionMongo.getDatabase("nicolasDga")
        val collection: MongoCollection<Juego> = database.getCollection("juegos", Juego::class.java)
        collection.find().forEach {
            println(
                "$it\n---------------"
            )
        }
    }

    fun juegosPorGeneros(genero: String){
        val database = ConexionMongo.getDatabase("nicolasDga")
        val collection: MongoCollection<Juego> = database.getCollection("juegos", Juego::class.java)
        val juegos = collection.find(Document("genero", genero))
            .sort(Document("titulo", 1)) // Orden ascendente por nombre
            .toList()

        juegos.forEach{
            println("$it\n---------------")
        }
    }

    fun eliminarJuegosPorGenero(genero: String) {
        try {
            println("¿Estás seguro de que deseas eliminar todos los juegos del género \"$genero\"? (sí/no)")
            val confirmacion = readln().toLowerCase()

            if (confirmacion == "sí" || confirmacion == "si") {
                val database = ConexionMongo.getDatabase("nicolasDga")
                val collection = database.getCollection("juegos", Juego::class.java)

                val result = collection.deleteMany(Document("genero", genero))

                if (result.deletedCount > 0) {
                    println("Se han eliminado ${result.deletedCount} juegos del género \"$genero\".")
                } else {
                    println("No se encontraron juegos para eliminar con el género \"$genero\".")
                }
            } else {
                println("Operación cancelada. No se han eliminado juegos.")
            }
        } catch (e: Exception) {
            println("Error al eliminar los juegos del género \"$genero\": ${e.message}")
        }
    }

    fun modificarJuego() {
        try {
            println("¿Dime el nombre del juego que deseas modificar?")
            val nombre = readln()
            val database = ConexionMongo.getDatabase("nicolasDga")
            val collection = database.getCollection("juegos", Juego::class.java)

            // Buscamos el juego por su nombre
            val juego = collection.find(Document("titulo", nombre)).first()

            if (juego != null) {
                println("Juego existente: ${juego.titulo} - ${juego.genero} - ${juego.precio} - ${juego.fechaLanzamiento}")

                // Modificar los campos
                println("¿Deseas modificar el nombre del juego? (Deja en blanco para no cambiarlo)")
                val nuevoNombre = readln()
                val nombreFinal = if (nuevoNombre.isNotBlank()) nuevoNombre else juego.titulo

                println("¿Deseas modificar el género del juego? (Deja en blanco para no cambiarlo)")
                val nuevoGenero = readln()
                val generoFinal = if (nuevoGenero.isNotBlank()) nuevoGenero else juego.genero

                println("¿Deseas modificar el precio del juego? (Deja en blanco para no cambiarlo)")
                val nuevoPrecio = readln()
                val precioFinal = if (nuevoPrecio.isNotBlank()) nuevoPrecio.toDouble() else juego.precio

                println("¿Deseas modificar la fecha de lanzamiento? (Deja en blanco para no cambiarlo)")
                val nuevaFecha = readln()
                val fechaFinal = if (nuevaFecha.isNotBlank()) {
                        SimpleDateFormat("dd/MM/yyyy").parse(nuevaFecha)
                } else juego.fechaLanzamiento

                // Actualizar el juego en la base de datos
                val filtro = Document("titulo", juego.titulo)
                val actualizacion = Document("\$set", Document("nombre", nombreFinal)
                    .append("genero", generoFinal)
                    .append("precio", precioFinal)
                    .append("fechaLanzamiento", fechaFinal))

                collection.updateOne(filtro, actualizacion)

                println("Juego actualizado exitosamente.")
            } else {
                println("No se encontró un juego con ese nombre.")
            }
        } catch (e: Exception) {
            println("Error al modificar el juego: ${e.message}")
        }
    }

    fun close() {
        ConexionMongo.close()
    }


}