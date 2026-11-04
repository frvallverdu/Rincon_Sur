package cl.duoc.rinconsur.viewmodel


data class EditarPerfilUiState(
    val nombre: String = "",
    val email: String = "",
    val fotoPerfilUri: String? = null,
    val errorNombre: String? = null,
    val errorEmail: String? = null,
    val estaCargando: Boolean = true,
    val estaGuardando: Boolean = false,
    val mensajeError: String? = null,
    val guardadoCompletado: Boolean =false
)
