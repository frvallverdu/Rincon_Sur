package cl.duoc.rinconsur.viewmodel

sealed interface EstadoSesion{
    data object Cargando : EstadoSesion
    data object SinSesion : EstadoSesion
    data object ConSesion : EstadoSesion
    data object Error : EstadoSesion
}
