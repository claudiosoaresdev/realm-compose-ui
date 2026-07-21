package br.com.claudiosoaresdev.realmcomposeui.buildlogic.convention

import br.com.claudiosoaresdev.realmcomposeui.buildlogic.helper.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * Injeção de dependência com Koin.
 */
class RealmAndroidDiPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        dependencies {
            add("implementation", libs.findLibrary("koin-core").get())
            add("implementation", libs.findLibrary("koin-android").get())
            add("implementation", libs.findLibrary("koin-compose").get())
            add("implementation", libs.findLibrary("koin-compose-viewmodel").get())
        }
    }
}
