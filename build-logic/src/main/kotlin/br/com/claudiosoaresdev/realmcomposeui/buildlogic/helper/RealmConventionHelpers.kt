package br.com.claudiosoaresdev.realmcomposeui.buildlogic.helper

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType

/**
 * Constantes de build compartilhadas. Módulo declara o que é; o "como" mora aqui.
 */
object RealmBuild {
    const val COMPILE_SDK = 37
    const val COMPILE_SDK_MINOR = 1
    const val MIN_SDK = 24
    const val TARGET_SDK = 36
    val JAVA_VERSION: JavaVersion = JavaVersion.VERSION_11
}

internal val Project.libs: VersionCatalog
    get() = extensions.getByType<VersionCatalogsExtension>().named("libs")

/**
 * Configuração Android comum a application e library.
 */
internal fun Project.configureAndroidCommon(extension: CommonExtension) {
    extension.compileSdk = RealmBuild.COMPILE_SDK
    extension.compileSdkMinor = RealmBuild.COMPILE_SDK_MINOR
    extension.defaultConfig.minSdk = RealmBuild.MIN_SDK
    extension.compileOptions.sourceCompatibility = RealmBuild.JAVA_VERSION
    extension.compileOptions.targetCompatibility = RealmBuild.JAVA_VERSION

    // Fonte Kotlin em src/<variant>/kotlin, espelhando o pacote de produção nos testes.
    extension.sourceSets.configureEach {
        java.directories.add("src/$name/kotlin")
    }
}
