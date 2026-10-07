package cl.duoc.rinconsur.navigation

/**
 * Encapsula todos los posibles eventos u órdenes de navegación de la aplicación.
 * Permite que el ViewModel comunique intenciones de navegación a la vista (MainActivity)
 * de forma reactiva a través de un SharedFlow.
 */
sealed class NavigationEvent {

    /**
     * Evento para navegar a un nuevo destino (Pantalla).
     *
     * @param route Pantalla destino (Screen.Inicio, Screen.Resultados o Screen.Entrada(X)).
     * @param popUpToRoute Pantalla destino hasta la cual se borrará el historial de navegación (BackStack).
     * @param inclusive Si es true, también elimina la pantalla indicada en popUpToRoute del historial.
     * @param singleTop Si es true, evita duplicar la misma pantalla en la pila si ya estás sobre ella.
     */
    data class NavigateTo(
        val route: Screen,
        val popUpToRoute: Screen? = null,
        val inclusive: Boolean = false,
        val singleTop: Boolean = false
    ) : NavigationEvent()

    /**
     * Evento para volver atrás en el historial de tiempo (BackStack).
     * Equivale a presionar el botón físicamente 'Atrás' del sistema operativo Android.
     */
    data object PopBackStack : NavigationEvent()

    /**
     * Evento para subir un nivel en la jerarquía visual de la aplicación.
     * Equivale a presionar la flecha '<-' en la barra superior (TopAppBar).
     */
    data object NavigateUp : NavigationEvent()
}
