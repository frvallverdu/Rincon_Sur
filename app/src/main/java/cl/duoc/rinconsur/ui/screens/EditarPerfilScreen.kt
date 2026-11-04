package cl.duoc.rinconsur.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import cl.duoc.rinconsur.viewmodel.EditarPerfilUiState
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import cl.duoc.rinconsur.ui.utils.CameraCapture
import cl.duoc.rinconsur.ui.utils.PhotoBox
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import cl.duoc.rinconsur.ui.utils.AnimacionEntradaFormulario


@Composable
fun EditarPerfilScreen(
    estado: EditarPerfilUiState,
    onNombreChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onFotoChange: (String) -> Unit,
    onGuardar: () -> Unit,
    onCancelar: () -> Unit
) {
    var mostrarCamara by rememberSaveable() {
        mutableStateOf(false)
    }

    val habilitado = !estado.estaCargando && !estado.estaGuardando && !estado.guardadoCompletado
    BackHandler {
        if (!estado.estaGuardando){
            if (mostrarCamara){
                mostrarCamara = false
            } else {
                onCancelar()
            }
        }
    }
    if (mostrarCamara) {
        CameraCapture(
            carpeta = "usuarios",
            onImageCaptured = { uri ->
                onFotoChange(uri.toString())
                mostrarCamara = false
            },
            onClose = { mostrarCamara = false}
        )
        return
    }
    var mostrarFormulario by remember{
        mutableStateOf(false)
    }
    LaunchedEffect(Unit){
        mostrarFormulario = true
    }
    AnimacionEntradaFormulario(
        visible = mostrarFormulario
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Editar Perfil",
                style = MaterialTheme.typography.headlineMedium
            )
            if (estado.estaCargando) {
                CircularProgressIndicator()
                Text("Cargando tus datos...")
            }
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                PhotoBox(
                    imageUri = estado.fotoPerfilUri,
                    circle = true,
                    onTakePhoto = {
                        if (habilitado) {
                            mostrarCamara = true
                        }
                    }
                )
            }

            OutlinedTextField(
                value = estado.nombre,
                onValueChange = onNombreChange,
                label = { Text("Nombre") },
                singleLine = true,
                enabled = habilitado,
                isError = estado.errorNombre != null,
                supportingText = {
                    estado.errorNombre?.let { Text(it) }
                },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = estado.email,
                onValueChange = onEmailChange,
                label = { Text("Correo electrónico") },
                singleLine = true,
                enabled = habilitado,
                isError = estado.errorEmail != null,
                supportingText = { estado.errorEmail?.let { Text(it) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )
            estado.mensajeError?.let { mensaje ->
                Text(
                    text = mensaje,
                    color = MaterialTheme.colorScheme.error
                )
            }
            Button(
                onClick = onGuardar,
                enabled = habilitado,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar Cambios.")
            }
            if (estado.estaGuardando) {
                CircularProgressIndicator()
                Text("Guardando cambios...")
            }

            TextButton(
                onClick = onCancelar,
                enabled = !estado.estaGuardando,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cancelar")
            }
        }
    }
}
