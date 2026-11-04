package cl.duoc.rinconsur.ui.screens

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
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import cl.duoc.rinconsur.viewmodel.AuthUiState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import cl.duoc.rinconsur.ui.utils.CameraCapture
import cl.duoc.rinconsur.ui.utils.PhotoBox
import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.ui.Alignment



@Composable
fun RegistroScreen(
    estado: AuthUiState,
    onNombreChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onContrasenaChange: (String) -> Unit,
    onRegistrarUsuario: (String?) -> Unit,
    onVolverALogin: () -> Unit
) {
    var mostrarCamara by rememberSaveable { mutableStateOf(false) }
    var selfieUri by rememberSaveable { mutableStateOf<String?>(null) }

    if (mostrarCamara) {
        CameraCapture(
            carpeta = "usuarios",
            onImageCaptured = { uri ->
                selfieUri = uri.toString()
                mostrarCamara = false
            },
            onClose = { mostrarCamara = false }
        )
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
    ){
        Text(
            text = "Crear una cuenta",
            style = MaterialTheme.typography.headlineMedium
        )
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            PhotoBox(
                imageUri = selfieUri,
                circle = true,
                onTakePhoto = { mostrarCamara = true }
            )
        }
        OutlinedTextField(
            value = estado.nombre,
            onValueChange = onNombreChange,
            label = { Text("Nombre")},
            singleLine = true,
            enabled = !estado.estaCargando,
            isError = estado.errorNombre != null,
            supportingText = {
                estado.errorNombre?.let { error ->
                    Text(error)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = estado.email,
            onValueChange = onEmailChange,
            label = { Text("Ingrese correo")},
            singleLine = true,
            enabled = !estado.estaCargando,
            isError = estado.errorEmail != null,
            supportingText = {
                estado.errorEmail?.let { error ->
                    Text(error)
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            ),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = estado.contrasena,
            onValueChange = onContrasenaChange,
            label = { Text("Contraseña")},
            singleLine = true,
            enabled = !estado.estaCargando,
            isError = estado.errorContrasena != null,
            supportingText = {
            estado.errorContrasena?.let { error ->
                Text(error)
            }
            },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = { onRegistrarUsuario(selfieUri) },
            enabled = !estado.estaCargando,
            modifier = Modifier.fillMaxWidth()
        ){
            Text("Registrarme")
        }
        if (estado.estaCargando){
            CircularProgressIndicator()
        }
        estado.mensaje?.let { mensaje ->
            Text(
                text = mensaje,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        TextButton(
            onClick = onVolverALogin,
            enabled = !estado.estaCargando
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Volver"
            )
            Text("Volver")
        }
    }
}