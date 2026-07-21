package br.com.claudiosoaresdev.realmcomposeui.feature.presentation.profile

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import br.com.claudiosoaresdev.realmcomposeui.feature.presentation.components.PlaceholderScreen

@Composable
fun ProfileScreen(modifier: Modifier = Modifier) {
    PlaceholderScreen(
        title = "PERFIL",
        icon = "profile",
        description = "Perfil local do usuário — único destino que não depende de payload.",
        modifier = modifier,
    )
}
