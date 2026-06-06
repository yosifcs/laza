package com.example.laza

import android.app.Application
import com.example.laza.di.networkModule
import com.example.laza.di.repositoryModule
import com.example.laza.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class LazaApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@LazaApplication)  // gives Koin access to Android context
            modules(
                networkModule,
                repositoryModule,
                viewModelModule
            )                     // register your module
        }
    }
}