package cl.duoc.rinconsur.navigation

sealed class Screen(val route: String) {

    data object Inicio : Screen("inicio")

    data class Entrada(val numeroEntrada: Int) : Screen(ROUTE) {
        fun buildRoute(): String = "entrada/$numeroEntrada"

        companion object {
            const val ROUTE = "entrada/{numeroEntrada}"
        }
    }
}