package com.picpay.desafio.android.base

import android.app.Application
import com.picpay.desafio.android.BuildConfig
import com.picpay.desafio.android.base.di.baseModule
import com.picpay.desafio.android.users.di.usersModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@BaseApplication)
            modules(baseModule)
        }
    }

    fun getBaseUrl() = BuildConfig.BASE_URL
}