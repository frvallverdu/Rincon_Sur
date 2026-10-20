package cl.duoc.rinconsur.repository

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import cl.duoc.rinconsur.model.IngresoRegistro

class IngresoRepository {

    // Diccionario que actúa como "Base de Datos" simulada en memoria
    val registrosPorPuerta = mutableStateMapOf<Int, MutableMap<String, MutableList<IngresoRegistro>>>()

    fun registrarIngreso(
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

        val mapaPuerta = registrosPorPuerta.getOrPut(numeroPuerta) {
            mutableMapOf(
                "Cliente" to mutableStateListOf(),
                "Transeúnte" to mutableStateListOf()
            )
        }

        mapaPuerta[tipoFlujo]?.add(nuevoRegistro)
    }
}
