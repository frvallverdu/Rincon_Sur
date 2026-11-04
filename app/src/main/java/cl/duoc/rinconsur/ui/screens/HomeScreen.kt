package cl.duoc.rinconsur.ui.screens

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duoc.rinconsur.ui.utils.obtenerWindowSizeClass
import cl.duoc.rinconsur.viewmodel.MainViewModel
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass

@Composable
fun HomeScreen(
    viewModel: MainViewModel = viewModel(),
    nombreUsuario: String = "",
    onCerrarSesion: () -> Unit
) {
    val windowSizeClass = obtenerWindowSizeClass()

    when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> HomeScreenCompacta(
            viewModel = viewModel,
            nombreUsuario = nombreUsuario,
            onCerrarSesion = onCerrarSesion
        )
        WindowWidthSizeClass.Medium -> HomeScreenMediana(viewModel = viewModel, nombreUsuario = nombreUsuario)
        WindowWidthSizeClass.Expanded -> HomeScreenExpandida(viewModel = viewModel, nombreUsuario = nombreUsuario)
        else -> HomeScreenCompacta(
            viewModel = viewModel,
            nombreUsuario = nombreUsuario,
            onCerrarSesion = onCerrarSesion
        )
    }
}
