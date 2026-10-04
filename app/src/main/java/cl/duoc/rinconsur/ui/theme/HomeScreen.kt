package cl.duoc.rinconsur.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.duoc.rinconsur.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {

    // El Box principal abarca toda la pantalla de la app
    Box(modifier = Modifier.fillMaxSize()){

        // 1. Imagen de fondo para toda la pantalla
        Image(
            painter = painterResource(id = R.drawable.fondo),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // 2. Scaffold con fondo transparente para mostrar lo que está detrás
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                CenterAlignedTopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.primary
                    ),
                    title = { Text("Bien Venido a Rincon Sur!") }
                )
            }
        ) { innerPadding ->
            // Colocamos el contenedor de la Card adaptándose al padding del Scaffold
            Card(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp)
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.85f) // Blanco con 85% de opacidad
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Control dinámico del tamaño de la fuente para que el texto lo tengamos en una linea
                    var fontSize by remember { mutableStateOf(24.sp)/* Tamaño de fuente inicial */
                    }

                    Text(
                        text = "Tienda Departamental Rincon Sur",
                        style = MaterialTheme.typography.headlineMedium.copy(fontSize = fontSize), // Aplicamos el fontSize dinámico sobre el estilo base
                        maxLines = 1,
                        softWrap = false,
                        onTextLayout = { textLayoutResult ->
                            // Si el texto excede el ancho disponible, reduce el tamaño en un 10%
                            if (textLayoutResult.didOverflowWidth) {
                                fontSize = (fontSize.value * 0.9f).sp
                            }
                        }
                    )

                    Text(
                        text = "Seleccione la Entrada",
                        style = MaterialTheme.typography.titleMedium
                    )

                    // Matriz o lista de botones de las 4 entradas
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Fila 1: Entrada 1 y Entrada 2
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            ElevatedButton(
                                onClick = { /* TODO: Navegar o abrir Entrada 1 */ },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Entrada 1")
                            }
                            ElevatedButton(
                                onClick = { /* TODO: Navegar o abrir Entrada 2 */ },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Entrada 2")
                            }
                        }

                        // Fila 2: Entrada 3 y Entrada 4
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            ElevatedButton(
                                onClick = { /* TODO: Navegar o abrir Entrada 3 */ },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Entrada 3")
                            }
                            ElevatedButton(
                                onClick = { /* TODO: Navegar o abrir Entrada 4 */ },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Entrada 4")
                            }
                        }
                    }

                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "ififfo",
                        modifier = Modifier
                            .size(180.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .alpha(0.1f),
                        contentScale = ContentScale.Fit
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}
