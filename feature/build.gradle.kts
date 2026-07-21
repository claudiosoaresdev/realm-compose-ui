plugins {
    id("realm.android.library")
    id("realm.android.compose")
    id("realm.android.di")
    id("realm.android.testing")
}

android {
    namespace = "br.com.claudiosoaresdev.realmcomposeui.feature"
}

dependencies {
    api(projects.core)
}
