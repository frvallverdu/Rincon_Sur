package cl.duoc.rinconsur.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import cl.duoc.rinconsur.data.local.ingreso.IngresoDao
import cl.duoc.rinconsur.data.local.user.Usuario
import cl.duoc.rinconsur.data.local.user.UsuarioDao
import cl.duoc.rinconsur.data.local.ingreso.IngresoEntity
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
/*
    notacion database para decirle a room que es una base de datos
    el entities es para registrar las tablas que tendra la base de datos segun el
    user entity
 */
@Database(entities = [Usuario::class, IngresoEntity::class], version = 3)
abstract class AppDatabase : RoomDatabase() {

    /*
        ahora creamos una funcion abstracta que va a retornar
        el dao del usuario
     */
    abstract fun usuarioDao(): UsuarioDao
    abstract fun ingresoDao(): IngresoDao // <-- Agregar esta línea

    companion object {

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
            CREATE TABLE IF NOT EXISTS `ingresos` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `numeroEntrada` INTEGER NOT NULL,
                `tipoFlujo` TEXT NOT NULL,
                `rangoEdad` TEXT NOT NULL,
                `cantidadAcompanantes` INTEGER NOT NULL,
                `fechaHora` TEXT NOT NULL
            )
            """.trimIndent()
                )
            }
        }

        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE usuarios ADD COLUMN fotoPerfilUri TEXT")
            }
        }

        /*
            la notacion volatile hace que la variable sea
            visible a todos los hilos de la aplicacion
         */
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /*
            esta funcion crea la instancia de la base de datos
            en singleton para que sea una para todos los hilos
            en el caso de que existe retorna la instancia existente
            si no la crea
         */
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "rincon_sur_database"
                )
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                    .build().also { database ->
                    INSTANCE = database
                }
            }
        }
    }
}