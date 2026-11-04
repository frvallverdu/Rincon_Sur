package cl.duoc.rinconsur.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.rinconsur.data.repository.UbicacionRepository
import cl.duoc.rinconsur.domain.validation.EvaluadorUbicacion
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import cl.duoc.rinconsur.model.ConfiguracionUbicacion


class UbicacionViewModel(
    application: Application
) : AndroidViewModel(application){
    private val repositorio = UbicacionRepository(application)
    private val _estado= MutableStateFlow(UbicacionUiState())
    val estado = _estado.asStateFlow()

    fun informarPermisoDenegado() {
        _estado.value = UbicacionUiState(
            mensajeError = "Necesitamos permiso de ubicación para verificar"
        )
    }

    fun consultarUbicacion() {
        if (_estado.value.estaConsultando){
            return
        }
        viewModelScope.launch {
            _estado.value = UbicacionUiState(
                estaConsultando = true
            )
            try {
                val ubicacion = repositorio.obtenerUbicacion()
                if (!ubicacion.hasAccuracy()) {
                    _estado.value = UbicacionUiState(mensajeError = "No se pudo conocer la precision. Reintenta")
                    return@launch
                }
                val resultado = EvaluadorUbicacion.evaluar(latitud=ubicacion.latitude, longitud = ubicacion.longitude, precisionMetros = ubicacion.accuracy)
                val mensajeError = when { resultado.precisionMetros > ConfiguracionUbicacion.PRECISION_MAXIMA_METROS ->
                    "La ubicación es poco precisa. Acercate más a la ubicación correcta y reintenta."
                    !resultado.estaDentroDelRadio -> "Estás fuera de la zona permitida."
                    else -> null
                }
                _estado.value = UbicacionUiState(
                    resultado = resultado,
                    mensajeError = mensajeError
                )
            }catch (error: CancellationException) {
                _estado.value = UbicacionUiState()
                throw error
            } catch (error: Exception) {
                _estado.value = UbicacionUiState( mensajeError = error.message ?: "Ocurrió un problema al consultar la ubicación")
            }
        }
    }
}