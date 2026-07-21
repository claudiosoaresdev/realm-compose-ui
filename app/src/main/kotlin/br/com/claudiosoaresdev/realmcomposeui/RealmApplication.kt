package br.com.claudiosoaresdev.realmcomposeui

import android.app.Application
import br.com.claudiosoaresdev.realmcomposeui.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class RealmApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@RealmApplication)
            modules(appModules)
        }
    }
}
