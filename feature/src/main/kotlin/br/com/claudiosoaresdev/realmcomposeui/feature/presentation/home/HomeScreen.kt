package br.com.claudiosoaresdev.realmcomposeui.feature.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.claudiosoaresdev.realmcomposeui.core.model.SduiComponent
import br.com.claudiosoaresdev.realmcomposeui.core.model.SduiScreenConfig
import br.com.claudiosoaresdev.realmcomposeui.feature.presentation.components.SduiActionHandler
import br.com.claudiosoaresdev.realmcomposeui.feature.presentation.components.SduiCarousel
import br.com.claudiosoaresdev.realmcomposeui.feature.presentation.components.SduiHeroBanner
import br.com.claudiosoaresdev.realmcomposeui.feature.presentation.components.SduiIconGrid
import br.com.claudiosoaresdev.realmcomposeui.feature.presentation.components.SduiNewsList
import br.com.claudiosoaresdev.realmcomposeui.feature.presentation.components.SduiTopBar
import br.com.claudiosoaresdev.realmcomposeui.feature.presentation.host.SduiHostState

/**
 * A home não tem layout próprio: ela é o interpretador da lista de componentes do payload.
 */
@Composable
fun HomeScreen(
    state: SduiHostState,
    onAction: SduiActionHandler,
    modifier: Modifier = Modifier,
) {
    val config = state.screen?.payload?.config ?: SduiScreenConfig()
    val scroll = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .then(if (config.scrollable) Modifier.verticalScroll(scroll) else Modifier)
            .padding(
                start = config.paddingStartDp.dp,
                end = config.paddingEndDp.dp,
                top = config.paddingTopDp.dp,
                bottom = config.paddingBottomDp.dp,
            ),
        verticalArrangement = Arrangement.spacedBy(config.componentSpacingDp.dp),
    ) {
        state.bodyComponents.forEach { component ->
            RenderComponent(component = component, onAction = onAction)
        }
    }
}

/**
 * Único ponto de despacho `type` → composable. Tipo novo entra aqui e em lugar nenhum mais.
 * Tipo desconhecido nunca chega: o parser já o descartou.
 */
@Composable
private fun RenderComponent(component: SduiComponent, onAction: SduiActionHandler) {
    when (component) {
        is SduiComponent.TopBar -> SduiTopBar(component, onAction)
        is SduiComponent.HeroBanner -> SduiHeroBanner(component, onAction)
        is SduiComponent.IconGrid -> SduiIconGrid(component, onAction)
        is SduiComponent.HorizontalCarousel -> SduiCarousel(component, onAction)
        is SduiComponent.NewsList -> SduiNewsList(component, onAction)
        is SduiComponent.BottomNavigation -> Unit // renderizado pelo Scaffold do host
    }
}
