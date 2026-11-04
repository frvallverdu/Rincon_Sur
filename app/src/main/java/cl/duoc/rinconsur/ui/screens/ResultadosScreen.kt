package cl.duoc.rinconsur.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duoc.rinconsur.model.IngresoRegistro
import cl.duoc.rinconsur.viewmodel.MainViewModel

private val ClienteColor = Color(0xFF287A5B)
private val TransitoColor = Color(0xFFE6A23C)
private val EdadColors = listOf(Color(0xFF5376C5), Color(0xFF8D69B2), Color(0xFF3F9A9A), Color(0xFFD47B58), Color(0xFF75849B))

@Composable
fun ResultadosScreen(
    viewModel: MainViewModel = viewModel(),
) {
    ResultadosContent(
        registrosPorPuerta = viewModel.registrosPorPuerta,
        onBack = { viewModel.navigateUp() },
    )
}

@Preview(name = "Resultados visuales", widthDp = 360, heightDp = 800, showBackground = true)
@Composable
private fun ResultadosScreenPreview() {
    val registros = mapOf(
        1 to mapOf(
            "Cliente" to listOf(
                IngresoRegistro(1, "Cliente", "18 - 30 años", cantidadAcompanantes = 2),
                IngresoRegistro(1, "Cliente", "31 - 40 años"),
                IngresoRegistro(1, "Cliente", "31 - 40 años"),
            ),
            "Transeúnte" to listOf(IngresoRegistro(1, "Transeúnte", "18 - 30 años")),
        ),
        2 to mapOf(
            "Cliente" to listOf(IngresoRegistro(2, "Cliente", "41 - 60 años")),
            "Transeúnte" to listOf(
                IngresoRegistro(2, "Transeúnte", "18 - 30 años"),
                IngresoRegistro(2, "Transeúnte", "Más de 60 años"),
            ),
        ),
    )
    MaterialTheme {
        ResultadosContent(registrosPorPuerta = registros, onBack = {})
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ResultadosContent(
    registrosPorPuerta: Map<Int, Map<String, List<IngresoRegistro>>>,
    onBack: () -> Unit,
) {
    val resumen = (1..4).map { puerta ->
        val registros = registrosPorPuerta[puerta]
        val clientes = registros?.get("Cliente") ?: emptyList()
        val transitos = registros?.get("Transeúnte") ?: emptyList()
        PuertaResumen(puerta, clientes, transitos)
    }
    val totalClientes = resumen.sumOf { it.clientes.size + it.clientes.sumOf(IngresoRegistro::cantidadAcompanantes) }
    val totalTransitos = resumen.sumOf { it.transitos.size + it.transitos.sumOf(IngresoRegistro::cantidadAcompanantes) }
    val total = totalClientes + totalTransitos
    val edades = (resumen.flatMap { it.clientes + it.transitos })
        .groupingBy { it.rangoEdad }.eachCount().toList().sortedByDescending { it.second }

    val totalEdades = edades.sumOf { it.second }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Resumen de flujo", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
            )
        },
        containerColor = MaterialTheme.colorScheme.surface
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            contentPadding = PaddingValues(start = 18.dp, end = 18.dp, top = 8.dp, bottom = 28.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
        ) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Así se mueve la tienda", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    Text("Vista general de los ingresos registrados", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    shape = RoundedCornerShape(24.dp),
                ) {
                    Column(Modifier.fillMaxWidth().padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Icon(Icons.Default.Groups, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimaryContainer)
                            Text("INGRESOS TOTALES", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
                        }
                        Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("$total", style = MaterialTheme.typography.displayMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
                            Text("personas", modifier = Modifier.padding(bottom = 8.dp), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onPrimaryContainer)
                        }
                        if (total > 0) {
                            Row(Modifier.fillMaxWidth().height(12.dp).clip(CircleShape)) {
                                Box(Modifier.weight(totalClientes.toFloat().coerceAtLeast(0.001f)).fillMaxHeight().background(ClienteColor))
                                Box(Modifier.weight(totalTransitos.toFloat().coerceAtLeast(0.001f)).fillMaxHeight().background(TransitoColor))
                            }
                        } else {
                            Box(Modifier.fillMaxWidth().height(12.dp).clip(CircleShape).background(MaterialTheme.colorScheme.surface.copy(alpha = 0.6f)))
                        }
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            LeyendaConteo("Clientes", totalClientes, ClienteColor)
                            LeyendaConteo("Transeúntes", totalTransitos, TransitoColor)
                        }
                    }
                }
            }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    IndicadorCard("Clientes", totalClientes, ClienteColor, Icons.Default.Storefront, Modifier.weight(1f))
                    IndicadorCard("Transeúntes", totalTransitos, TransitoColor, Icons.Default.TrendingUp, Modifier.weight(1f))
                }
            }
            item {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Flujo por acceso", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text("Compara de dónde vienen los ingresos", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            items(resumen.size) { index ->
                val puerta = resumen[index]
                AccesoCard(puerta, total)
            }
            item {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Edades registradas", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text("Incluye solo a las personas con edad registrada; los acompañantes se suman al flujo total.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    if (edades.isEmpty()) {
                        Text("Aún no hay datos para mostrar.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    } else {
                        Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f))) {
                            Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                                edades.forEachIndexed { index, (rango, cantidad) ->
                                    val porcentaje = cantidad.toFloat() / totalEdades.coerceAtLeast(1)
                                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                                Box(Modifier.size(10.dp).clip(CircleShape).background(EdadColors[index % EdadColors.size]))
                                                Text(rango, style = MaterialTheme.typography.bodyMedium)
                                            }
                                            Text("$cantidad · ${(porcentaje * 100).toInt()}%", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold)
                                        }
                                        LinearProgressIndicator(
                                            progress = { porcentaje },
                                            modifier = Modifier.fillMaxWidth().height(7.dp).clip(CircleShape),
                                            color = EdadColors[index % EdadColors.size],
                                            trackColor = MaterialTheme.colorScheme.surfaceVariant,
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private data class PuertaResumen(val numero: Int, val clientes: List<IngresoRegistro>, val transitos: List<IngresoRegistro>) {
    val totalRegistros: Int get() = clientes.size + transitos.size
    val totalAcompanantes: Int get() = (clientes + transitos).sumOf(IngresoRegistro::cantidadAcompanantes)
    val personasClientes: Int get() = clientes.size + clientes.sumOf(IngresoRegistro::cantidadAcompanantes)
    val personasTransitos: Int get() = transitos.size + transitos.sumOf(IngresoRegistro::cantidadAcompanantes)
    val totalPersonas: Int get() = personasClientes + personasTransitos
}

@Composable
private fun LeyendaConteo(titulo: String, cantidad: Int, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(7.dp)) {
        Box(Modifier.size(9.dp).clip(CircleShape).background(color))
        Text("$titulo  $cantidad", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onPrimaryContainer)
    }
}

