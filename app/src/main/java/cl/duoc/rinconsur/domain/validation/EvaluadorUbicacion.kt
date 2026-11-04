package cl.duoc.rinconsur.domain.validation

import android.location.Location
import cl.duoc.rinconsur.model.ConfiguracionUbicacion
import cl.duoc.rinconsur.model.ResultadoUbicacion

object EvaluadorUbicacion{

    fun evaluar(
        latitud: Double,
        longitud: Double,
        precisionMetros: Float
    ): ResultadoUbicacion {
        val resultados = FloatArray(1)

        Location.distanceBetween(
            latitud,
            longitud,
            ConfiguracionUbicacion.LATITUD_SEDE,
            ConfiguracionUbicacion.LONGITUD_SEDE,
            resultados
        )
        val distancia = resultados[0]
        return ResultadoUbicacion(
            distanciaMetros = distancia,
            estaDentroDelRadio = distancia <= ConfiguracionUbicacion.RADIO_PERMITIDO_METROS,
            precisionMetros = precisionMetros
        )
    }
}
