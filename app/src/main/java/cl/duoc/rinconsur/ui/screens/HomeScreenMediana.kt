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
import cl.duoc.rinconsur.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenMediana() {
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
                    title = { Text("¡Bien Venido a Rincón Sur!") }
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
                        text = "Tienda Departamental Rincón Sur",
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
                            onClick = { },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Entrada 1")
                        }
                        ElevatedButton(
                            onClick = { },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Entrada 2")
                        }
                        ElevatedButton(
                            onClick = { },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Entrada 3")
                        }
                        ElevatedButton(
                            onClick = { },
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
                            .alpha(0.25f),
                        contentScale = ContentScale.Fit
                    )
                }
            }
        }
    }
}

// Preview configurado con un ancho de 700dp (Rango de Medium es 600dp a 840dp)
@Preview(name = "Medium Screen", widthDp = 700, heightDp = 1000, showBackground = true)
@Composable
fun HomeScreenMedianaPreview() {
    HomeScreenMediana()
}