package br.com.claudiosoaresdev.realmcomposeui.feature.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.claudiosoaresdev.realmcomposeui.core.designsystem.RealmTheme
import br.com.claudiosoaresdev.realmcomposeui.core.designsystem.SduiImage
import br.com.claudiosoaresdev.realmcomposeui.core.designsystem.sduiIcon
import br.com.claudiosoaresdev.realmcomposeui.core.designsystem.toColor
import br.com.claudiosoaresdev.realmcomposeui.core.model.SduiAction
import br.com.claudiosoaresdev.realmcomposeui.core.model.SduiCardItem
import br.com.claudiosoaresdev.realmcomposeui.core.model.SduiComponent
import br.com.claudiosoaresdev.realmcomposeui.core.model.SduiIconItem

/** Fatia horizontal do hero ocupada pela arte de fundo. */
private const val HERO_ART_WIDTH_FRACTION = 0.62f

/** Handler único de ação: o componente não sabe navegar, só emite o que o payload mandou. */
typealias SduiActionHandler = (SduiAction?) -> Unit

@Composable
fun SduiTopBar(component: SduiComponent.TopBar, onAction: SduiActionHandler) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // `leading` (menu hambúrguer) não é renderizado: não há drawer no app.
        Column(modifier = Modifier.weight(1f)) {
            component.eyebrow?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Text(
                text = component.title,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground,
            )
            component.subtitle?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }
        component.trailing?.let { IconAction(it, onAction) }
    }
}

@Composable
private fun IconAction(item: SduiIconItem, onAction: SduiActionHandler) {
    val enabled = item.enabled && item.action?.enabled != false
    BadgedBox(
        badge = {
            if (item.badgeCount > 0) {
                Badge(containerColor = MaterialTheme.colorScheme.primary) {
                    Text(text = item.badgeCount.toString())
                }
            }
        },
    ) {
        IconButton(enabled = enabled, onClick = { onAction(item.action) }) {
            Icon(
                imageVector = sduiIcon(item.icon),
                contentDescription = item.title,
                tint = if (enabled) {
                    MaterialTheme.colorScheme.onBackground
                } else {
                    RealmTheme.sdui.colors.textDisabled.toColor()
                },
            )
        }
    }
}

@Composable
fun SduiHeroBanner(component: SduiComponent.HeroBanner, onAction: SduiActionHandler) {
    val theme = RealmTheme.sdui
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 320.dp)
            .clip(MaterialTheme.shapes.medium)
            .background(theme.colors.surface.toColor())
            .border(
                width = 1.dp,
                color = theme.colors.outline.toColor(),
                shape = MaterialTheme.shapes.medium,
            ),
    ) {
        // Arte de fundo em cover, ocupando a faixa direita do card: sobra respiro à
        // esquerda para o texto, sem deformar a imagem.
        component.backgroundImage?.let { path ->
            SduiImage(
                path = path,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .fillMaxHeight()
                    .fillMaxWidth(HERO_ART_WIDTH_FRACTION),
            )
            // Scrim horizontal: opaco na coluna do texto, limpo em cima da arte.
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        Brush.horizontalGradient(
                            0f to theme.colors.surface.toColor(),
                            0.42f to theme.colors.surface.toColor().copy(alpha = 0.85f),
                            1f to theme.colors.surface.toColor().copy(alpha = 0.10f),
                        ),
                    ),
            )
            // Reforço na base para o botão e a paginação não brigarem com a arte.
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        Brush.verticalGradient(
                            0.55f to Color.Transparent,
                            1f to theme.colors.surface.toColor().copy(alpha = 0.85f),
                        ),
                    ),
            )
        }

        HeroContent(
            component = component,
            onAction = onAction,
            modifier = Modifier.align(Alignment.BottomStart),
        )
    }
}

