package cl.duoc.rinconsur.viewmodel

data class AuthUiState(
    val nombre: String = "",
    val email: String = "",
    val contrasena: String = "",
    val errorNombre: String? = null,
    val errorEmail: String? = null,
    val errorContrasena: String? = null,
    val estaCargando: Boolean = false,
    val mensaje: String? = null
)
