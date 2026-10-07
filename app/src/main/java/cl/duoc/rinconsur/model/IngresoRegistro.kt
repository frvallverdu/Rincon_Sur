package cl.duoc.rinconsur.model

data class IngresoRegistro(
    val numeroEntrada: Int,
    val tipoFlujo: String,        // "Cliente" o "Transeúnte"
    val rangoEdad: String         // "18 - 30 años", "31 - 40 años", "41- 60 años", "Más de 60 años"
)
