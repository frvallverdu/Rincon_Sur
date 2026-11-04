package cl.duoc.rinconsur.data.repository
import cl.duoc.rinconsur.data.local.storage.UserSessionPrefs
import cl.duoc.rinconsur.data.local.user.UsuarioDao
import kotlinx.coroutines.flow.Flow
import android.database.sqlite.SQLiteConstraintException
import cl.duoc.rinconsur.data.local.user.Usuario
import java.util.Locale

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
class UserRepository(
    private val usuarioDao: UsuarioDao,
    private val userPreferences: UserSessionPrefs
){
    val isLoggedIn: Flow<Boolean> = userPreferences.isLoggedIn
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
    suspend fun cerrarSesion(){
        userPreferences.cerrarSesion()
    }

}