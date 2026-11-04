package cl.duoc.rinconsur.viewmodel

import cl.duoc.rinconsur.model.ResultadoUbicacion

data class UbicacionUiState(
    val estaConsultando: Boolean = false,
    val resultado: ResultadoUbicacion? = null,
    val mensajeError: String? = null
)
