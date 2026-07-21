package br.com.claudiosoaresdev.realmcomposeui.feature.presentation.houses

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import br.com.claudiosoaresdev.realmcomposeui.core.designsystem.RealmTheme
import br.com.claudiosoaresdev.realmcomposeui.core.designsystem.sduiIcon
import br.com.claudiosoaresdev.realmcomposeui.core.designsystem.toColor
import br.com.claudiosoaresdev.realmcomposeui.feature.presentation.components.SectionHeader
import br.com.claudiosoaresdev.realmcomposeui.feature.presentation.host.SduiHostState

/**
 * Troca a casa ativa. Trocar a casa recarrega o payload — tema, textos e barra inferior
 * mudam juntos, sem release do app.
 */
@Composable
fun HousesScreen(
    state: SduiHostState,
    onSelectHouse: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val theme = RealmTheme.sdui
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(
                horizontal = theme.spacing.screenHorizontalDp.dp,
                vertical = theme.spacing.screenTopDp.dp,
            ),
        verticalArrangement = Arrangement.spacedBy(theme.spacing.itemGapDp.dp),
    ) {
        SectionHeader(title = "CASAS", actionTitle = null, onActionClick = {})
        Spacer(Modifier.height(4.dp))
        state.availableHouses.forEach { house ->
            val selected = house == state.house
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.medium)
                    .background(theme.colors.surface.toColor())
                    .border(
                        width = if (selected) 2.dp else 1.dp,
                        color = if (selected) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            theme.colors.outline.toColor()
                        },
                        shape = MaterialTheme.shapes.medium,
                    )
                    .clickable { onSelectHouse(house) }
                    .padding(16.dp),
            ) {
                Icon(
                    imageVector = sduiIcon(if (house == "targaryen") "dragon_egg" else "tower"),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                )
                Spacer(Modifier.padding(horizontal = 8.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = house.uppercase(),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Text(
                        text = if (selected) "Casa ativa" else "Tocar para carregar o payload",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
        state.screen?.let { screen ->
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Payload: ${screen.id} · schema ${screen.schemaVersion} · layout ${screen.layout}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
