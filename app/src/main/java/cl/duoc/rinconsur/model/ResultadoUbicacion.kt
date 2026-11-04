package cl.duoc.rinconsur.model

data class ResultadoUbicacion(
    val distanciaMetros: Float,
    val estaDentroDelRadio: Boolean,
    val precisionMetros: Float
)
