package cl.duoc.rinconsur.viewmodel

import androidx.lifecycle.ViewModel
import cl.duoc.rinconsur.data.repository.UserRepository
import kotlinx.coroutines.flow.Flow
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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.first
import cl.duoc.rinconsur.data.repository.ResultadoEdicionPerfil



class AuthViewModel(
    private val repository: UserRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    private val _estadoSesion = MutableStateFlow<EstadoSesion>(EstadoSesion.Cargando)
    val estadoSesion: StateFlow<EstadoSesion> = _estadoSesion.asStateFlow()
    val nombreUsuario: Flow<String> = repository.nombreUsuario

    private val _estadoEdicion = MutableStateFlow(EditarPerfilUiState())
    val estadoEdicion: StateFlow<EditarPerfilUiState> = _estadoEdicion.asStateFlow()

    val estadoPerfil: StateFlow<PerfilUiState> = repository.perfilUsuario
        .map { perfil ->
            PerfilUiState(
                estaCargando = false,
                perfil = perfil,
                mensajeError = if (perfil == null) {
                    "No se encontro el perfil."
                } else {
                    null
                }
            )
        }
        .catch {
            emit(
                PerfilUiState(
                    estaCargando = false,
                    mensajeError = "No se pudo cargar el perfil"
                )
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(
                stopTimeoutMillis = 5_000,
                replayExpirationMillis = 0
            ),
            initialValue = PerfilUiState()
        )

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

    fun prepararEdicionPerfil(){
        if (_estadoEdicion.value.estaGuardando){
            return
        }
        _estadoEdicion.value = EditarPerfilUiState()
        viewModelScope.launch {
            try {
                val perfil = repository.perfilUsuario.first()
                _estadoEdicion.value = if (perfil != null){
                    EditarPerfilUiState(
                        nombre = perfil.nombre,
                        email = perfil.email,
                        fotoPerfilUri = perfil.fotoPerfilUri,
                        estaCargando = false
                    )
                } else {
                    EditarPerfilUiState(
                        estaCargando = false,
                        mensajeError = "No se encontró el usuario conectado"
                    )
                }
            } catch (error: CancellationException) {
                throw error
            } catch (error: Exception){
                _estadoEdicion.value = EditarPerfilUiState(
                    estaCargando = false,
                    mensajeError = "No se pudieron cargar los datos del perfil."
                )
            }
        }
    }
    fun actualizarNombrePerfil(nombre: String){
        if (_estadoEdicion.value.estaCargando || _estadoEdicion.value.estaGuardando) {
            return
        }
        _estadoEdicion.update { estadoActual ->
            estadoActual.copy(
                nombre = nombre,
                errorNombre = null,
                mensajeError = null
            )
        }
    }
    fun actualizarEmailPerfil(email: String){
        if (_estadoEdicion.value.estaCargando || _estadoEdicion.value.estaGuardando){
            return
        }
        _estadoEdicion.update { estadoActual ->
            estadoActual.copy(
                email = email,
                errorEmail = null,
                mensajeError = null
            )
        }
    }
    fun actualizarFotoPerfil(fotoPerfilUri: String){
        if (_estadoEdicion.value.estaCargando || _estadoEdicion.value.estaGuardando){
            return
        }
        _estadoEdicion.update {estadoActual ->
            estadoActual.copy(
                fotoPerfilUri = fotoPerfilUri,
                mensajeError = null
            )
        }
    }
    fun guardarPerfil(){
        val formulario = _estadoEdicion.value

        if (
            formulario.estaCargando ||
            formulario.estaGuardando ||
            formulario.guardadoCompletado
        ) {
            return
        }
        val errorNombre = Validators.validarNombre(formulario.nombre)
        val errorEmail = Validators.validarEmail(formulario.email)
        _estadoEdicion.update {
            it.copy(
                errorNombre = errorNombre,
                errorEmail = errorEmail,
                mensajeError = null
            )
        }
        if (errorNombre != null || errorEmail != null){
            return
        }
        _estadoEdicion.update{
            it.copy(estaGuardando = true)
        }
        viewModelScope.launch {
            try {
                val resultado = repository.actualizarPerfil(
                    nombre = formulario.nombre,
                    email = formulario.email,
                    fotoPerfilUri = formulario.fotoPerfilUri
                )
                when (resultado){
                    ResultadoEdicionPerfil.Exito -> {
                        _estadoEdicion.update{
                            it.copy(guardadoCompletado = true)
                        }
                    }

                    ResultadoEdicionPerfil.CorreoExistente -> {
                        _estadoEdicion.update {
                            it.copy(
                                errorEmail = "Ese correo pertenece a otra cuenta"
                            )
                        }
                    }

                    ResultadoEdicionPerfil.UsuarioNoEncontrado -> {
                        _estadoEdicion.update {
                            it.copy(
                                mensajeError = "No se encontró tu cuenta."
                            )
                        }
                    }
                }
            } catch (error: CancellationException){
                throw error
            } catch (error: Exception) {
                _estadoEdicion.update {
                    it.copy(
                        mensajeError = "No se pudo completar el guardado. " + "Verifica los cambios guardados"
                    )
                }
            } finally {
                _estadoEdicion.update {
                    it.copy(estaGuardando = false)
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

    fun registrarUsuario(fotoPerfilUri: String? = null) {
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
                    contrasena = formulario.contrasena,
                    fotoPerfilUri = fotoPerfilUri
                )
                when (resultado) {
                    ResultadoRegistro.Exito -> {
                        _uiState.update { estadoActual ->
                            estadoActual.copy(
                                contrasena = "",
                                mensaje = null,
                                registroCompletado = true
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
    fun cerrarSesion(){
        if (_uiState.value.estaCargando){
            return
        }
        _uiState.update { estadoActual ->
            estadoActual.copy(
                estaCargando = true,
                mensaje = null,
            )
        }
        viewModelScope.launch {
            try {
                repository.cerrarSesion()
                _uiState.value = AuthUiState()
            } catch (error: CancellationException){
                throw error
            } catch (error: Exception){
                _uiState.update { estadoActual ->
                    estadoActual.copy(
                        mensaje = "No se pudo cerrar la sesión. Reintenta"
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
    fun limpiarFormulario(){
        if (_uiState.value.estaCargando){
            return
        }
        _uiState.value = AuthUiState()
    }
    fun prepararLoginRegistro(){
        val emailRegistrado = _uiState.value.email
        _uiState.value = AuthUiState(
            email = emailRegistrado
        )
    }
}
