package cl.duoc.rinconsur

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import cl.duoc.rinconsur.navigation.NavigationEvent
import cl.duoc.rinconsur.navigation.Screen
import cl.duoc.rinconsur.ui.screens.FormularioEntradaScreen
import cl.duoc.rinconsur.ui.screens.HomeScreen
import cl.duoc.rinconsur.ui.screens.ResultadosScreen
import cl.duoc.rinconsur.viewmodel.MainViewModel
import kotlinx.coroutines.flow.collectLatest

/**
 * Actividad Principal de la Aplicación Rincón Sur.
 * Contiene el NavHost y escucha los eventos de navegación emitidos por el MainViewModel.
 */
class MainActivity : ComponentActivity() {

    // Instancia compartida del ViewModel en el ciclo de vida de la Activity
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                // Control remoto de navegación
                val navController = rememberNavController()

                // Recolector reactivo que escucha la "radio" de eventos del MainViewModel
                LaunchedEffect(Unit) {
                    mainViewModel.navigationEvents.collectLatest { event ->
                        when (event) {
                            is NavigationEvent.NavigateTo -> {
                                // Traduce la pantalla en su dirección URL correspondiente
                                val destinationRoute = when (val screen = event.route) {
                                    is Screen.Inicio -> screen.route
                                    is Screen.Resultados -> screen.route
                                    is Screen.Entrada -> screen.buildRoute()
                                }
                                navController.navigate(destinationRoute) {
                                    event.popUpToRoute?.let { target ->
                                        val popRoute = when (target) {
                                            is Screen.Inicio -> target.route
                                            is Screen.Resultados -> target.route
                                            is Screen.Entrada -> target.buildRoute()
                                        }
                                        popUpTo(popRoute) { inclusive = event.inclusive }
                                    }
                                    launchSingleTop = event.singleTop
                                }
                            }
                            // Ejecuta la vuelta atrás en el historial (PopBackStack)
                            is NavigationEvent.PopBackStack -> navController.popBackStack()

                            // Ejecuta el ascenso jerárquico (NavigateUp / Flecha arriba)
                            is NavigationEvent.NavigateUp -> navController.navigateUp()
                        }
                    }
                }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Contenedor que renderiza la pantalla activa según la ruta
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Inicio.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        // 1. Ruta de la pantalla de inicio
                        composable(Screen.Inicio.route) {
                            HomeScreen(viewModel = mainViewModel)
                        }

                        // 2. Ruta de la pantalla de resultados / estadísticas
                        composable(Screen.Resultados.route) {
                            ResultadosScreen(viewModel = mainViewModel)
                        }

                        // 3. Ruta parametrizada del formulario ("entrada/{numeroEntrada}")
                        composable(
                            route = Screen.Entrada.ROUTE,
                            arguments = listOf(
                                navArgument("numeroEntrada") { type = NavType.IntType }
                            )
                        ) { backStackEntry ->
                            // Lee el argumento de la URL
                            val numeroEntrada = backStackEntry.arguments?.getInt("numeroEntrada") ?: 1
                            FormularioEntradaScreen(
                                numeroEntrada = numeroEntrada,
                                viewModel = mainViewModel
                            )
                        }
                    }
                }
            }
        }
    }
}
