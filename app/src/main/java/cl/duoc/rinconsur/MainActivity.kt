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

class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                val navController = rememberNavController()

                // Recolector de eventos de navegación emitidos por el ViewModel
                LaunchedEffect(Unit) {
                    mainViewModel.navigationEvents.collectLatest { event ->
                        when (event) {
                            is NavigationEvent.NavigateTo -> {
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
                            is NavigationEvent.PopBackStack -> navController.popBackStack()
                            is NavigationEvent.NavigateUp -> navController.navigateUp()
                        }
                    }
                }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Inicio.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        // Ruta de inicio
                        composable(Screen.Inicio.route) {
                            HomeScreen(viewModel = mainViewModel)
                        }

                        // Ruta de resultados / estadísticas
                        composable(Screen.Resultados.route) {
                            ResultadosScreen(viewModel = mainViewModel)
                        }

                        // Ruta de entradas con argumento entero
                        composable(
                            route = Screen.Entrada.ROUTE,
                            arguments = listOf(
                                navArgument("numeroEntrada") { type = NavType.IntType }
                            )
                        ) { backStackEntry ->
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
