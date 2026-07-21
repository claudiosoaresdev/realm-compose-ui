package br.com.claudiosoaresdev.realmcomposeui.buildlogic.convention

import br.com.claudiosoaresdev.realmcomposeui.buildlogic.helper.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * Stack de teste comum. Fixtures/fakes vivem no `test/` do módulo que define a interface.
 */
class RealmAndroidTestingPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        dependencies {
            add("testImplementation", libs.findLibrary("junit").get())
            add("testImplementation", libs.findLibrary("kotlinx-coroutines-test").get())
            add("androidTestImplementation", libs.findLibrary("androidx-junit").get())
            add("androidTestImplementation", libs.findLibrary("androidx-espresso-core").get())
        }
    }
}
