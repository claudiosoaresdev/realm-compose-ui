package br.com.claudiosoaresdev.realmcomposeui.feature.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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

/**
 * Destino ainda sem payload no servidor. Renderiza com o tema SDUI corrente para deixar
 * explícito que o tema é global e não pertence à home.
 */
@Composable
fun PlaceholderScreen(
    title: String,
    icon: String,
    description: String,
    modifier: Modifier = Modifier,
) {
    val theme = RealmTheme.sdui
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(
                horizontal = theme.spacing.screenHorizontalDp.dp,
                vertical = theme.spacing.screenTopDp.dp,
            ),
        verticalArrangement = Arrangement.spacedBy(theme.spacing.itemGapDp.dp),
    ) {
        SectionHeader(title = title, actionTitle = null, onActionClick = {})
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.medium)
                .background(theme.colors.surface.toColor())
                .border(1.dp, theme.colors.outline.toColor(), MaterialTheme.shapes.medium)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Icon(
                imageVector = sduiIcon(icon),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
