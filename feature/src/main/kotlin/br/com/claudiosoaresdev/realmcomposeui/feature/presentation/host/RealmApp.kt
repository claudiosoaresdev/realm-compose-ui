package br.com.claudiosoaresdev.realmcomposeui.feature.presentation.host

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import br.com.claudiosoaresdev.realmcomposeui.core.designsystem.RealmSduiTheme
import br.com.claudiosoaresdev.realmcomposeui.core.designsystem.SduiImage
import br.com.claudiosoaresdev.realmcomposeui.core.designsystem.sduiIcon
import br.com.claudiosoaresdev.realmcomposeui.core.designsystem.toColor
import br.com.claudiosoaresdev.realmcomposeui.core.model.SduiAction
import br.com.claudiosoaresdev.realmcomposeui.core.model.SduiComponent
import br.com.claudiosoaresdev.realmcomposeui.core.model.SduiIconItem
import br.com.claudiosoaresdev.realmcomposeui.feature.navigation.RealmNavHost
import br.com.claudiosoaresdev.realmcomposeui.feature.navigation.RealmRoute
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

/**
 * Host do app: um único payload alimenta tema e barra inferior de todos os destinos.
 */
@Composable
fun RealmApp(
    modifier: Modifier = Modifier,
    viewModel: SduiHostViewModel = koinViewModel(),
    navController: NavHostController = rememberNavController(),
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    fun navigateTo(route: RealmRoute) {
        if (route.route == currentRoute) return
        navController.navigate(route.route) {
            popUpTo(RealmRoute.Home.route) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
    }

    fun handleAction(action: SduiAction?) {
        when (action) {
            null -> Unit
            is SduiAction.Navigate -> {
                if (!action.enabled) return
                RealmRoute.houseFromSduiRoute(action.route)
                    ?.takeIf { it != state.house && it in state.availableHouses }
                    ?.let { viewModel.onEvent(SduiHostEvent.SelectHouse(it)) }

                val target = RealmRoute.fromSduiRoute(action.route)
                if (target != null) {
                    navigateTo(target)
                } else {
                    scope.launch { snackbarHostState.showSnackbar("Rota sem destino local: ${action.route}") }
                }
            }

            is SduiAction.OpenDrawer ->
                scope.launch { snackbarHostState.showSnackbar("Menu lateral ainda não publicado no payload") }

            is SduiAction.Unsupported ->
                scope.launch { snackbarHostState.showSnackbar("Ação não suportada: ${action.type}") }
        }
    }

    RealmSduiTheme(theme = state.theme) {
        Box(modifier = Modifier.fillMaxSize().background(state.theme.background.color.toColor())) {
            // Fundo da tela vem do tema do payload: imagem em cover + overlay da casa.
            // Sem imagem, sobra a cor sólida — nenhum destino precisa saber disso.
            state.theme.background.image?.let { path ->
                SduiImage(
                    path = path,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    opaqueFallback = false,
                    modifier = Modifier.matchParentSize(),
                )
                state.theme.background.overlayColor?.let { overlay ->
                    Box(modifier = Modifier.matchParentSize().background(overlay.toColor()))
                }
            }

            Scaffold(
                modifier = modifier,
                containerColor = Color.Transparent,
                snackbarHost = { SnackbarHost(snackbarHostState) },
                floatingActionButton = {
                    // Só na home: troca a casa ativa e refaz o fetch do payload.
                    if (currentRoute == RealmRoute.Home.route && state.screen != null) {
                        ExtendedFloatingActionButton(
                            onClick = { viewModel.onEvent(SduiHostEvent.SelectHouse(state.nextHouse)) },
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                        ) {
                            Text(
                                text = state.nextHouse.uppercase(),
                                style = MaterialTheme.typography.labelLarge,
                            )
                        }
                    }
                },
                bottomBar = {
                    SduiBottomBar(
                        component = state.bottomNavigation,
                        currentRoute = currentRoute,
                        onNavigate = ::navigateTo,
                        onAction = ::handleAction,
                    )
                },
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                ) {
                    when {
                        state.loading && state.screen == null -> CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center),
                            color = MaterialTheme.colorScheme.primary,
                        )

                        state.screen == null -> ErrorState(
                            message = state.error ?: "Sem payload",
                            onRetry = { viewModel.onEvent(SduiHostEvent.Retry) },
                            modifier = Modifier.align(Alignment.Center),
                        )

                        else -> RealmNavHost(
                            navController = navController,
                            state = state,
                            onAction = ::handleAction,
                            onSelectHouse = { viewModel.onEvent(SduiHostEvent.SelectHouse(it)) },
                        )
                    }
                }
            }
        }
    }
}

/**
 * A barra inferior é SDUI: rótulos, ícones, ordem e habilitação vêm do payload.
 * Sem payload, cai na ordem local só para o app continuar navegável.
 */
@Composable
private fun SduiBottomBar(
    component: SduiComponent.BottomNavigation?,
    currentRoute: String?,
    onNavigate: (RealmRoute) -> Unit,
    onAction: (SduiAction?) -> Unit,
) {
    val items = component?.items ?: fallbackItems()

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
    ) {
        items.forEach { item ->
            val route = (item.action as? SduiAction.Navigate)?.route?.let(RealmRoute::fromSduiRoute)
            val selected = route?.route == currentRoute
            NavigationBarItem(
                selected = selected,
                enabled = item.enabled && item.action?.enabled != false,
                onClick = { if (route != null) onNavigate(route) else onAction(item.action) },
                icon = {
                    Icon(imageVector = sduiIcon(item.icon), contentDescription = item.title)
                },
                label = { Text(text = item.title, style = MaterialTheme.typography.labelLarge) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.surfaceVariant,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                ),
            )
        }
    }
}

private fun fallbackItems(): List<SduiIconItem> = listOf(
    SduiIconItem("home", "INÍCIO", "home", action = SduiAction.Navigate("/sdui")),
    SduiIconItem("house", "CASA", "crown", action = SduiAction.Navigate("/houses")),
    SduiIconItem("map", "MAPA", "map", action = SduiAction.Navigate("/map")),
    SduiIconItem("decrees", "DECRETOS", "description", action = SduiAction.Navigate("/decrees")),
    SduiIconItem("profile", "PERFIL", "person", action = SduiAction.Navigate("/profile")),
)

@Composable
private fun ErrorState(message: String, onRetry: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Text(
            text = "Suba o servidor com: npx json-server db.json",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Button(onClick = onRetry) { Text(text = "TENTAR DE NOVO") }
    }
}
