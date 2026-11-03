package cl.duoc.rinconsur.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import cl.duoc.rinconsur.data.local.ingreso.IngresoDao
import cl.duoc.rinconsur.data.local.user.Usuario
import cl.duoc.rinconsur.data.local.user.UsuarioDao
import cl.duoc.rinconsur.data.local.ingreso.IngresoEntity

/*
    notacion database para decirle a room que es una base de datos
    el entities es para registrar las tablas que tendra la base de datos segun el
    user entity
 */
@Database(entities = [Usuario::class, IngresoEntity::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    /*
        ahora creamos una funcion abstracta que va a retornar
        el dao del usuario
     */
    abstract fun usuarioDao(): UsuarioDao
    abstract fun ingresoDao(): IngresoDao // <-- Agregar esta línea

    companion object {
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
                .fallbackToDestructiveMigration(dropAllTables = true)
                .build().also { database ->
                    INSTANCE = database
                }
            }
        }
    }
}