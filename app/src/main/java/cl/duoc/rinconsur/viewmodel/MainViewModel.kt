package cl.duoc.rinconsur.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.rinconsur.data.repository.IngresoRepository
import cl.duoc.rinconsur.navigation.NavigationEvent
import cl.duoc.rinconsur.navigation.Screen
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

/**
 * MainViewModel administra el estado de la interfaz de usuario (UI) y
 * emite los eventos de navegación mediante un SharedFlow.
 */
class MainViewModel(
    // Inyección de la capa de datos (Repositorio que almacena los registros)
    private val repository: IngresoRepository = IngresoRepository()
) : ViewModel() {

    // Canal privado de eventos de navegación (Emisor)
    private val _navigationEvents = MutableSharedFlow<NavigationEvent>()
    
    // Canal público que escucha la Activity / NavHost (Receptor)
    val navigationEvents: SharedFlow<NavigationEvent> = _navigationEvents.asSharedFlow()

    // Expone los registros guardados en el Repositorio (Capa de Datos / BD simulada)
    val registrosPorPuerta get() = repository.registrosPorPuerta

    /**
     * Guarda un nuevo ingreso delegándolo al repositorio de datos.
     */
    fun registrarIngreso(
        numeroPuerta: Int,
        tipoFlujo: String,
        rangoEdad: String,
        cantidadAcompanantes: Int = 0
    ) {
        viewModelScope.launch {
            repository.registrarIngreso(
                numeroPuerta = numeroPuerta,
                tipoFlujo = tipoFlujo,
                rangoEdad = rangoEdad,
                cantidadAcompanantes = cantidadAcompanantes
            )
        }
    }

    /**
     * Emite la orden de navegar hacia una pantalla destino (Screen).
     */
    fun navigateTo(screen: Screen) {
        viewModelScope.launch {
            _navigationEvents.emit(NavigationEvent.NavigateTo(route = screen))
        }
    }

    /**
     * Emite la orden de volver atrás en el historial de navegación (PopBackStack).
     */
    fun navigateBack() {
        viewModelScope.launch {
            _navigationEvents.emit(NavigationEvent.PopBackStack)
        }
    }

    /**
     * Emite la orden de subir un nivel en la jerarquía visual (NavigateUp).
     */
    fun navigateUp() {
        viewModelScope.launch {
            _navigationEvents.emit(NavigationEvent.NavigateUp)
        }
    }
}
