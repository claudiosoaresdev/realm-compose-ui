package br.com.claudiosoaresdev.realmcomposeui.feature.presentation.decrees

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import br.com.claudiosoaresdev.realmcomposeui.feature.presentation.components.PlaceholderScreen

@Composable
fun DecreesScreen(modifier: Modifier = Modifier) {
    PlaceholderScreen(
        title = "DECRETOS",
        icon = "description",
        description = "Decretos chegam do servidor; hoje o item existe só na barra inferior do payload.",
        modifier = modifier,
    )
}
