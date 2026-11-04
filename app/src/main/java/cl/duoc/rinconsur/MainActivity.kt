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
import cl.duoc.rinconsur.data.local.database.AppDatabase
import cl.duoc.rinconsur.data.local.storage.UserSessionPrefs
import cl.duoc.rinconsur.data.repository.UserRepository
import cl.duoc.rinconsur.viewmodel.AuthViewModel
import cl.duoc.rinconsur.viewmodel.AuthViewModelFactory
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cl.duoc.rinconsur.ui.screens.LoginScreen
import cl.duoc.rinconsur.ui.screens.RegistroScreen
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import cl.duoc.rinconsur.viewmodel.EstadoSesion
import androidx.compose.ui.unit.dp
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.activity.compose.BackHandler
import cl.duoc.rinconsur.ui.screens.UbicacionRoute


class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    private val authViewModel: AuthViewModel by viewModels {
        val database = AppDatabase.getDatabase(applicationContext)
        val userPreferences = UserSessionPrefs(applicationContext)
        val repository = UserRepository(
            usuarioDao = database.usuarioDao(),
            userPreferences = userPreferences
        )
        AuthViewModelFactory(repository)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                val navController = rememberNavController()
                val authState by authViewModel.uiState.collectAsStateWithLifecycle()
                val estadoSesion by authViewModel.estadoSesion.collectAsStateWithLifecycle()
                val context = LocalContext.current

                // Recolector de eventos de navegación emitidos por el ViewModel
                LaunchedEffect(Unit) {
                    mainViewModel.navigationEvents.collectLatest { event ->
                        when (event) {
                            is NavigationEvent.NavigateTo -> {
                                val destinationRoute = when (val screen = event.route) {
                                    Screen.Login -> screen.route
                                    Screen.Registro -> screen.route
                                    Screen.Ubicacion -> screen.route
                                    is Screen.Inicio -> screen.route
                                    is Screen.Resultados -> screen.route
                                    is Screen.VerificarEntrada -> screen.buildRoute()
                                    is Screen.Entrada -> screen.buildRoute()
                                }
                                navController.navigate(destinationRoute) {
                                    event.popUpToRoute?.let { target ->
                                        val popRoute = when (target) {
                                            Screen.Login -> target.route
                                            Screen.Registro -> target.route
                                            Screen.Ubicacion -> target.route
                                            is Screen.Inicio -> target.route
                                            is Screen.Resultados -> target.route
                                            is Screen.Entrada -> target.buildRoute()
                                            is Screen.VerificarEntrada -> target.buildRoute()
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
                    when (estadoSesion) {
                        EstadoSesion.Cargando -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(innerPadding),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }

                        EstadoSesion.Error -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(innerPadding)
                                    .padding(24.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "No se pudo leer la sesion. Cierra y vuelve a abrir la aplicación")
                            }
                        }

                        EstadoSesion.SinSesion,
                        EstadoSesion.ConSesion -> {
                            NavHost(
                                navController = navController,
                                startDestination = Screen.Login.route,
                                modifier = Modifier.padding(innerPadding)
                            ) {
                                composable(Screen.Login.route) {
                                    LoginScreen(
                                        estado = authState,
                                        onEmailChange = authViewModel::actualizarEmail,
                                        onContrasenaChange = authViewModel::actualizarContrasena,
                                        onIniciarSesion = authViewModel::iniciarSesion,
                                        onIrARegistro = {
                                            authViewModel.limpiarFormulario()

                                            navController.navigate(Screen.Registro.route) {
                                                launchSingleTop = true
                                            }
                                        }
                                    )
                                }
                                composable(Screen.Registro.route) {
                                    BackHandler {
                                        if (!authState.estaCargando){
                                            authViewModel.limpiarFormulario()
                                            navController.popBackStack()
                                        }
                                    }
                                    LaunchedEffect(authState.registroCompletado) {
                                        if (authState.registroCompletado) {
                                            authViewModel.prepararLoginRegistro()
                                            Toast.makeText(
                                                context,
                                                "Cuenta creada. Ahora puedes iniciar sesión",
                                                Toast.LENGTH_LONG
                                            ).show()
                                            navController.popBackStack()
                                        }
                                    }
                                    RegistroScreen(
                                        estado = authState,
                                        onNombreChange = authViewModel::actualizarNombre,
                                        onEmailChange = authViewModel::actualizarEmail,
                                        onContrasenaChange = authViewModel::actualizarContrasena,
                                        onRegistrarUsuario = { fotoPerfilUri ->
                                            authViewModel.registrarUsuario(fotoPerfilUri)
                                        },
                                        onVolverALogin = {
                                            authViewModel.limpiarFormulario()
                                            navController.popBackStack()
                                        }
                                    )
                                }
                                // Ruta de inicio
                                composable(Screen.Inicio.route) {
                                    HomeScreen(viewModel = mainViewModel, onCerrarSesion = authViewModel::cerrarSesion)
                                }

                                // Ruta de resultados / estadísticas
                                composable(Screen.Resultados.route) {
                                    ResultadosScreen(viewModel = mainViewModel)
                                }

                                composable(Screen.Ubicacion.route){
                                    UbicacionRoute(
                                        onVolver = { navController.navigateUp() }
                                    )
                                }
                                composable(
                                    route = Screen.VerificarEntrada.ROUTE,
                                    arguments = listOf(
                                        navArgument("numeroEntrada"){
                                            type = NavType.IntType
                                        }
                                    )
                                ) { backStackEntry ->
                                    val numeroEntrada = backStackEntry.arguments?.getInt("numeroEntrada") ?: 1
                                    UbicacionRoute(
                                        onVolver = { navController.navigateUp() },
                                        onUbicacionVerificada = {
                                            navController.navigate(
                                                Screen.Entrada(numeroEntrada).buildRoute()
                                            ) {
                                                popUpTo(backStackEntry.destination.id) {
                                                    inclusive = true
                                                }
                                                launchSingleTop = true
                                            }
                                        }
                                    )
                                }

                                // Ruta de entradas con argumento entero
                                composable(
                                    route = Screen.Entrada.ROUTE,
                                    arguments = listOf(
                                        navArgument("numeroEntrada") { type = NavType.IntType }
                                    )
                                ) { backStackEntry ->
                                    val numeroEntrada =
                                        backStackEntry.arguments?.getInt("numeroEntrada") ?: 1
                                    FormularioEntradaScreen(
                                        numeroEntrada = numeroEntrada,
                                        viewModel = mainViewModel
                                    )
                                }
                            }
                            LaunchedEffect(estadoSesion){
                                val rutaActual = navController.currentDestination?.route
                                val estaEnAutenticacion =
                                    rutaActual == Screen.Login.route || rutaActual == Screen.Registro.route
                                if (estadoSesion == EstadoSesion.ConSesion && estaEnAutenticacion){
                                    navController.navigate(Screen.Inicio.route){
                                        popUpTo(navController.graph.id){
                                            inclusive = true
                                        }
                                        launchSingleTop = true
                                    }
                                } else if (
                                    estadoSesion == EstadoSesion.SinSesion && !estaEnAutenticacion
                                ){
                                    navController.navigate(Screen.Login.route){
                                        popUpTo(navController.graph.id){
                                            inclusive = true
                                        }
                                        launchSingleTop = true
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
