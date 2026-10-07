package cl.duoc.rinconsur.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.rinconsur.navigation.NavigationEvent
import cl.duoc.rinconsur.navigation.Screen
import cl.duoc.rinconsur.repository.IngresoRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: IngresoRepository = IngresoRepository()
) : ViewModel() {

    private val _navigationEvents = MutableSharedFlow<NavigationEvent>()
    val navigationEvents: SharedFlow<NavigationEvent> = _navigationEvents.asSharedFlow()

    // Accedemos a los datos desde el Repositorio (Capa de Datos / BD simulada)
    val registrosPorPuerta get() = repository.registrosPorPuerta

    fun registrarIngreso(numeroPuerta: Int, tipoFlujo: String, rangoEdad: String) {
        repository.registrarIngreso(numeroPuerta, tipoFlujo, rangoEdad)
    }

    fun navigateTo(screen: Screen) {
        viewModelScope.launch {
            _navigationEvents.emit(NavigationEvent.NavigateTo(route = screen))
        }
    }

    fun navigateBack() {
        viewModelScope.launch {
            _navigationEvents.emit(NavigationEvent.PopBackStack)
        }
    }

    fun navigateUp() {
        viewModelScope.launch {
            _navigationEvents.emit(NavigationEvent.NavigateUp)
        }
    }
}
