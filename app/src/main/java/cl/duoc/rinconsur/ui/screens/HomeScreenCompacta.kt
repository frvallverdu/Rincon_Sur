package cl.duoc.rinconsur.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.duoc.rinconsur.R
import cl.duoc.rinconsur.viewmodel.MainViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duoc.rinconsur.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenCompacta(viewModel: MainViewModel = viewModel(), nombreUsuario: String = "", onCerrarSesion: () -> Unit) {
    var menuExpanded by remember { mutableStateOf(false) }

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
                    title = { Text("¡Bienvenido a Rincón Sur!") },
                    actions = {
                        IconButton(onClick = { menuExpanded = true }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "Menú"
                            )
                        }

                        DropdownMenu(
                            expanded = menuExpanded,
                            onDismissRequest = { menuExpanded = false }
                        ) {
                            // Opción para ver Resultados
                            DropdownMenuItem(
                                text = { Text("Ver Resultados / Estadísticas") },
                                onClick = {
                                    menuExpanded = false
                                    viewModel.navigateTo(Screen.Resultados)
                                }
                            )

                            HorizontalDivider(
                                Modifier,
                                DividerDefaults.Thickness,
                                DividerDefaults.color
                            )

                            DropdownMenuItem(
                                text = { Text("Verificar ubicación")},
                                onClick = {
                                    menuExpanded = false
                                    viewModel.navigateTo(Screen.Ubicacion)
                                }
                            )



                            // Opción Salir
                            DropdownMenuItem(
                                text = { Text("Cerrar Sesión") },
                                onClick = {
                                    menuExpanded = false
                                    onCerrarSesion()
                                }
                            )
                        }
                    }
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
                        text = nombreUsuario.trim().takeIf { it.isNotEmpty() }?.let { "¡Hola, $it!" } ?: "¡Hola!",
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

                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            ElevatedButton(
                                onClick = { viewModel.navigateTo(Screen.VerificarEntrada(1)) },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Entrada 1")
                            }
                            ElevatedButton(
                                onClick = { viewModel.navigateTo(Screen.VerificarEntrada(2)) },
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
                                onClick = { viewModel.navigateTo(Screen.VerificarEntrada(3)) },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Entrada 3")
                            }
                            ElevatedButton(
                                onClick = { viewModel.navigateTo(Screen.VerificarEntrada(4)) },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Entrada 4")
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // Botón para ir directo a Resultados
                    OutlinedButton(
                        onClick = { viewModel.navigateTo(Screen.Resultados) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Ver Resultados y Estadísticas por Puerta")
                    }

                    // Logo
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "Logo Rincon Sur",
                        modifier = Modifier
                            .size(150.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .alpha(0.1f),
                        contentScale = ContentScale.Fit
                    )
                }
            }
        }
    }
}

