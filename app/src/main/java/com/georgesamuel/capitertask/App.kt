package com.georgesamuel.capitertask

import android.app.Application
import com.georgesamuel.capitertask.di.*

class App: Application(), Injector {

    private lateinit var appComponent: AppComponent


    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(applicationContext))
            .networkModule(NetworkModule(BuildConfig.BASE_URL, BuildConfig.API_KEY))
            .appRepositoryModule(AppRepositoryModule())
            .build()
    }

    override fun createAppSubComponent(): AppSubComponent {
        return appComponent.appSubComponent().create()
    }
}