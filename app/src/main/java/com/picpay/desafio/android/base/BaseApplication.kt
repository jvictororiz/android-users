package com.picpay.desafio.android

import android.app.Application
import com.picpay.desafio.android.di.userModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

open class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@BaseApplication)
            modules(userModules)
        }
    }

    open fun getBaseUrl() = BuildConfig.BASE_URL
}

fun <T> T.makeIf(add: Boolean, block: (T) -> T): T {
    return if (add) {
        block(this)
    } else {
        this
    }
}