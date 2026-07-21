package br.com.claudiosoaresdev.realmcomposeui.core.model

/**
 * Modelo do contrato SDUI. Nenhum tipo de framework aqui: só dado.
 * Cores continuam como string hex — a conversão para `Color` é responsabilidade do design system.
 */

data class SduiAppConfig(
    val appName: String,
    val defaultScreen: String,
    val defaultHouse: String,
    val supportedSchemaVersions: List<String>,
    val supportedLayouts: List<String>,
)

data class SduiScreen(
    val id: String,
    val screen: String,
    val house: String,
    val version: Int,
    val schemaVersion: String,
    val layout: String,
    val payload: SduiPayload,
)

data class SduiPayload(
    val analytics: SduiAnalytics,
    val theme: SduiTheme,
    val config: SduiScreenConfig,
    val components: List<SduiComponent>,
)

data class SduiAnalytics(
    val screenName: String = "",
    val tags: Map<String, String> = emptyMap(),
)

data class SduiScreenConfig(
    val orientation: String = "vertical",
    val scrollable: Boolean = true,
    val paddingTopDp: Int = 20,
    val paddingStartDp: Int = 20,
    val paddingEndDp: Int = 20,
    val paddingBottomDp: Int = 24,
    val componentSpacingDp: Int = 24,
)

// --- Tema -------------------------------------------------------------------

data class SduiTheme(
    val id: String = "default",
    val statusBarStyle: String = "light",
    val background: SduiBackground = SduiBackground(),
    val colors: SduiColors = SduiColors(),
    val typography: SduiTypography = SduiTypography(),
    val shapes: SduiShapes = SduiShapes(),
    val spacing: SduiSpacing = SduiSpacing(),
)

data class SduiBackground(
    val color: String = "#050505",
    val image: String? = null,
    val overlayColor: String? = null,
)

data class SduiColors(
    val primary: String = "#E53935",
    val primaryDark: String = "#8F1111",
    val secondary: String = "#C39A5B",
    val surface: String = "#101010",
    val surfaceVariant: String = "#171717",
    val outline: String = "#3A2525",
    val textPrimary: String = "#F5F5F2",
    val textSecondary: String = "#A8A8A5",
    val textDisabled: String = "#626262",
)

data class SduiTextStyle(
    val fontFamily: String = "sans-serif",
    val fontSizeSp: Float = 16f,
    val fontWeight: Int = 400,
    val lineHeightSp: Float = 24f,
    val letterSpacingSp: Float = 0f,
)

data class SduiTypography(
    val display: SduiTextStyle = SduiTextStyle(fontFamily = "serif", fontSizeSp = 40f, fontWeight = 500, lineHeightSp = 46f),
    val headline: SduiTextStyle = SduiTextStyle(fontFamily = "serif", fontSizeSp = 24f, fontWeight = 500, lineHeightSp = 30f),
    val title: SduiTextStyle = SduiTextStyle(fontSizeSp = 18f, fontWeight = 600, lineHeightSp = 24f),
    val body: SduiTextStyle = SduiTextStyle(),
    val label: SduiTextStyle = SduiTextStyle(fontSizeSp = 13f, fontWeight = 600, lineHeightSp = 18f, letterSpacingSp = 1.2f),
)

data class SduiShapes(
    val smallRadiusDp: Int = 12,
    val cardRadiusDp: Int = 22,
    val largeRadiusDp: Int = 28,
    val pillRadiusDp: Int = 999,
)

data class SduiSpacing(
    val screenHorizontalDp: Int = 20,
    val screenTopDp: Int = 20,
    val screenBottomDp: Int = 24,
    val sectionGapDp: Int = 24,
    val itemGapDp: Int = 12,
)

// --- Ações ------------------------------------------------------------------

sealed interface SduiAction {
    val enabled: Boolean

    data class Navigate(val route: String, override val enabled: Boolean = true) : SduiAction

    data class OpenDrawer(override val enabled: Boolean = true) : SduiAction

    /** Tipo desconhecido: o app registra e ignora, nunca quebra. */
    data class Unsupported(val type: String, override val enabled: Boolean = false) : SduiAction
}

// --- Itens compartilhados ---------------------------------------------------

data class SduiIconItem(
    val id: String,
    val title: String,
    /** Símbolo resolvido pelo registry local de ícones do Android. */
    val icon: String,
    val enabled: Boolean = true,
    val badgeCount: Int = 0,
    val action: SduiAction? = null,
)

data class SduiLink(
    val title: String,
    val icon: String? = null,
    val enabled: Boolean = true,
    val action: SduiAction? = null,
)

data class SduiCardItem(
    val id: String,
    val title: String,
    val subtitle: String? = null,
    val image: String? = null,
    val badge: String? = null,
    val enabled: Boolean = true,
    val action: SduiAction? = null,
)

data class SduiNewsItem(
    val id: String,
    val category: String,
    val title: String,
    val description: String? = null,
    val image: String? = null,
    val publishedAt: String? = null,
    val enabled: Boolean = true,
    val action: SduiAction? = null,
)

data class SduiPagination(
    val visible: Boolean = false,
    val selectedIndex: Int = 0,
    val itemCount: Int = 0,
)

// --- Componentes ------------------------------------------------------------

sealed interface SduiComponent {
    val id: String

    data class TopBar(
        override val id: String,
        val eyebrow: String? = null,
        val title: String,
        val subtitle: String? = null,
        val leading: SduiIconItem? = null,
        val trailing: SduiIconItem? = null,
    ) : SduiComponent

    data class HeroBanner(
        override val id: String,
        val eyebrow: String? = null,
        val title: String,
        val description: String? = null,
        val sideLabel: String? = null,
        val foregroundImage: String? = null,
        val backgroundImage: String? = null,
        /** `start` ou `end`: de que lado do card a arte fica. */
        val imageAlignment: String = "end",
        val button: SduiLink? = null,
        val pagination: SduiPagination = SduiPagination(),
    ) : SduiComponent

    data class IconGrid(
        override val id: String,
        val columns: Int = 5,
        val horizontalScroll: Boolean = true,
        val items: List<SduiIconItem> = emptyList(),
    ) : SduiComponent

    data class HorizontalCarousel(
        override val id: String,
        val title: String,
        val horizontalScroll: Boolean = true,
        val showSeeAll: Boolean = false,
        val seeAll: SduiLink? = null,
        val items: List<SduiCardItem> = emptyList(),
    ) : SduiComponent

    data class NewsList(
        override val id: String,
        val title: String,
        val showSeeAll: Boolean = false,
        val seeAll: SduiLink? = null,
        val maxVisibleItems: Int = Int.MAX_VALUE,
        val items: List<SduiNewsItem> = emptyList(),
    ) : SduiComponent

    data class BottomNavigation(
        override val id: String,
        val selectedItem: String? = null,
        val items: List<SduiIconItem> = emptyList(),
    ) : SduiComponent
}

/** Erros previstos do pipeline SDUI. */
sealed class SduiError(message: String) : Exception(message) {
    class Network(cause: String) : SduiError("Falha de rede: $cause")
    class EmptyScreen(screen: String, house: String) : SduiError("Nenhum payload para $screen/$house")
    class UnsupportedSchema(schemaVersion: String, layout: String) :
        SduiError("Payload não suportado: schema=$schemaVersion layout=$layout")
}
