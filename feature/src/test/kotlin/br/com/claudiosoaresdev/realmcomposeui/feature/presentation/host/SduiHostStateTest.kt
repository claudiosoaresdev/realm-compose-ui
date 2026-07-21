package br.com.claudiosoaresdev.realmcomposeui.feature.presentation.host

import org.junit.Assert.assertEquals
import org.junit.Test

class SduiHostStateTest {

    @Test
    fun `proxima casa alterna targaryen para hightower`() {
        // Arrange
        val state = SduiHostState(house = "targaryen")

        // Act
        val next = state.nextHouse

        // Assert
        assertEquals("hightower", next)
    }

    @Test
    fun `proxima casa volta ao inicio depois da ultima`() {
        // Arrange
        val state = SduiHostState(house = "hightower")

        // Act
        val next = state.nextHouse

        // Assert
        assertEquals("targaryen", next)
    }

    @Test
    fun `sem casa ativa o rodizio comeca pela primeira`() {
        // Arrange
        val state = SduiHostState(house = "")

        // Act
        val next = state.nextHouse

        // Assert
        assertEquals("targaryen", next)
    }
}
