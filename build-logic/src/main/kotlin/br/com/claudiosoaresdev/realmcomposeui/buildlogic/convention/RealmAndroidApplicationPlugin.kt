package br.com.claudiosoaresdev.realmcomposeui.buildlogic.convention

import br.com.claudiosoaresdev.realmcomposeui.buildlogic.helper.RealmBuild
import br.com.claudiosoaresdev.realmcomposeui.buildlogic.helper.configureAndroidCommon
import br.com.claudiosoaresdev.realmcomposeui.buildlogic.helper.libs
import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

/**
 * Convention do módulo de aplicação. Único módulo que conhece flavors e buildTypes.
 */
class RealmAndroidApplicationPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        pluginManager.apply("com.android.application")

        extensions.configure<ApplicationExtension> {
            configureAndroidCommon(this)

            defaultConfig {
                targetSdk = RealmBuild.TARGET_SDK
                versionCode = 1
                versionName = "1.0"
                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            }

            flavorDimensions += "environment"
            productFlavors {
                create("dev") {
                    dimension = "environment"
                    applicationIdSuffix = ".dev"
                    versionNameSuffix = "-dev"
                }
                create("staging") {
                    dimension = "environment"
                    applicationIdSuffix = ".staging"
                    versionNameSuffix = "-staging"
                }
                create("prod") {
                    dimension = "environment"
                }
            }

            buildTypes {
                release {
                    optimization {
                        enable = false
                    }
                }
            }
        }

        dependencies {
            add("testImplementation", libs.findLibrary("junit").get())
            add("androidTestImplementation", libs.findLibrary("androidx-junit").get())
            add("androidTestImplementation", libs.findLibrary("androidx-espresso-core").get())
        }
    }
}
