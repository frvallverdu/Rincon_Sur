package cl.duoc.rinconsur.data.repository

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import cl.duoc.rinconsur.data.local.ingreso.IngresoDao
import cl.duoc.rinconsur.data.local.ingreso.IngresoEntity
import cl.duoc.rinconsur.model.IngresoRegistro

class IngresoRepository(
    private val ingresoDao: IngresoDao? = null // DAO de Room
) {

    // Diccionario en memoria para respuestas rápidas en la UI
    val registrosPorPuerta = mutableStateMapOf<Int, MutableMap<String, MutableList<IngresoRegistro>>>()

    suspend fun registrarIngreso(
        numeroPuerta: Int,
        tipoFlujo: String,
        rangoEdad: String,
        cantidadAcompanantes: Int
    ) {
        val nuevoRegistro = IngresoRegistro(
            numeroEntrada = numeroPuerta,
            tipoFlujo = tipoFlujo,
            rangoEdad = rangoEdad,
            cantidadAcompanantes = cantidadAcompanantes
        )

        // 1. Guardar en memoria (para actualizar la pantalla de inmediato)
        val mapaPuerta = registrosPorPuerta.getOrPut(numeroPuerta) {
            mutableMapOf(
                "Cliente" to mutableStateListOf(),
                "Transeúnte" to mutableStateListOf()
            )
        }
        mapaPuerta[tipoFlujo]?.add(nuevoRegistro)

        // 2. Guardar en la Base de Datos SQLite usando Room
        ingresoDao?.insertarIngreso(
            IngresoEntity(
                numeroEntrada = numeroPuerta,
                tipoFlujo = tipoFlujo,
                rangoEdad = rangoEdad,
                cantidadAcompanantes = cantidadAcompanantes,
                fechaHora = nuevoRegistro.fechaHora
            )
        )
    }
}