@Composable
private fun HeroContent(
    component: SduiComponent.HeroBanner,
    onAction: SduiActionHandler,
    modifier: Modifier = Modifier,
) {
    val theme = RealmTheme.sdui
    Column(modifier = modifier.fillMaxWidth().padding(20.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                component.eyebrow?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
                Spacer(Modifier.height(8.dp))
                Text(
                    text = component.title,
                    style = MaterialTheme.typography.displayLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    // Título de hero nunca passa de duas linhas, venha o que vier do payload.
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            component.sideLabel?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.End,
                    modifier = Modifier.width(72.dp),
                )
            }
        }
        // Descrição e arte dividem a faixa abaixo do título: texto de um lado,
        // imagem do outro, conforme `imageAlignment` do payload.
        Row(verticalAlignment = Alignment.Bottom) {
            val imageAtEnd = component.imageAlignment != "start"
            if (!imageAtEnd) {
                HeroArt(path = component.foregroundImage)
                Spacer(Modifier.width(12.dp))
            }
            Column(modifier = Modifier.weight(1f)) {
                component.description?.let {
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            if (imageAtEnd) {
                Spacer(Modifier.width(12.dp))
                HeroArt(path = component.foregroundImage)
            }
        }
        component.button?.let { button ->
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = { onAction(button.action) },
                enabled = button.enabled && button.action?.enabled != false,
                shape = RoundedCornerShape(theme.shapes.pillRadiusDp.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                ),
            ) {
                Text(text = button.title, style = MaterialTheme.typography.labelLarge)
                button.icon?.let {
                    Spacer(Modifier.width(8.dp))
                    Icon(imageVector = sduiIcon(it), contentDescription = null)
                }
            }
        }
        if (component.pagination.visible && component.pagination.itemCount > 0) {
            Spacer(Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                repeat(component.pagination.itemCount) { index ->
                    val selected = index == component.pagination.selectedIndex
                    Box(
                        modifier = Modifier
                            .size(width = if (selected) 20.dp else 6.dp, height = 6.dp)
                            .clip(CircleShape)
                            .background(
                                if (selected) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.outline
                                },
                            ),
                    )
                }
            }
        }
    }
}

/** Arte do hero: ocupa a lateral do card e some quando o payload não manda imagem. */
@Composable
private fun HeroArt(path: String?) {
    if (path == null) return
    SduiImage(
        path = path,
        contentDescription = null,
        contentScale = ContentScale.Fit,
        modifier = Modifier
            .width(120.dp)
            .height(150.dp),
    )
}

@Composable
fun SduiIconGrid(component: SduiComponent.IconGrid, onAction: SduiActionHandler) {
    val theme = RealmTheme.sdui
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(theme.spacing.itemGapDp.dp),
        userScrollEnabled = component.horizontalScroll,
    ) {
        items(items = component.items, key = { it.id }) { item ->
            ShortcutCard(item = item, onAction = onAction)
        }
    }
}

