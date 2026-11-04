package cl.duoc.rinconsur.data.local.user

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
/*
    Se pone anotacion Dao para que la interfaz pueda
    acceder a los datos
 */
@Dao
interface UsuarioDao {
    /*
        Primero insertamos un nuevo usuario en la tabla que creamos
        en el UserEntity
        se pone con on conflict con abort para que si el correo se repite
        rechace insertar el usuario
        la ponemos en suspend para el uso de corrutinas
     */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun registrarUsuario(usuario: Usuario)

    /*
        Acá va la query para hacer el login del usuario
        lo que hace es buscar dentro de la database el email y contraseña
        si coincide retorna el mismo usuario o null si las
        credenciales son incorrectas
     */
    @Query("SELECT * FROM usuarios WHERE email = :email AND contrasena = :contrasena LIMIT 1")
    suspend fun login(email: String, contrasena: String): Usuario?

    @Query("SELECT * FROM usuarios WHERE email = :email LIMIT 1")
    fun observarUsuario(email: String): Flow<Usuario?>
    /*
        Para verificar si un usuario ya existe en la base de datos
        usaremos la siguiente query
     */

    @Query("SELECT * FROM usuarios WHERE email = :email LIMIT 1")
    suspend fun existeUsuario(email: String): Usuario?

    @Update
    suspend fun actualizarUsuario(usuario: Usuario): Int
}
