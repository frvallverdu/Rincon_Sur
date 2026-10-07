package cl.duoc.rinconsur.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duoc.rinconsur.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Preview(name = "Compact", widthDp = 360, heightDp = 800, showBackground = true)
@Composable
fun ResultadosScreen(
    viewModel: MainViewModel = viewModel(),
) {
    val registrosPorPuerta = viewModel.registrosPorPuerta

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Resultados y Flujo por Puerta") },
                navigationIcon = {
                    IconButton(onClick = { viewModel.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            items(count = 4) { index ->
                val puertaNum = index + 1
                val mapaPuerta = registrosPorPuerta[puertaNum]

                val clientesList = mapaPuerta?.get("Cliente") ?: emptyList()
                val bypassList = mapaPuerta?.get("Transeúnte") ?: emptyList()

                val totalPuerta = clientesList.size + bypassList.size

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Text(
                            text = "Puerta / Acceso $puertaNum",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primary,
                        )

                        HorizontalDivider()

                        Text(
                            text = "Total de ingresos registrados: $totalPuerta",
                            style = MaterialTheme.typography.bodyLarge,
                        )

                        // Clientes con intención de compra
                        Text(
                            text = "Clientes: ${clientesList.size}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.secondary,
                        )
                        if (clientesList.isNotEmpty()) {
                            val edadesClientes = clientesList.groupingBy { it.rangoEdad }.eachCount()
                            for ((rango, count) in edadesClientes) {
                                Text(
                                    text = "   • $rango: $count personas",
                                    style = MaterialTheme.typography.bodySmall,
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        // Transeúntes / Bypass
                        Text(
                            text = "Transeúnte: ${bypassList.size}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.error,
                        )
                        if (bypassList.isNotEmpty()) {
                            val edadesBypass = bypassList.groupingBy { it.rangoEdad }.eachCount()
                            for ((rango, count) in edadesBypass) {
                                Text(
                                    text = "   • $rango: $count personas",
                                    style = MaterialTheme.typography.bodySmall,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
