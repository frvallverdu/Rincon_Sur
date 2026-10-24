package cl.duoc.rinconsur.viewmodel

import androidx.lifecycle.ViewModel
import cl.duoc.rinconsur.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import androidx.lifecycle.viewModelScope
import cl.duoc.rinconsur.data.repository.ResultadoRegistro
import cl.duoc.rinconsur.domain.validation.Validators
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import cl.duoc.rinconsur.data.repository.ResultadoLogin
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import java.io.IOException

class AuthViewModel(
    private val repository: UserRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    private val _estadoSesion = MutableStateFlow<EstadoSesion>(EstadoSesion.Cargando)
    val estadoSesion: StateFlow<EstadoSesion> = _estadoSesion.asStateFlow()
    init {
        viewModelScope.launch {
            repository.isLoggedIn
                .catch { error ->
                    if (error is IOException){
                        _estadoSesion.value = EstadoSesion.Error
                    } else {
                        throw error
                    }
                }
                .collect { sesionActiva ->
                    _estadoSesion.value = if (sesionActiva){
                        EstadoSesion.ConSesion
                    } else {
                        EstadoSesion.SinSesion
                    }
                }
        }
    }
    fun actualizarNombre(nombre: String) {
        _uiState.update { estadoActual ->
            estadoActual.copy(
                nombre = nombre,
                errorNombre = null,
                mensaje = null
            )
        }
    }

    fun actualizarEmail(email: String) {
        _uiState.update { estadoActual ->
            estadoActual.copy(
                email = email,
                errorEmail = null,
                mensaje = null
            )
        }
    }

    fun actualizarContrasena(contrasena: String) {
        _uiState.update { estadoActual ->
            estadoActual.copy(
                contrasena = contrasena,
                errorContrasena = null,
                mensaje = null
            )
        }
    }

    fun registrarUsuario() {
        val formulario = _uiState.value
        if (formulario.estaCargando) {
            return
        }
        val errorNombre = Validators.validarNombre(formulario.nombre)
        val errorEmail = Validators.validarEmail(formulario.email)
        val errorContrasena = Validators.validarContrasena(formulario.contrasena)
        _uiState.update { estadoActual ->
            estadoActual.copy(
                errorNombre = errorNombre,
                errorEmail = errorEmail,
                errorContrasena = errorContrasena,
                mensaje = null
            )
        }
        if (errorNombre != null || errorEmail != null || errorContrasena != null) {
            return
        }
        _uiState.update { estadoActual ->
            estadoActual.copy(estaCargando = true)
        }
        viewModelScope.launch {
            try {
                val resultado = repository.registrarUsuario(
                    nombre = formulario.nombre,
                    email = formulario.email,
                    contrasena = formulario.contrasena
                )
                when (resultado) {
                    ResultadoRegistro.Exito -> {
                        _uiState.update { estadoActual ->
                            estadoActual.copy(
                                contrasena = "",
                                mensaje = "Cuenta creada. Ahora puedes iniciar sesión."
                            )
                        }
                    }
                    ResultadoRegistro.CorreoExistente -> {
                        _uiState.update { estadoActual ->
                            estadoActual.copy(errorEmail = "Este correo ya fue registrado")
                        }
                    }
                }
            }catch (error: CancellationException) {
                throw error
            } catch (error: Exception) {
                _uiState.update { estadoActual ->
                    estadoActual.copy(
                        mensaje = "No se pudo completar el registro. Intentalo nuevamente"
                    )
                }
                } finally {
                _uiState.update { estadoActual ->
                    estadoActual.copy(
                        estaCargando = false
                    )
                }
            }
        }
    }
    fun iniciarSesion(){
        val formulario = _uiState.value

        if (formulario.estaCargando){
            return
        }
        val errorEmail = Validators.validarEmail(formulario.email)
        val errorContrasena = Validators.validarContrasena(formulario.contrasena)
        _uiState.update { estadoActual ->
            estadoActual.copy(
                errorNombre = null,
                errorEmail = errorEmail,
                errorContrasena = errorContrasena,
                mensaje = null
            )
        }
        if (errorEmail != null || errorContrasena != null){
            return
        }
        _uiState.update { estadoActual ->
            estadoActual.copy(estaCargando = true)
        }
        viewModelScope.launch {
            try {
                val resultado = repository.iniciarSesion(
                    email = formulario.email,
                    contrasena = formulario.contrasena
                )
                when (resultado) {
                    ResultadoLogin.Exito -> {
                        _uiState.update { estadoActual ->
                            estadoActual.copy(
                                contrasena = "",
                                mensaje = "Sesion iniciada Correctamente"
                            )
                        }
                    }

                    ResultadoLogin.CredencialesIncorrectas -> {
                        _uiState.update { estadoActual ->
                            estadoActual.copy(
                                mensaje = "Credenciales incorrectas. Reintente"
                            )
                        }
                    }
                }
            } catch (error: CancellationException) {
                throw error
            } catch (error: Exception){
                _uiState.update { estadoActual ->
                    estadoActual.copy(
                        mensaje = "No se puedo iniciar Sesión. Reintente"
                    )
                }
            } finally {
                _uiState.update { estadoActual ->
                    estadoActual.copy(estaCargando = false)
                }
            }
        }
    }
}