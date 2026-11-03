package cl.duoc.rinconsur.data.repository

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import cl.duoc.rinconsur.data.local.ingreso.IngresoDao
import cl.duoc.rinconsur.data.local.ingreso.IngresoEntity
import cl.duoc.rinconsur.model.IngresoRegistro

class IngresoRepository(
    private val ingresoDao: IngresoDao // DAO de Room
) {

    // Diccionario en memoria para respuestas rápidas en la UI
    val registrosPorPuerta = mutableStateMapOf<Int, MutableMap<String, MutableList<IngresoRegistro>>>()

    suspend fun cargarDatosDesdeBD() {
        ingresoDao.obtenerTodosLosIngresos().collect { listaEntities ->
            // 1. 'listaEntities' es la lista de filas de la tabla SQLite que nos envía Room.

            // 2. Limpiamos la pantalla previa para no duplicar información
            registrosPorPuerta.clear()

            // 3. Tomamos cada elemento de esa lista entregada por Room
            for (entity in listaEntities) {

                // 4. Convertimos el dato de la BD al objeto de nuestra app
                val registro = IngresoRegistro(
                    numeroEntrada = entity.numeroEntrada,
                    tipoFlujo = entity.tipoFlujo,
                    rangoEdad = entity.rangoEdad,
                    cantidadAcompanantes = entity.cantidadAcompanantes,
                    fechaHora = entity.fechaHora
                )

                // 5. Lo metemos a nuestro mapa para que la pantalla de Resultados se dibuje
                val mapaPuerta = registrosPorPuerta.getOrPut(entity.numeroEntrada) {
                    mutableMapOf(
                        "Cliente" to mutableStateListOf(),
                        "Transeúnte" to mutableStateListOf()
                    )
                }
                mapaPuerta[entity.tipoFlujo]?.add(registro)
            }
        }
    }
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

        // Guardar directamente en la Base de Datos SQLite usando Room
        ingresoDao.insertarIngreso(
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
