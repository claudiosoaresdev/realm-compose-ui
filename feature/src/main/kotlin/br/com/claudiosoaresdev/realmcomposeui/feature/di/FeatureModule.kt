package br.com.claudiosoaresdev.realmcomposeui.feature.di

import br.com.claudiosoaresdev.realmcomposeui.feature.presentation.host.SduiHostViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val featureModule = module {
    viewModelOf(::SduiHostViewModel)
}
