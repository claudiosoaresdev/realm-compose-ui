package br.com.claudiosoaresdev.realmcomposeui.feature.presentation.map

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import br.com.claudiosoaresdev.realmcomposeui.feature.presentation.components.PlaceholderScreen

@Composable
fun MapScreen(modifier: Modifier = Modifier) {
    PlaceholderScreen(
        title = "MAPA",
        icon = "map",
        description = "O mapa de Westeros entra quando o servidor publicar um payload com layout próprio.",
        modifier = modifier,
    )
}
