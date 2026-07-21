package br.com.claudiosoaresdev.realmcomposeui.core.parser

import br.com.claudiosoaresdev.realmcomposeui.core.model.SduiAction
import br.com.claudiosoaresdev.realmcomposeui.core.model.SduiAnalytics
import br.com.claudiosoaresdev.realmcomposeui.core.model.SduiAppConfig
import br.com.claudiosoaresdev.realmcomposeui.core.model.SduiBackground
import br.com.claudiosoaresdev.realmcomposeui.core.model.SduiCardItem
import br.com.claudiosoaresdev.realmcomposeui.core.model.SduiColors
import br.com.claudiosoaresdev.realmcomposeui.core.model.SduiComponent
import br.com.claudiosoaresdev.realmcomposeui.core.model.SduiIconItem
import br.com.claudiosoaresdev.realmcomposeui.core.model.SduiLink
import br.com.claudiosoaresdev.realmcomposeui.core.model.SduiNewsItem
import br.com.claudiosoaresdev.realmcomposeui.core.model.SduiPagination
import br.com.claudiosoaresdev.realmcomposeui.core.model.SduiPayload
import br.com.claudiosoaresdev.realmcomposeui.core.model.SduiScreen
import br.com.claudiosoaresdev.realmcomposeui.core.model.SduiScreenConfig
import br.com.claudiosoaresdev.realmcomposeui.core.model.SduiShapes
import br.com.claudiosoaresdev.realmcomposeui.core.model.SduiSpacing
import br.com.claudiosoaresdev.realmcomposeui.core.model.SduiTextStyle
import br.com.claudiosoaresdev.realmcomposeui.core.model.SduiTheme
import br.com.claudiosoaresdev.realmcomposeui.core.model.SduiTypography

/**
 * JSON já desserializado (mapas) → modelo tipado. Puro e testável fora do Compose.
 *
 * Invariantes:
 * - `type` desconhecido é descartado; o resto da tela continua.
 * - campo obrigatório ausente descarta só o componente;
 * - campo opcional ausente cai no default do modelo.
 */
object SduiPayloadParser {

    fun parseAppConfig(raw: Map<String, Any?>): SduiAppConfig = SduiAppConfig(
        appName = raw.str("appName") ?: "",
        defaultScreen = raw.str("defaultScreen") ?: "house_home",
        defaultHouse = raw.str("defaultHouse") ?: "",
        supportedSchemaVersions = raw.strList("supportedSchemaVersions"),
        supportedLayouts = raw.strList("supportedLayouts"),
    )

    fun parseScreen(raw: Map<String, Any?>): SduiScreen? {
        val id = raw.str("id") ?: return null
        val payload = raw.obj("payload") ?: return null
        return SduiScreen(
            id = id,
            screen = raw.str("screen") ?: return null,
            house = raw.str("house") ?: "",
            version = raw.int("version") ?: 1,
            schemaVersion = raw.str("schemaVersion") ?: "",
            layout = raw.str("layout") ?: "",
            payload = parsePayload(payload),
        )
    }

    fun parsePayload(raw: Map<String, Any?>): SduiPayload = SduiPayload(
        analytics = parseAnalytics(raw.obj("analytics")),
        theme = parseTheme(raw.obj("theme")),
        config = parseScreenConfig(raw.obj("screen")),
        components = raw.objList("components").mapNotNull(::parseComponent),
    )

    private fun parseAnalytics(raw: Map<String, Any?>?): SduiAnalytics {
        if (raw == null) return SduiAnalytics()
        val tags = raw.obj("tags").orEmpty()
            .mapNotNull { (key, value) -> (value as? String)?.let { key to it } }
            .toMap()
        return SduiAnalytics(screenName = raw.str("screenName") ?: "", tags = tags)
    }

    private fun parseScreenConfig(raw: Map<String, Any?>?): SduiScreenConfig {
        if (raw == null) return SduiScreenConfig()
        val padding = raw.obj("contentPadding")
        val default = SduiScreenConfig()
        return SduiScreenConfig(
            orientation = raw.str("orientation") ?: default.orientation,
            scrollable = raw.bool("scrollable") ?: default.scrollable,
            paddingTopDp = padding?.int("topDp") ?: default.paddingTopDp,
            paddingStartDp = padding?.int("startDp") ?: default.paddingStartDp,
            paddingEndDp = padding?.int("endDp") ?: default.paddingEndDp,
            paddingBottomDp = padding?.int("bottomDp") ?: default.paddingBottomDp,
            componentSpacingDp = raw.int("componentSpacingDp") ?: default.componentSpacingDp,
        )
    }

