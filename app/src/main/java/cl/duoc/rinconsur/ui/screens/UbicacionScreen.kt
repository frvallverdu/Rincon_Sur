package cl.duoc.rinconsur.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cl.duoc.rinconsur.viewmodel.UbicacionUiState

@Composable
fun UbicacionScreen(
    estado: UbicacionUiState,
    onConsultarUbicacion: () -> Unit,
    onVolver: () -> Unit
) {
    Column(
        modifier = Modifier.padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ){
        TextButton(onClick = onVolver) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Volver"
            )
            Text(" Volver")
        }
        Text(
            text = "Verificar ubicación",
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = "Punto de referencia: Duoc UC"
        )
        Button(
            onClick = onConsultarUbicacion,
            enabled = !estado.estaConsultando,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Consultar ubicación")
        }

        if (estado.estaConsultando) {
            CircularProgressIndicator()
            Text("Buscando ubicación...")
        }
        estado.resultado?.let { resultado ->
            Text(
                text = "Distancia a la sede: " + "${resultado.distanciaMetros.toInt()} metros"
            )
            Text(
                text = "Precisión informada: " + "${resultado.precisionMetros.toInt()} metros"
            )
        }
        estado.mensajeError?.let { mensaje ->
            Text(
                text = mensaje,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}