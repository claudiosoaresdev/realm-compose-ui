package br.com.claudiosoaresdev.realmcomposeui.feature.presentation.host

import br.com.claudiosoaresdev.realmcomposeui.core.model.SduiComponent
import br.com.claudiosoaresdev.realmcomposeui.core.model.SduiScreen
import br.com.claudiosoaresdev.realmcomposeui.core.model.SduiTheme

data class SduiHostState(
    val loading: Boolean = true,
    val house: String = "",
    val availableHouses: List<String> = listOf("targaryen", "hightower"),
    val screen: SduiScreen? = null,
    val error: String? = null,
) {
    val theme: SduiTheme get() = screen?.payload?.theme ?: SduiTheme()

    val components: List<SduiComponent> get() = screen?.payload?.components.orEmpty()

    val bottomNavigation: SduiComponent.BottomNavigation?
        get() = components.filterIsInstance<SduiComponent.BottomNavigation>().firstOrNull()

    /** O que a tela renderiza: tudo menos a barra inferior, que vive no Scaffold. */
    val bodyComponents: List<SduiComponent>
        get() = components.filterNot { it is SduiComponent.BottomNavigation }

    /** Próxima casa do rodízio — alvo do botão flutuante da home. */
    val nextHouse: String
        get() {
            val index = availableHouses.indexOf(house)
            return availableHouses[(index + 1).mod(availableHouses.size)]
        }
}

sealed interface SduiHostEvent {
    data object Retry : SduiHostEvent
    data class SelectHouse(val house: String) : SduiHostEvent
}