    fun parseTheme(raw: Map<String, Any?>?): SduiTheme {
        if (raw == null) return SduiTheme()
        val default = SduiTheme()
        return SduiTheme(
            id = raw.str("id") ?: default.id,
            statusBarStyle = raw.str("statusBarStyle") ?: default.statusBarStyle,
            background = raw.obj("background")?.let { bg ->
                SduiBackground(
                    color = bg.str("color") ?: default.background.color,
                    image = bg.str("image"),
                    overlayColor = bg.str("overlayColor"),
                )
            } ?: default.background,
            colors = raw.obj("colors")?.let { c ->
                val d = default.colors
                SduiColors(
                    primary = c.str("primary") ?: d.primary,
                    primaryDark = c.str("primaryDark") ?: d.primaryDark,
                    secondary = c.str("secondary") ?: d.secondary,
                    surface = c.str("surface") ?: d.surface,
                    surfaceVariant = c.str("surfaceVariant") ?: d.surfaceVariant,
                    outline = c.str("outline") ?: d.outline,
                    textPrimary = c.str("textPrimary") ?: d.textPrimary,
                    textSecondary = c.str("textSecondary") ?: d.textSecondary,
                    textDisabled = c.str("textDisabled") ?: d.textDisabled,
                )
            } ?: default.colors,
            typography = raw.obj("typography")?.let { t ->
                val d = default.typography
                SduiTypography(
                    display = parseTextStyle(t.obj("display"), d.display),
                    headline = parseTextStyle(t.obj("headline"), d.headline),
                    title = parseTextStyle(t.obj("title"), d.title),
                    body = parseTextStyle(t.obj("body"), d.body),
                    label = parseTextStyle(t.obj("label"), d.label),
                )
            } ?: default.typography,
            shapes = raw.obj("shapes")?.let { s ->
                val d = default.shapes
                SduiShapes(
                    smallRadiusDp = s.int("smallRadiusDp") ?: d.smallRadiusDp,
                    cardRadiusDp = s.int("cardRadiusDp") ?: d.cardRadiusDp,
                    largeRadiusDp = s.int("largeRadiusDp") ?: d.largeRadiusDp,
                    pillRadiusDp = s.int("pillRadiusDp") ?: d.pillRadiusDp,
                )
            } ?: default.shapes,
            spacing = raw.obj("spacing")?.let { s ->
                val d = default.spacing
                SduiSpacing(
                    screenHorizontalDp = s.int("screenHorizontalDp") ?: d.screenHorizontalDp,
                    screenTopDp = s.int("screenTopDp") ?: d.screenTopDp,
                    screenBottomDp = s.int("screenBottomDp") ?: d.screenBottomDp,
                    sectionGapDp = s.int("sectionGapDp") ?: d.sectionGapDp,
                    itemGapDp = s.int("itemGapDp") ?: d.itemGapDp,
                )
            } ?: default.spacing,
        )
    }

    private fun parseTextStyle(raw: Map<String, Any?>?, default: SduiTextStyle): SduiTextStyle {
        if (raw == null) return default
        return SduiTextStyle(
            fontFamily = raw.str("fontFamily") ?: default.fontFamily,
            fontSizeSp = raw.float("fontSizeSp") ?: default.fontSizeSp,
            fontWeight = raw.int("fontWeight") ?: default.fontWeight,
            lineHeightSp = raw.float("lineHeightSp") ?: default.lineHeightSp,
            letterSpacingSp = raw.float("letterSpacingSp") ?: default.letterSpacingSp,
        )
    }

