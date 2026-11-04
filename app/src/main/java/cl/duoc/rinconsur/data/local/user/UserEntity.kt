package cl.duoc.rinconsur.data.local.user
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Index

/*
    Ponemos la notacion entity para darle un nombre a la tabla en
    la base de datos

 */
@Entity(tableName = "usuarios",indices = [Index(value = ["email"], unique = true)])
data class Usuario(
    /*
        Anotacion PrimaryKey con autogenerate en true para que
        el id incremente solo
     */
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nombre: String,
    val email: String,
    val contrasena: String,
    val fotoPerfilUri: String? = null
)



