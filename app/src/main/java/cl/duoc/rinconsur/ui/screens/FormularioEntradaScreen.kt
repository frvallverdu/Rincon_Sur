package cl.duoc.rinconsur.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duoc.rinconsur.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioEntradaScreen(
    numeroEntrada: Int,
    viewModel: MainViewModel = viewModel()
) {
    // Opciones según el caso de estudio de Rincón Sur
    val tiposFlujo = listOf(
        "Cliente",
        "Transeúnte"
    )
    var flujoSeleccionado by remember { mutableStateOf(tiposFlujo[0]) }

    var cantidadAcompanantes by remember { mutableIntStateOf(0) }

    // Rangos de edad solicitados: 18-30, 31-40, 41-60, Más de 60
    val rangosEdad = listOf("18 - 30 años", "31 - 40 años", "41 - 60 años", "Más de 60 años")
    var edadSeleccionada by remember { mutableStateOf(rangosEdad[0]) }

    var mensajeConfirmacion by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Acceso / Puerta $numeroEntrada") },
                navigationIcon = {
                    IconButton(onClick = { viewModel.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = "Registro de Flujo - Puerta $numeroEntrada",
                style = MaterialTheme.typography.titleMedium
            )

            // --- 1. TIPO DE FLUJO (Cliente vs Bypass) ---
            Text("1. Propósito de ingreso:", style = MaterialTheme.typography.bodyLarge)
            tiposFlujo.forEach { flujo ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    RadioButton(
                        selected = (flujo == flujoSeleccionado),
                        onClick = { flujoSeleccionado = flujo }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = flujo)
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // --- 2. SEGMENTACIÓN (Rango de Edad) ---
            Text("2. Rango de Edad (Segmentación):", style = MaterialTheme.typography.bodyLarge)
            rangosEdad.forEach { edad ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    RadioButton(
                        selected = (edad == edadSeleccionada),
                        onClick = { edadSeleccionada = edad }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = edad)
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // --- 3. CANTIDAD DE ACOMPAÑANTES ---
            Text("3. Cantidad de acompañantes", style = MaterialTheme.typography.bodyLarge)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    enabled = cantidadAcompanantes > 0,
                    onClick = { cantidadAcompanantes-- }
                ) {
                    Text("−")
                }

                Text(
                    text = cantidadAcompanantes.toString(),
                    style = MaterialTheme.typography.headlineSmall
                )

                IconButton(
                    enabled = cantidadAcompanantes < 6,
                    onClick = { cantidadAcompanantes++ }
                ) {
                    Text("+")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // --- BOTÓN REGISTRAR ---
            Button(
                onClick = {
                    // Guardamos en el diccionario del ViewModel
                    viewModel.registrarIngreso(
                        numeroPuerta = numeroEntrada,
                        tipoFlujo = flujoSeleccionado,
                        rangoEdad = edadSeleccionada,
                        cantidadAcompanantes = cantidadAcompanantes
                    )

                    mensajeConfirmacion = "¡Registrado en Puerta $numeroEntrada con éxito!"
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Registrar Ingreso / Tránsito")
            }

            if (mensajeConfirmacion.isNotEmpty()) {
                Text(
                    text = mensajeConfirmacion,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Preview(name = "Formulario Entrada 1", showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun FormularioEntradaScreenPreview() {
    MaterialTheme {
        FormularioEntradaScreen(
            numeroEntrada = 1
        )
    }
}
