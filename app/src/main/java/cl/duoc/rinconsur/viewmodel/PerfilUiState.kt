package cl.duoc.rinconsur.viewmodel
import cl.duoc.rinconsur.model.PerfilUsuario

data class PerfilUiState (
    val estaCargando: Boolean = true,
    val perfil: PerfilUsuario? = null,
    val mensajeError: String? = null

)