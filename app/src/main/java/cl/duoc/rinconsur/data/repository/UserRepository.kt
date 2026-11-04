package cl.duoc.rinconsur.data.repository
import cl.duoc.rinconsur.data.local.storage.UserSessionPrefs
import cl.duoc.rinconsur.data.local.user.UsuarioDao
import kotlinx.coroutines.flow.Flow
import android.database.sqlite.SQLiteConstraintException
import cl.duoc.rinconsur.data.local.user.Usuario
import java.util.Locale
import cl.duoc.rinconsur.model.PerfilUsuario
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.first



/*
    Creamos la clase repository que va a recibir un usuario en Dao
    para asi poder trabajar con la base de datos
    y el userPreferens sirve para verificar el guardado de la sesion
    y el cierre de sesion
 */
/*
    Creamos un estado para verificar el resultado del registro
 */
sealed interface ResultadoRegistro{
    data object Exito : ResultadoRegistro
    data object CorreoExistente : ResultadoRegistro
}
sealed interface ResultadoLogin{
    data object Exito : ResultadoLogin
    data object CredencialesIncorrectas : ResultadoLogin
}

sealed interface ResultadoEdicionPerfil{
    data object Exito : ResultadoEdicionPerfil
    data object CorreoExistente : ResultadoEdicionPerfil
    data object  UsuarioNoEncontrado : ResultadoEdicionPerfil
}
class UserRepository(
    private val usuarioDao: UsuarioDao,
    private val userPreferences: UserSessionPrefs
){
    val isLoggedIn: Flow<Boolean> = userPreferences.isLoggedIn
    val nombreUsuario: Flow<String> = userPreferences.nombreUsuario

    @OptIn(ExperimentalCoroutinesApi::class)
    val perfilUsuario: Flow<PerfilUsuario?> =
        userPreferences.emailUsuario.flatMapLatest { email ->

            if (email.isBlank()) {
                flowOf<PerfilUsuario?>(null)
            } else {
                usuarioDao.observarUsuario(email).map { usuario ->
                    usuario?.let {
                        PerfilUsuario(
                            nombre = it.nombre,
                            email = it.email,
                            fotoPerfilUri = it.fotoPerfilUri
                        )
                    }
                }
            }
        }

    suspend fun registrarUsuario(
        nombre: String,
        email: String,
        contrasena: String,
        fotoPerfilUri: String? = null
    ): ResultadoRegistro{
        val emailNormalizado = email.trim().lowercase(Locale.ROOT)
        if (usuarioDao.existeUsuario(emailNormalizado) != null){
            return ResultadoRegistro.CorreoExistente
        }
        val nuevoUsuario = Usuario(
            nombre = nombre.trim(),
            email = emailNormalizado,
            contrasena = contrasena,
            fotoPerfilUri = fotoPerfilUri
        )
        try{
            usuarioDao.registrarUsuario(nuevoUsuario)
        }catch (error: SQLiteConstraintException){
            if (usuarioDao.existeUsuario(emailNormalizado) != null){

            return ResultadoRegistro.CorreoExistente
        }
            throw error
        }
        return ResultadoRegistro.Exito
    }
    suspend fun iniciarSesion(
        email: String,
        contrasena: String
    ): ResultadoLogin{
        val emailNormalizado = email.trim().lowercase(Locale.ROOT)
        val usuario = usuarioDao.login(
            email = emailNormalizado,
            contrasena = contrasena
        ) ?: return ResultadoLogin.CredencialesIncorrectas
        userPreferences.guardarSesion(
            email = usuario.email,
            nombre = usuario.nombre
        )
        return ResultadoLogin.Exito
    }

    suspend fun actualizarPerfil(
        nombre: String,
        email: String,
        fotoPerfilUri: String?
    ): ResultadoEdicionPerfil {
        val emailSesion = userPreferences.emailUsuario.first()

        if (emailSesion.isBlank()) {
            return ResultadoEdicionPerfil.UsuarioNoEncontrado
        }
        val usuarioActual = usuarioDao.existeUsuario(emailSesion) ?: return ResultadoEdicionPerfil.UsuarioNoEncontrado

        val emailNormalizado = email.trim().lowercase(Locale.ROOT)
        val usuarioConEseCorreo = usuarioDao.existeUsuario(emailNormalizado)

        if (usuarioConEseCorreo != null && usuarioConEseCorreo.id != usuarioActual.id) {
            return ResultadoEdicionPerfil.CorreoExistente
        }
        val usuarioActualizado = usuarioActual.copy(
            nombre = nombre.trim(),
            email = emailNormalizado,
            fotoPerfilUri = fotoPerfilUri
        )
        val filasActualizadas = try{
            usuarioDao.actualizarUsuario(usuarioActualizado)
        } catch (error: SQLiteConstraintException){
            val otroUsuario = usuarioDao.existeUsuario(emailNormalizado)
            if (otroUsuario != null && otroUsuario.id != usuarioActual.id) {
                return ResultadoEdicionPerfil.CorreoExistente
            }
            throw error
        }
        if (filasActualizadas != 1){
            return ResultadoEdicionPerfil.UsuarioNoEncontrado
        }
        userPreferences.guardarSesion(
            email = usuarioActualizado.email,
            nombre = usuarioActualizado.nombre
        )
        return ResultadoEdicionPerfil.Exito
    }

    suspend fun cerrarSesion(){
        userPreferences.cerrarSesion()
    }

}
