package br.com.claudiosoaresdev.realmcomposeui.buildlogic.convention

import br.com.claudiosoaresdev.realmcomposeui.buildlogic.helper.RealmBuild
import br.com.claudiosoaresdev.realmcomposeui.buildlogic.helper.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

/**
 * Convention JVM pura — usada por `domain` e `core:model`.
 * Ausência do plugin Android garante por compilação que regra de negócio não toca framework.
 */
class RealmKotlinLibraryPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        pluginManager.apply("org.jetbrains.kotlin.jvm")

        extensions.configure<JavaPluginExtension> {
            sourceCompatibility = RealmBuild.JAVA_VERSION
            targetCompatibility = RealmBuild.JAVA_VERSION
        }

        dependencies {
            add("implementation", libs.findLibrary("kotlinx-coroutines-core").get())
            add("testImplementation", libs.findLibrary("junit").get())
            add("testImplementation", libs.findLibrary("kotlinx-coroutines-test").get())
        }
    }
}
