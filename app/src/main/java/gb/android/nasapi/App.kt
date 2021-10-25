package gb.android.nasapi

import android.app.Application
import gb.android.nasapi.settings.Settings

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        private lateinit var instance: App

        val settings by lazy(LazyThreadSafetyMode.NONE) { Settings(instance) }
    }


}