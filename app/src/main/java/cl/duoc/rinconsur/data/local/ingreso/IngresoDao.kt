package cl.duoc.rinconsur.data.local.ingreso

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface IngresoDao {

    // Guarda una nueva fila en la tabla de SQLite
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarIngreso(ingreso: IngresoEntity)

    // Obtiene la lista completa de ingresos ordenados del mas reciente al mas antiguo
    @Query("SELECT * FROM ingresos ORDER BY id DESC")
    fun obtenerTodosLosIngresos(): Flow<List<IngresoEntity>>

    // Elimina todos los registros de la tabla
    @Query("DELETE FROM ingresos")
    suspend fun eliminarTodos()
}