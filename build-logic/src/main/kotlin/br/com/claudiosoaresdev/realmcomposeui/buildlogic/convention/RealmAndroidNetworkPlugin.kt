package br.com.claudiosoaresdev.realmcomposeui.buildlogic.convention

import br.com.claudiosoaresdev.realmcomposeui.buildlogic.helper.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * Camada de transporte: cliente HTTP do json-server de estudo.
 */
class RealmAndroidNetworkPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        dependencies {
            add("implementation", libs.findLibrary("retrofit-core").get())
            add("implementation", libs.findLibrary("retrofit-converter-moshi").get())
            add("implementation", libs.findLibrary("okhttp-core").get())
            add("implementation", libs.findLibrary("okhttp-logging").get())
        }
    }
}
