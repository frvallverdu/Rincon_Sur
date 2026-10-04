package cl.duoc.rinconsur.ui.screens

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
fun HomeScreenCompacta() {
    Box(modifier = Modifier.fillMaxSize()) {

        // 1. Imagen de fondo
        Image(
            painter = painterResource(id = R.drawable.fondo),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // 2. Scaffold transparente
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                CenterAlignedTopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.primary
                    ),
                    title = { Text("¡Bienvenido a Rincón Sur!") }
                )
            }
        ) { innerPadding ->
            Card(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp)
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.85f)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    var fontSize by remember { mutableStateOf(24.sp) }

                    Text(
                        text = "Tienda Departamental Rincón Sur",
                        style = MaterialTheme.typography.headlineMedium.copy(fontSize = fontSize),
                        maxLines = 1,
                        softWrap = false,
                        onTextLayout = { textLayoutResult ->
                            if (textLayoutResult.didOverflowWidth) {
                                fontSize = (fontSize.value * 0.9f).sp
                            }
                        }
                    )

                    Text(
                        text = "Seleccione la Entrada",
                        style = MaterialTheme.typography.titleMedium
                    )

                    // Matriz de botones 2x2
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            ElevatedButton(
                                onClick = { /* TODO */ },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Entrada 1")
                            }
                            ElevatedButton(
                                onClick = { /* TODO */ },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Entrada 2")
                            }
                        }

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            ElevatedButton(
                                onClick = { /* TODO */ },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Entrada 3")
                            }
                            ElevatedButton(
                                onClick = { /* TODO */ },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Entrada 4")
                            }
                        }
                    }

                    // Logo
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "Logo Rincón Sur",
                        modifier = Modifier
                            .size(180.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .alpha(0.2f),
                        contentScale = ContentScale.Fit
                    )
                }
            }
        }
    }
}

@Preview(name = "Compact", widthDp = 360, heightDp = 800, showBackground = true)
@Composable
fun HomeScreenCompactaPreview() {
    HomeScreenCompacta()
}