/** Atalho do `iconGrid`: card fechado com ícone e rótulo dentro. */
@Composable
private fun ShortcutCard(item: SduiIconItem, onAction: SduiActionHandler) {
    val theme = RealmTheme.sdui
    val enabled = item.enabled && item.action?.enabled != false
    val contentColor = if (enabled) {
        MaterialTheme.colorScheme.primary
    } else {
        theme.colors.textDisabled.toColor()
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .size(width = 96.dp, height = 102.dp)
            .clip(MaterialTheme.shapes.small)
            .background(theme.colors.surface.toColor())
            .border(1.dp, theme.colors.outline.toColor(), MaterialTheme.shapes.small)
            .clickable(enabled = enabled) { onAction(item.action) }
            .padding(horizontal = 8.dp, vertical = 10.dp),
    ) {
        Icon(
            imageVector = sduiIcon(item.icon),
            contentDescription = item.title,
            tint = contentColor,
            modifier = Modifier.size(30.dp),
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = item.title,
            style = MaterialTheme.typography.labelLarge.copy(
                fontSize = 11.sp,
                lineHeight = 13.sp,
                letterSpacing = 0.4.sp,
            ),
            color = if (enabled) {
                MaterialTheme.colorScheme.onSurface
            } else {
                theme.colors.textDisabled.toColor()
            },
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
fun SduiCarousel(component: SduiComponent.HorizontalCarousel, onAction: SduiActionHandler) {
    val theme = RealmTheme.sdui
    Column(modifier = Modifier.fillMaxWidth()) {
        SectionHeader(
            title = component.title,
            actionTitle = component.seeAll?.title.takeIf { component.showSeeAll },
            onActionClick = { onAction(component.seeAll?.action) },
        )
        Spacer(Modifier.height(12.dp))
        // Scroll horizontal preguiçoso: a lista de membros cresce pelo payload,
        // então nada de compor os 9+ cards de uma vez.
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(theme.spacing.itemGapDp.dp),
            userScrollEnabled = component.horizontalScroll,
        ) {
            items(items = component.items, key = { it.id }) { item ->
                MemberCard(item = item, onAction = onAction)
            }
        }
    }
}

@Composable
private fun MemberCard(item: SduiCardItem, onAction: SduiActionHandler) {
    val theme = RealmTheme.sdui
    Column(
        modifier = Modifier
            .width(150.dp)
            .clip(MaterialTheme.shapes.medium)
            .background(theme.colors.surface.toColor())
            .border(1.dp, theme.colors.outline.toColor(), MaterialTheme.shapes.medium)
            .clickable(enabled = item.enabled && item.action?.enabled != false) {
                onAction(item.action)
            },
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(190.dp),
        ) {
            SduiImage(
                path = item.image,
                contentDescription = item.title,
                fallbackLabel = item.title.take(1),
                modifier = Modifier.fillMaxWidth().height(190.dp),
            )
            // Degradê para o texto nunca competir com a foto.
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(72.dp)
                    .align(Alignment.BottomCenter)
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.Transparent, theme.colors.surface.toColor()),
                        ),
                    ),
            )
            item.badge?.let { badge ->
                Text(
                    text = badge.uppercase(),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(theme.shapes.pillRadiusDp.dp))
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(horizontal = 8.dp, vertical = 2.dp),
                )
            }
        }
        Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)) {
            Text(
                text = item.title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            item.subtitle?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Composable
fun SduiNewsList(component: SduiComponent.NewsList, onAction: SduiActionHandler) {
    val theme = RealmTheme.sdui
    Column(modifier = Modifier.fillMaxWidth()) {
        SectionHeader(
            title = component.title,
            actionTitle = component.seeAll?.title.takeIf { component.showSeeAll },
            onActionClick = { onAction(component.seeAll?.action) },
        )
        Spacer(Modifier.height(12.dp))
        component.items.take(component.maxVisibleItems).forEach { item ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = theme.spacing.itemGapDp.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .background(theme.colors.surface.toColor())
                    .clickable(enabled = item.enabled) { onAction(item.action) }
                    .padding(16.dp),
            ) {
                Text(
                    text = item.category,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                item.description?.let {
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }
    }
}

@Composable
fun SectionHeader(title: String, actionTitle: String?, onActionClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(width = 3.dp, height = 18.dp)
                .background(MaterialTheme.colorScheme.primary),
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.weight(1f),
        )
        if (actionTitle != null) {
            Text(
                text = actionTitle,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable(onClick = onActionClick),
            )
        }
    }
}

/**
 * O db.json referencia imagens que o json-server não serve; o placeholder mantém
 * o layout honesto sem inventar conteúdo.
 */
@Composable
fun ImagePlaceholder(label: String?, modifier: Modifier = Modifier) {
    val theme = RealmTheme.sdui
    Box(
        modifier = modifier
            .background(theme.colors.surfaceVariant.toColor())
            .border(1.dp, theme.colors.outline.toColor()),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = label ?: "",
            style = MaterialTheme.typography.labelLarge,
            color = theme.colors.textDisabled.toColor(),
        )
    }
}

@Composable
fun SduiScreenBackground(content: @Composable () -> Unit) {
    val theme = RealmTheme.sdui
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(theme.background.color.toColor(Color.Black)),
    ) {
        content()
    }
}
