package br.com.claudiosoaresdev.realmcomposeui.feature.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import br.com.claudiosoaresdev.realmcomposeui.feature.presentation.components.SduiActionHandler
import br.com.claudiosoaresdev.realmcomposeui.feature.presentation.decrees.DecreesScreen
import br.com.claudiosoaresdev.realmcomposeui.feature.presentation.home.HomeScreen
import br.com.claudiosoaresdev.realmcomposeui.feature.presentation.host.SduiHostState
import br.com.claudiosoaresdev.realmcomposeui.feature.presentation.houses.HousesScreen
import br.com.claudiosoaresdev.realmcomposeui.feature.presentation.map.MapScreen
import br.com.claudiosoaresdev.realmcomposeui.feature.presentation.profile.ProfileScreen

@Composable
fun RealmNavHost(
    navController: NavHostController,
    state: SduiHostState,
    onAction: SduiActionHandler,
    onSelectHouse: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = RealmRoute.Home.route,
        modifier = modifier,
    ) {
        composable(RealmRoute.Home.route) {
            HomeScreen(state = state, onAction = onAction)
        }
        composable(RealmRoute.Houses.route) {
            HousesScreen(state = state, onSelectHouse = onSelectHouse)
        }
        composable(RealmRoute.Map.route) { MapScreen() }
        composable(RealmRoute.Decrees.route) { DecreesScreen() }
        composable(RealmRoute.Profile.route) { ProfileScreen() }
    }
}
