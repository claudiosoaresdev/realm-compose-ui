plugins {
    id("realm.android.application")
    id("realm.android.compose")
    id("realm.android.network")
    id("realm.android.di")
    id("realm.android.testing")
}

android {
    namespace = "br.com.claudiosoaresdev.realmcomposeui"

    defaultConfig {
        applicationId = "br.com.claudiosoaresdev.realmcomposeui"
    }
}

dependencies {
    implementation(projects.core)
    implementation(projects.feature)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)
    implementation(libs.kotlinx.coroutines.android)
}
