package cl.duoc.rinconsur.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duoc.rinconsur.R
import cl.duoc.rinconsur.navigation.Screen
import cl.duoc.rinconsur.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenMediana(viewModel: MainViewModel = viewModel(), nombreUsuario: String = "") {
    Box(modifier = Modifier.fillMaxSize()) {

        // Imagen de fondo
        Image(
            painter = painterResource(id = R.drawable.fondo),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                CenterAlignedTopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.primary
                    ),
                    title = { Text("Tienda Departamental Rincon Sur") }
                )
            }
        ) { innerPadding ->
            Card(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(24.dp) // Más margen exterior para pantallas más anchas
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.85f)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = nombreUsuario.trim().takeIf { it.isNotEmpty() }?.let { "¡Hola, $it!" } ?: "¡Bienvenido!",
                        style = MaterialTheme.typography.headlineLarge // Texto más grande para Medium
                    )

                    Text(
                        text = "Seleccione la Entrada",
                        style = MaterialTheme.typography.titleLarge
                    )

                    // CAMBIO CLAVE PARA MEDIUM:
                    // En lugar de un 2x2, ponemos las 4 entradas en una SOLA FILA
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        ElevatedButton(
                            onClick = { viewModel.navigateTo(Screen.Entrada(1)) },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Entrada 1")
                        }
                        ElevatedButton(
                            onClick = { viewModel.navigateTo(Screen.Entrada(2)) },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Entrada 2")
                        }
                        ElevatedButton(
                            onClick = { viewModel.navigateTo(Screen.Entrada(3)) },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Entrada 3")
                        }
                        ElevatedButton(
                            onClick = { viewModel.navigateTo(Screen.Entrada(4)) },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Entrada 4")
                        }
                    }

                    // Logo más grande
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "Logo Rincón Sur",
                        modifier = Modifier
                            .size(220.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .alpha(0.1f),
                        contentScale = ContentScale.Fit
                    )
                }
            }
        }
    }
}