@Composable
private fun IndicadorCard(titulo: String, cantidad: Int, color: Color, icon: androidx.compose.ui.graphics.vector.ImageVector, modifier: Modifier = Modifier) {
    Card(modifier = modifier, shape = RoundedCornerShape(20.dp)) {
        Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Icon(icon, contentDescription = null, tint = color)
            Text("$cantidad", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text(titulo, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun AccesoCard(puerta: PuertaResumen, totalGlobal: Int) {
    Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f))) {
        Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Acceso ${puerta.numero}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Surface(shape = CircleShape, color = MaterialTheme.colorScheme.surface) {
                    Text("${puerta.totalPersonas} personas", modifier = Modifier.padding(horizontal = 11.dp, vertical = 6.dp), style = MaterialTheme.typography.labelMedium)
                }
            }
            Text("${puerta.totalRegistros} registros · ${puerta.totalAcompanantes} acompañantes", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            val fraccionClientes = if (puerta.totalPersonas == 0) 0f else puerta.personasClientes.toFloat() / puerta.totalPersonas
            Row(Modifier.fillMaxWidth().height(10.dp).clip(CircleShape)) {
                if (puerta.totalPersonas > 0) {
                    if (fraccionClientes > 0f) Box(Modifier.weight(fraccionClientes).fillMaxHeight().background(ClienteColor))
                    if (fraccionClientes < 1f) Box(Modifier.weight(1f - fraccionClientes).fillMaxHeight().background(TransitoColor))
                } else Box(Modifier.fillMaxWidth().weight(1f).fillMaxHeight().background(MaterialTheme.colorScheme.outlineVariant))
            }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                LeyendaConteo("Clientes", puerta.personasClientes, ClienteColor)
                LeyendaConteo("Transeúntes", puerta.personasTransitos, TransitoColor)
            }
            val proporcionDia = puerta.totalPersonas.toFloat() / totalGlobal.coerceAtLeast(1)
            Text("${(proporcionDia * 100).toInt()}% del flujo total", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}





