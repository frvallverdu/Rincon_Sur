package cl.duoc.rinconsur.navigation

sealed class Screen(val route: String) {
    data object Login : Screen("login")

    data object Registro : Screen("registro")
    data object Inicio : Screen("inicio")

    data object Resultados : Screen("resultados")

    data class Entrada(val numeroEntrada: Int) : Screen(ROUTE) {
        
        /**
         * Construye la URL final reemplazando el parámetro por el valor real.
         * Ejemplo: Screen.Entrada(2).buildRoute() produce "entrada/2"
         */
        fun buildRoute(): String = "entrada/$numeroEntrada"

        companion object {
            // Patrón de la ruta registrado en el NavHost para recibir el argumento entero
            const val ROUTE = "entrada/{numeroEntrada}"
        }
    }
}
