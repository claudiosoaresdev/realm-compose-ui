package br.com.claudiosoaresdev.realmcomposeui.di

import br.com.claudiosoaresdev.realmcomposeui.core.di.coreModule
import br.com.claudiosoaresdev.realmcomposeui.feature.di.featureModule
import org.koin.core.module.Module

/**
 * Grafo raiz: o :app é o único que conhece todos os módulos.
 */
val appModules: List<Module> = listOf(coreModule, featureModule)
