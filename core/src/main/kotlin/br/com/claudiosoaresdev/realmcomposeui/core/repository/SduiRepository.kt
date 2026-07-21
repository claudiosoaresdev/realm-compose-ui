package br.com.claudiosoaresdev.realmcomposeui.core.repository

import br.com.claudiosoaresdev.realmcomposeui.core.model.SduiAppConfig
import br.com.claudiosoaresdev.realmcomposeui.core.model.SduiError
import br.com.claudiosoaresdev.realmcomposeui.core.model.SduiScreen
import br.com.claudiosoaresdev.realmcomposeui.core.network.SduiApi
import br.com.claudiosoaresdev.realmcomposeui.core.parser.SduiPayloadParser
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

interface SduiRepository {
    suspend fun appConfig(): Result<SduiAppConfig>
    suspend fun screen(screen: String, house: String): Result<SduiScreen>
}

class SduiRepositoryImpl(
    private val api: SduiApi,
    private val dispatcher: CoroutineDispatcher,
) : SduiRepository {

    private var cachedConfig: SduiAppConfig? = null

    override suspend fun appConfig(): Result<SduiAppConfig> = withContext(dispatcher) {
        runCatching { SduiPayloadParser.parseAppConfig(api.appConfig()) }
            .onSuccess { cachedConfig = it }
            .recoverNetworkError()
    }

    override suspend fun screen(screen: String, house: String): Result<SduiScreen> =
        withContext(dispatcher) {
            runCatching {
                val parsed = api.screens(screen = screen, house = house)
                    .firstNotNullOfOrNull(SduiPayloadParser::parseScreen)
                    ?: throw SduiError.EmptyScreen(screen, house)

                val config = cachedConfig
                val schemaOk = config == null || parsed.schemaVersion in config.supportedSchemaVersions
                val layoutOk = config == null || parsed.layout in config.supportedLayouts
                if (!schemaOk || !layoutOk) {
                    throw SduiError.UnsupportedSchema(parsed.schemaVersion, parsed.layout)
                }
                parsed
            }.recoverNetworkError()
        }
}

/** Exceção de transporte vira [SduiError.Network]; erro de contrato passa intacto. */
private fun <T> Result<T>.recoverNetworkError(): Result<T> = recoverCatching { throwable ->
    if (throwable is SduiError) throw throwable
    throw SduiError.Network(throwable.message ?: throwable::class.simpleName.orEmpty())
}
