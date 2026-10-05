package cl.duoc.rinconsur.navigation

sealed class NavigationEvent {

    /**
     * Evento para navegar a cualquier destino
     * @param route Ruta a navegar y debe hacer el pop
     *
     * @param popUpToRoute Ruta desde la cual navegar
     * @param inclusive Si la ruta debe ser removida
     * @param singleTop Si la ruta debe ser reemplazada
     *
     */

    data class NavigateTo(
        val route: Screen,
        val popUpToRoute: Screen? = null,
        val inclusive: Boolean = false,
        val singleTop: Boolean = false
    ) : NavigationEvent()

    /**
     * Evento para navegar hacia atrás
     */
    data object PopBackStack : NavigationEvent()

    /**
     * Evento para navegar hacia arriba en la jerarquía de navegación
     * y eliminar el elemento actual de la pila de navegación.
     */
    data object NavigateUp : NavigationEvent()

}