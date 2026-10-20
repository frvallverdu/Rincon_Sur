package cl.duoc.rinconsur.model

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Modelo de Datos que representa cada registro de ingreso en la Tienda Departamental Rincón Sur.
 *
 * @param numeroEntrada Número del acceso o puerta (1, 2, 3 o 4).
 * @param tipoFlujo Propósito del ingreso ("Cliente" o "Transeúnte").
 * @param rangoEdad Segmentación por edad ("18 - 30 años", "31 - 40 años", "41 - 60 años", "Más de 60 años").
 * @param fechaHora Fecha y hora exacta en que se realizó el registro (ej: "28/10/2025 14:35:10").
 */
data class IngresoRegistro(
    val numeroEntrada: Int,
    val tipoFlujo: String,        // "Cliente" o "Transeúnte"
    val rangoEdad: String,        // "18 - 30 años", "31 - 40 años", "41 - 60 años", "Más de 60 años"
    val cantidadAcompanantes: Int = 0,
    val fechaHora: String = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(Date())
)
