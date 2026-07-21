package br.com.claudiosoaresdev.realmcomposeui.core.parser

import br.com.claudiosoaresdev.realmcomposeui.core.model.SduiAction
import br.com.claudiosoaresdev.realmcomposeui.core.model.SduiComponent
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class SduiPayloadParserTest {

    @Test
    fun `componente de tipo desconhecido e descartado`() {
        // Arrange
        val raw = mapOf<String, Any?>("id" to "x", "type" to "hologram", "props" to emptyMap<String, Any?>())

        // Act
        val component = SduiPayloadParser.parseComponent(raw)

        // Assert
        assertNull(component)
    }

    @Test
    fun `componente invisivel nao entra na arvore`() {
        // Arrange
        val raw = mapOf<String, Any?>(
            "id" to "hero",
            "type" to "heroBanner",
            "visible" to false,
            "props" to mapOf<String, Any?>("title" to "A CASA DO DRAGÃO"),
        )

        // Act
        val component = SduiPayloadParser.parseComponent(raw)

        // Assert
        assertNull(component)
    }

    @Test
    fun `campo obrigatorio ausente descarta apenas o componente`() {
        // Arrange — heroBanner sem `title`
        val raw = mapOf<String, Any?>(
            "id" to "hero",
            "type" to "heroBanner",
            "props" to mapOf<String, Any?>("eyebrow" to "A CASA DO DRAGÃO"),
        )

        // Act
        val component = SduiPayloadParser.parseComponent(raw)

        // Assert
        assertNull(component)
    }

    @Test
    fun `bottomNavigation preserva ordem e estado desabilitado`() {
        // Arrange
        val raw = mapOf<String, Any?>(
            "id" to "bottom-navigation",
            "type" to "bottomNavigation",
            "props" to mapOf<String, Any?>(
                "selectedItem" to "home",
                "items" to listOf(
                    mapOf<String, Any?>(
                        "id" to "home",
                        "title" to "INÍCIO",
                        "icon" to "home",
                        "enabled" to true,
                        "action" to mapOf<String, Any?>(
                            "type" to "navigate",
                            "route" to "/sdui?house=targaryen",
                            "enabled" to true,
                        ),
                    ),
                    mapOf<String, Any?>(
                        "id" to "decrees",
                        "title" to "DECRETOS",
                        "icon" to "description",
                        "enabled" to false,
                        "action" to mapOf<String, Any?>(
                            "type" to "navigate",
                            "route" to "/decrees",
                            "enabled" to false,
                        ),
                    ),
                ),
            ),
        )

        // Act
        val component = SduiPayloadParser.parseComponent(raw) as SduiComponent.BottomNavigation

        // Assert
        assertEquals(listOf("home", "decrees"), component.items.map { it.id })
        assertEquals("home", component.selectedItem)
        assertTrue(component.items.first().enabled)
        assertEquals(false, component.items.last().enabled)
    }

    @Test
    fun `acao de tipo novo vira Unsupported em vez de excecao`() {
        // Arrange
        val raw = mapOf<String, Any?>("type" to "share_raven", "enabled" to true)

        // Act
        val action = SduiPayloadParser.parseAction(raw)

        // Assert
        assertEquals(SduiAction.Unsupported("share_raven"), action)
    }

    @Test
    fun `tema ausente cai no fallback do design system`() {
        // Arrange
        val payload = mapOf<String, Any?>("components" to emptyList<Map<String, Any?>>())

        // Act
        val parsed = SduiPayloadParser.parsePayload(payload)

        // Assert
        assertEquals("default", parsed.theme.id)
        assertTrue(parsed.components.isEmpty())
    }

    @Test
    fun `numeros do json chegam como Number e viram Int e Float`() {
        // Arrange — Moshi entrega números como Double
        val theme = mapOf<String, Any?>(
            "shapes" to mapOf<String, Any?>("cardRadiusDp" to 22.0),
            "typography" to mapOf<String, Any?>(
                "body" to mapOf<String, Any?>("fontSizeSp" to 16.0, "fontWeight" to 400.0),
            ),
        )

        // Act
        val parsed = SduiPayloadParser.parseTheme(theme)

        // Assert
        assertEquals(22, parsed.shapes.cardRadiusDp)
        assertEquals(16f, parsed.typography.body.fontSizeSp, 0.001f)
        assertEquals(400, parsed.typography.body.fontWeight)
    }
}
