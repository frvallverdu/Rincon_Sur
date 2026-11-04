package cl.duoc.rinconsur.data.local.ingreso

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ingresos")
data class IngresoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val numeroEntrada: Int,
    val tipoFlujo: String,
    val rangoEdad: String,
    val cantidadAcompanantes: Int = 0,
    val fechaHora: String
)