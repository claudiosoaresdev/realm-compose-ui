package br.com.claudiosoaresdev.realmcomposeui.core.network

import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Contrato do json-server de estudo (`npx json-server db.json`).
 *
 * O payload é dinâmico por natureza: a rede devolve mapas crus e quem tipa é o
 * [br.com.claudiosoaresdev.realmcomposeui.core.parser.SduiPayloadParser].
 */
interface SduiApi {

    @GET("appConfig")
    suspend fun appConfig(): Map<String, Any?>

    @GET("sdui")
    suspend fun screens(
        @Query("screen") screen: String,
        @Query("house") house: String,
    ): List<Map<String, Any?>>
}

object RealmNetwork {
    /** `10.0.2.2` é o host da máquina visto de dentro do emulador. */
    const val BASE_URL: String = "http://10.0.2.2:3000/"
}
