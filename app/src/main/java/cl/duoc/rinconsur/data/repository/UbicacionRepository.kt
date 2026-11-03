package cl.duoc.rinconsur.data.repository

import android.content.Context
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class UbicacionRepository(context: Context){
    private val contexto = context.applicationContext
    private val clienteUbicacion = LocationServices.getFusedLocationProviderClient(context.applicationContext)
    private val solicitudUbicacion =
        CurrentLocationRequest.Builder()
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .setMaxUpdateAgeMillis(10_000L)
            .setDurationMillis(20_000L)
            .build()
    suspend fun obtenerUbicacion(): Location {
        val tienePermisoPreciso = ContextCompat.checkSelfPermission(
            contexto,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val tienePermisoAproximado = ContextCompat.checkSelfPermission(
            contexto,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        if (!tienePermisoPreciso && !tienePermisoAproximado){
            throw SecurityException("La aplicación debe tener permiso para acceder a la ubicación")
        }
        return suspendCancellableCoroutine { continuacion ->
            val cancelacion = CancellationTokenSource()
            continuacion.invokeOnCancellation { cancelacion.cancel() }
            clienteUbicacion.getCurrentLocation(
                solicitudUbicacion,
                cancelacion.token
            )
                .addOnSuccessListener { ubicacion ->
                    if (continuacion.isActive){
                        if (ubicacion != null){
                            continuacion.resume(ubicacion)
                        } else {
                            continuacion.resumeWithException(
                                IllegalStateException(
                                    "No se pudo obtener la ubicación" + "Revisa que este activada y vuelve a intentar"
                                )
                            )
                        }
                    }
        }
                .addOnFailureListener { error ->
                    if ( continuacion.isActive){
                        continuacion.resumeWithException(error)
                    }
                }
                .addOnCanceledListener {
                    continuacion.cancel()
                }
        }
    }
}

