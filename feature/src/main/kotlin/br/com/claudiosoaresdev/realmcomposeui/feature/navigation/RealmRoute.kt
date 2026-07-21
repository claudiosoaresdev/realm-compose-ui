package br.com.claudiosoaresdev.realmcomposeui.feature.navigation

/**
 * Destinos Compose do app. A rota que o servidor manda (`/map`, `/house/targaryen`…)
 * é traduzida aqui — o payload nunca conhece o grafo do Navigation.
 */
enum class RealmRoute(val route: String) {
    Home("home"),
    Houses("houses"),
    Map("map"),
    Decrees("decrees"),
    Profile("profile"),
    ;

    companion object {
        val bottomBarOrder: List<RealmRoute> = listOf(Home, Houses, Map, Decrees, Profile)

        /**
         * Mapeia a rota do payload para um destino local.
         * Rota não mapeada devolve `null` — o host registra e ignora, sem navegar.
         */
        fun fromSduiRoute(sduiRoute: String): RealmRoute? {
            val path = sduiRoute.substringBefore('?').trimEnd('/')
            return when {
                path == "/sdui" || path == "/home" -> Home
                path == "/houses" || path.startsWith("/house/") || path == "/house" -> Houses
                path == "/map" -> Map
                path == "/decrees" -> Decrees
                path == "/profile" -> Profile
                else -> null
            }
        }

        /** `/house/targaryen` → `targaryen`; `/sdui?house=x` → `x`. */
        fun houseFromSduiRoute(sduiRoute: String): String? {
            val query = sduiRoute.substringAfter('?', missingDelimiterValue = "")
            val fromQuery = query.split('&')
                .firstOrNull { it.startsWith("house=") }
                ?.removePrefix("house=")
            if (!fromQuery.isNullOrBlank()) return fromQuery

            val path = sduiRoute.substringBefore('?').trimEnd('/')
            return path.removePrefix("/house/").takeIf { it != path && it.isNotBlank() }
        }
    }
}