    fun parseComponent(raw: Map<String, Any?>): SduiComponent? {
        if (raw.bool("visible") == false) return null
        val id = raw.str("id") ?: return null
        val props = raw.obj("props").orEmpty()
        return when (raw.str("type")) {
            "topBar" -> SduiComponent.TopBar(
                id = id,
                eyebrow = props.str("eyebrow"),
                title = props.str("title") ?: return null,
                subtitle = props.str("subtitle"),
                leading = props.obj("leading")?.let { parseIconItem(it, fallbackId = "$id-leading") },
                trailing = props.obj("trailing")?.let { parseIconItem(it, fallbackId = "$id-trailing") },
            )

            "heroBanner" -> SduiComponent.HeroBanner(
                id = id,
                eyebrow = props.str("eyebrow"),
                title = props.str("title") ?: return null,
                description = props.str("description"),
                sideLabel = props.str("sideLabel"),
                foregroundImage = props.str("foregroundImage"),
                backgroundImage = props.str("backgroundImage"),
                imageAlignment = props.str("imageAlignment") ?: "end",
                button = props.obj("button")?.let(::parseLink),
                pagination = props.obj("pagination")?.let {
                    SduiPagination(
                        visible = it.bool("visible") ?: false,
                        selectedIndex = it.int("selectedIndex") ?: 0,
                        itemCount = it.int("itemCount") ?: 0,
                    )
                } ?: SduiPagination(),
            )

            "iconGrid" -> SduiComponent.IconGrid(
                id = id,
                columns = props.int("columns") ?: 5,
                horizontalScroll = props.bool("horizontalScroll") ?: true,
                items = props.objList("items").mapNotNull { parseIconItem(it, fallbackId = null) },
            )

            "horizontalCarousel" -> SduiComponent.HorizontalCarousel(
                id = id,
                title = props.str("title") ?: return null,
                horizontalScroll = props.bool("horizontalScroll") ?: true,
                showSeeAll = props.bool("showSeeAll") ?: false,
                seeAll = props.obj("seeAll")?.let(::parseLink),
                items = props.objList("items").mapNotNull(::parseCardItem),
            )

            "newsList" -> SduiComponent.NewsList(
                id = id,
                title = props.str("title") ?: return null,
                showSeeAll = props.bool("showSeeAll") ?: false,
                seeAll = props.obj("seeAll")?.let(::parseLink),
                maxVisibleItems = props.int("maxVisibleItems") ?: Int.MAX_VALUE,
                items = props.objList("items").mapNotNull(::parseNewsItem),
            )

            "bottomNavigation" -> SduiComponent.BottomNavigation(
                id = id,
                selectedItem = props.str("selectedItem"),
                items = props.objList("items").mapNotNull { parseIconItem(it, fallbackId = null) },
            )

            else -> null
        }
    }

    fun parseAction(raw: Map<String, Any?>?): SduiAction? {
        if (raw == null) return null
        val enabled = raw.bool("enabled") ?: true
        return when (val type = raw.str("type")) {
            "navigate" -> raw.str("route")?.let { SduiAction.Navigate(it, enabled) }
            "open_drawer" -> SduiAction.OpenDrawer(enabled)
            null -> null
            else -> SduiAction.Unsupported(type)
        }
    }

    private fun parseIconItem(raw: Map<String, Any?>, fallbackId: String?): SduiIconItem? {
        val id = raw.str("id") ?: fallbackId ?: return null
        return SduiIconItem(
            id = id,
            title = raw.str("title") ?: raw.str("contentDescription") ?: "",
            icon = raw.str("icon") ?: return null,
            enabled = raw.bool("enabled") ?: true,
            badgeCount = raw.int("badgeCount") ?: 0,
            action = parseAction(raw.obj("action")),
        )
    }

    private fun parseLink(raw: Map<String, Any?>): SduiLink? {
        val title = raw.str("title") ?: return null
        return SduiLink(
            title = title,
            icon = raw.str("icon"),
            enabled = raw.bool("enabled") ?: true,
            action = parseAction(raw.obj("action")),
        )
    }

    private fun parseCardItem(raw: Map<String, Any?>): SduiCardItem? {
        val id = raw.str("id") ?: return null
        return SduiCardItem(
            id = id,
            title = raw.str("title") ?: return null,
            subtitle = raw.str("subtitle"),
            image = raw.str("image"),
            badge = raw.str("badge"),
            enabled = raw.bool("enabled") ?: true,
            action = parseAction(raw.obj("action")),
        )
    }

    private fun parseNewsItem(raw: Map<String, Any?>): SduiNewsItem? {
        val id = raw.str("id") ?: return null
        return SduiNewsItem(
            id = id,
            category = raw.str("category") ?: "",
            title = raw.str("title") ?: return null,
            description = raw.str("description"),
            image = raw.str("image"),
            publishedAt = raw.str("publishedAt"),
            enabled = raw.bool("enabled") ?: true,
            action = parseAction(raw.obj("action")),
        )
    }
}

// --- Acesso tolerante ao mapa cru -------------------------------------------

private fun Map<String, Any?>.str(key: String): String? = this[key] as? String

private fun Map<String, Any?>.bool(key: String): Boolean? = this[key] as? Boolean

private fun Map<String, Any?>.int(key: String): Int? = (this[key] as? Number)?.toInt()

private fun Map<String, Any?>.float(key: String): Float? = (this[key] as? Number)?.toFloat()

@Suppress("UNCHECKED_CAST")
private fun Map<String, Any?>.obj(key: String): Map<String, Any?>? = this[key] as? Map<String, Any?>

@Suppress("UNCHECKED_CAST")
private fun Map<String, Any?>.objList(key: String): List<Map<String, Any?>> =
    (this[key] as? List<*>)?.mapNotNull { it as? Map<String, Any?> } ?: emptyList()

private fun Map<String, Any?>.strList(key: String): List<String> =
    (this[key] as? List<*>)?.filterIsInstance<String>() ?: emptyList()
