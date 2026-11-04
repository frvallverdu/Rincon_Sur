package cl.duoc.rinconsur.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import cl.duoc.rinconsur.viewmodel.PerfilUiState
import coil.compose.AsyncImage
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.core.tween
import androidx.compose.material3.Button

@Composable
fun PerfilScreen(
    estado: PerfilUiState,
    onEditar: () -> Unit,
    onVolver: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = "Mi Perfil",
            style = MaterialTheme.typography.headlineMedium
        )
        if (estado.estaCargando){
            CircularProgressIndicator()
        }
        estado.perfil?.let { perfil ->
            val fotoUri = perfil.fotoPerfilUri
            if (!fotoUri.isNullOrBlank()){
                AsyncImage(
                    model = fotoUri,
                    contentDescription = "Foto de perfil",
                    modifier = Modifier
                        .size(130.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Text("Sin foto perfil")
            }
            Text(
                text = perfil.nombre,
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = perfil.email,
                style = MaterialTheme.typography.bodyLarge
            )
        }
        estado.mensajeError?.let { mensaje ->
            Text(
                text = mensaje,
                color = MaterialTheme.colorScheme.error
            )
        }
        Button(
            onClick = onEditar,
            enabled = !estado.estaCargando && estado.perfil != null,
            modifier = Modifier.fillMaxWidth()
        ) {
             Text("Editar perfil")
        }
        TextButton(
            onClick = onVolver,
            modifier = Modifier.fillMaxWidth()
        ){
            Text("Volver")
        }
    }
}

