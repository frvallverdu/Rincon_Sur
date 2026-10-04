package cl.duoc.rinconsur.ui.screens

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import cl.duoc.rinconsur.ui.utils.obtenerWindowSizeClass

@Composable
fun HomeScreen() {
    // Obtenemos la clase de tamaño actual
    val windowSizeClass = obtenerWindowSizeClass()

    // Seleccionamos qué pantalla renderizar
    when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> HomeScreenCompacta()
        WindowWidthSizeClass.Medium -> HomeScreenMediana()
        WindowWidthSizeClass.Expanded -> HomeScreenExpandida()
        else -> HomeScreenCompacta() // Por defecto
    }
}