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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import cl.duoc.rinconsur.viewmodel.AuthUiState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import cl.duoc.rinconsur.R
import cl.duoc.rinconsur.ui.utils.pulsoYRotacion

@Composable
fun LoginScreen(
    estado: AuthUiState,
    onEmailChange: (String) -> Unit,
    onContrasenaChange: (String) -> Unit,
    onIniciarSesion:() -> Unit,
    onIrARegistro:() -> Unit
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.logo),
            contentDescription = "Logo de Rincon Sur",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .size(100.dp)
                .pulsoYRotacion()
        )
        Text(
            text = "Iniciar sesión",
            style = MaterialTheme.typography.headlineMedium
        )
        OutlinedTextField(
            value = estado.email,
            onValueChange = onEmailChange,
            label = { Text("Correo electronico") },
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
            label = { Text("Contraseña") },
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
            onClick = onIniciarSesion,
            enabled = !estado.estaCargando,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ingresar")
        }
        if (estado.estaCargando) {
            CircularProgressIndicator()
        }
        estado.mensaje?.let { mensaje ->
            Text(
                text = mensaje,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        TextButton(
            onClick = onIrARegistro,
            enabled = !estado.estaCargando,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Crear una cuenta")
        }
    }
}