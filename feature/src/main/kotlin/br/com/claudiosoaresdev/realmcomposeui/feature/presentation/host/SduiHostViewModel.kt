package br.com.claudiosoaresdev.realmcomposeui.feature.presentation.host

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.claudiosoaresdev.realmcomposeui.core.repository.SduiRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Dono do payload corrente. Todas as telas do grafo leem daqui: o tema e a barra
 * inferior são os mesmos vindos do servidor, independente do destino ativo.
 */
class SduiHostViewModel(
    private val repository: SduiRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(SduiHostState())
    val state: StateFlow<SduiHostState> = _state.asStateFlow()

    private var screenName: String = DEFAULT_SCREEN

    init {
        bootstrap()
    }

    fun onEvent(event: SduiHostEvent) {
        when (event) {
            SduiHostEvent.Retry -> bootstrap()
            is SduiHostEvent.SelectHouse -> loadScreen(event.house)
        }
    }

    private fun bootstrap() {
        viewModelScope.launch {
            _state.update { it.copy(loading = true, error = null) }
            val config = repository.appConfig().getOrNull()
            screenName = config?.defaultScreen ?: DEFAULT_SCREEN
            val house = _state.value.house.ifBlank { config?.defaultHouse ?: DEFAULT_HOUSE }
            loadScreen(house)
        }
    }

    private fun loadScreen(house: String) {
        viewModelScope.launch {
            _state.update { it.copy(loading = true, error = null, house = house) }
            repository.screen(screen = screenName, house = house)
                .onSuccess { screen ->
                    _state.update { it.copy(loading = false, screen = screen, error = null) }
                }
                .onFailure { throwable ->
                    _state.update { it.copy(loading = false, error = throwable.message) }
                }
        }
    }

    private companion object {
        const val DEFAULT_SCREEN = "house_home"
        const val DEFAULT_HOUSE = "targaryen"
    }
}
