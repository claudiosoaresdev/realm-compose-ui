package br.com.claudiosoaresdev.realmcomposeui.feature.navigation

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class RealmRouteTest {

    @Test
    fun `rota sdui com query vira destino Home`() {
        // Arrange
        val sduiRoute = "/sdui?house=targaryen"

        // Act
        val route = RealmRoute.fromSduiRoute(sduiRoute)

        // Assert
        assertEquals(RealmRoute.Home, route)
    }

    @Test
    fun `rota de casa especifica vira destino Houses`() {
        // Arrange
        val sduiRoute = "/house/hightower"

        // Act
        val route = RealmRoute.fromSduiRoute(sduiRoute)

        // Assert
        assertEquals(RealmRoute.Houses, route)
    }

    @Test
    fun `rota sem destino local devolve null em vez de navegar errado`() {
        // Arrange
        val sduiRoute = "/characters/daemon-targaryen"

        // Act
        val route = RealmRoute.fromSduiRoute(sduiRoute)

        // Assert
        assertNull(route)
    }

    @Test
    fun `casa e extraida da query e do path`() {
        // Arrange
        val fromQuery = "/sdui?house=hightower"
        val fromPath = "/house/targaryen"

        // Act
        val queryHouse = RealmRoute.houseFromSduiRoute(fromQuery)
        val pathHouse = RealmRoute.houseFromSduiRoute(fromPath)

        // Assert
        assertEquals("hightower", queryHouse)
        assertEquals("targaryen", pathHouse)
    }

    @Test
    fun `rota sem casa devolve null`() {
        // Arrange
        val sduiRoute = "/map"

        // Act
        val house = RealmRoute.houseFromSduiRoute(sduiRoute)

        // Assert
        assertNull(house)
    }
}
