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
                println("¿Dime el nombre del juego?")
                nombre = readln()
                if (existeJuego(nombre)) {
                    println("Ya existe un juego con el nombre $nombre")
                    nombre = ""
                }
        } while (nombre.isBlank())

        do {
                println("¿Dime el genero del juego?")
                genero = readln()
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
            println("Error al leer la fecha de lanzamiento: ${e.message} PONIENDO FECHA A DIA DE HOY")
        }
        val juego = Juego(nombre,genero,precio,fechaLanzamiento)
        subirJuego(juego)
        println("Juego guardado")
    }

    private fun existeJuego(nombre: String): Boolean {
        return try {
            val database = ConexionMongo.getDatabase("nicolasDga")
            val collection = database.getCollection("juegos", Juego::class.java)
            val count = collection.countDocuments(Document("titulo", nombre))
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
            .sort(Document("titulo", 1))
            .toList()

        juegos.forEach{
            println("$it\n---------------")
        }
    }

    fun eliminarJuegosPorGenero(genero: String) {
        try {
            println("¿Estás seguro de que deseas eliminar todos los juegos del género \"$genero\"? (sí/no)")
            val confirmacion = readln().lowercase(Locale.getDefault())

            if (confirmacion == "sí" || confirmacion == "si" || confirmacion == "s") {
                val database = ConexionMongo.getDatabase("nicolasDga")
                val collection = database.getCollection("juegos", Juego::class.java)

                val result = collection.deleteMany(Document("genero", genero))

                if (result.deletedCount > 0) {
                    println("Se han eliminado ${result.deletedCount} juegos del género $genero.")
                } else {
                    println("No se encontraron juegos para eliminar con el género $genero.")
                }
            } else {
                println("Operación cancelada. No se han eliminado juegos.")
            }
        } catch (e: Exception) {
            println("Error al eliminar los juegos del género $genero: ${e.message}")
        }
    }

    fun modificarJuego() {
        try {
            println("¿Dime el nombre del juego que deseas modificar?")
            val nombre = readln()
            val database = ConexionMongo.getDatabase("nicolasDga")
            val collection = database.getCollection("juegos", Juego::class.java)
            val juego = collection.find(Document("titulo", nombre)).first()

            if (juego != null) {
                println("Juego existente: ${juego.titulo} - ${juego.genero} - ${juego.precio} - ${juego.fechaLanzamiento}")

                println("¿Deseas modificar el nombre del juego? (Deja en blanco para no cambiarlo)")
                val nuevoNombre = readln()
                val nombreFinal = nuevoNombre.ifBlank { juego.titulo }

                println("¿Deseas modificar el género del juego? (Deja en blanco para no cambiarlo)")
                val nuevoGenero = readln()
                val generoFinal = nuevoGenero.ifBlank { juego.genero }

                println("¿Deseas modificar el precio del juego? (Deja en blanco para no cambiarlo)")
                val nuevoPrecio = readln()
                val precioFinal = if (nuevoPrecio.isNotBlank()) nuevoPrecio.toDouble() else juego.precio

                println("¿Deseas modificar la fecha de lanzamiento? (Deja en blanco para no cambiarlo)")
                val nuevaFecha = readln()
                val fechaFinal = if (nuevaFecha.isNotBlank()) {
                        SimpleDateFormat("dd/MM/yyyy").parse(nuevaFecha)
                } else juego.fechaLanzamiento

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