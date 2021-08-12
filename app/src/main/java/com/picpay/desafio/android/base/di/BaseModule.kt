package com.picpay.desafio.android.base.di

import androidx.room.Room
import com.picpay.desafio.android.BuildConfig
import com.picpay.desafio.android.base.BaseApplication
import com.picpay.desafio.android.base.di.builders.AppDatabase
import com.picpay.desafio.android.base.di.builders.ResourceManager
import com.picpay.desafio.android.base.di.builders.buildRetrofit
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module


val baseModule = module {
    factory { ResourceManager(context = get()) }
    factory { buildRetrofit((androidApplication() as BaseApplication).getBaseUrl()) }
    factory {
        Room.databaseBuilder(
            androidApplication(),
            AppDatabase::class.java,
            BuildConfig.DATABASE_NAME
        ).build()
    }
}