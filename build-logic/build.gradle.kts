plugins {
    `kotlin-dsl`
}

group = "br.com.claudiosoaresdev.realmcomposeui.buildlogic"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.compose.compiler.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "realm.android.application"
            implementationClass = "br.com.claudiosoaresdev.realmcomposeui.buildlogic.convention.RealmAndroidApplicationPlugin"
        }
        register("androidLibrary") {
            id = "realm.android.library"
            implementationClass = "br.com.claudiosoaresdev.realmcomposeui.buildlogic.convention.RealmAndroidLibraryPlugin"
        }
        register("kotlinLibrary") {
            id = "realm.kotlin.library"
            implementationClass = "br.com.claudiosoaresdev.realmcomposeui.buildlogic.convention.RealmKotlinLibraryPlugin"
        }
        register("androidCompose") {
            id = "realm.android.compose"
            implementationClass = "br.com.claudiosoaresdev.realmcomposeui.buildlogic.convention.RealmAndroidComposePlugin"
        }
        register("androidNetwork") {
            id = "realm.android.network"
            implementationClass = "br.com.claudiosoaresdev.realmcomposeui.buildlogic.convention.RealmAndroidNetworkPlugin"
        }
        register("androidDi") {
            id = "realm.android.di"
            implementationClass = "br.com.claudiosoaresdev.realmcomposeui.buildlogic.convention.RealmAndroidDiPlugin"
        }
        register("androidTesting") {
            id = "realm.android.testing"
            implementationClass = "br.com.claudiosoaresdev.realmcomposeui.buildlogic.convention.RealmAndroidTestingPlugin"
        }
    }
}
