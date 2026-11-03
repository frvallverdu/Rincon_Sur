package cl.duoc.rinconsur.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duoc.rinconsur.viewmodel.UbicacionViewModel

@Composable
fun UbicacionRoute(
    onUbicacionVerificada: (() -> Unit)? = null
) {
    val contexto = LocalContext.current
    val ubicacionViewModel: UbicacionViewModel = viewModel()
    val estado by ubicacionViewModel.estado.collectAsStateWithLifecycle()
    val solicitarPermisos = rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestMultiplePermissions()) {
        permisos -> val tienePermiso = permisos[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            permisos[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (tienePermiso){
            ubicacionViewModel.consultarUbicacion()
        } else {
            ubicacionViewModel.informarPermisoDenegado()
        }
    }
    LaunchedEffect(estado) {
        val resultado = estado.resultado
        if (
            !estado.estaConsultando && estado.mensajeError == null && resultado?.estaDentroDelRadio == true
        ) {
            onUbicacionVerificada?.invoke()
        }
    }
    UbicacionScreen(
        estado = estado,
        onConsultarUbicacion = {
            val permisoPreciso = ContextCompat.checkSelfPermission(contexto, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
            val permisoAproximado = ContextCompat.checkSelfPermission(contexto,Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
            if (permisoPreciso || permisoAproximado){
                ubicacionViewModel.consultarUbicacion()
            } else {
                solicitarPermisos.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))
            }
        }
    )

}