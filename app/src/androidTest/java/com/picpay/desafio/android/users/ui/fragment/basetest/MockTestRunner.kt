package com.picpay.desafio.android.users.ui.fragment.config

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.os.StrictMode
import androidx.room.Room
import androidx.test.runner.AndroidJUnitRunner
import com.picpay.desafio.android.base.AppDatabaseConfig
import com.picpay.desafio.android.base.ResourceManager
import com.picpay.desafio.android.base.buildRetrofit
import com.picpay.desafio.android.users.data.local.UserLocalDataSourceImpl
import com.picpay.desafio.android.users.data.local.contract.UserLocalDataSource
import com.picpay.desafio.android.users.data.remote.UserRemoteDataSourceImpl
import com.picpay.desafio.android.users.data.remote.contract.UserRemoteDataSource
import com.picpay.desafio.android.users.data.remote.service.UserService
import com.picpay.desafio.android.users.domain.repository.UserRepositoryImpl
import com.picpay.desafio.android.users.domain.repository.contract.UserRepository
import com.picpay.desafio.android.users.domain.usecase.UserUseCaseImpl
import com.picpay.desafio.android.users.domain.usecase.contract.UserUseCase
import com.picpay.desafio.android.users.viewmodel.UserViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import retrofit2.Retrofit

class MockTestRunner : AndroidJUnitRunner() {
    override fun onCreate(arguments: Bundle?) {
        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder().permitAll().build())
        super.onCreate(arguments)
    }

    override fun newApplication(
        cl: ClassLoader?,
        className: String?,
        context: Context?
    ): Application {
        return super.newApplication(cl, BaseApplicationTest::class.java.name, context)
    }
}

class BaseApplicationTest : Application(), KoinTest {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@BaseApplicationTest)
            modules(mockModule)
        }
    }

    fun getBaseUrl(): String {
        return "http://127.0.0.1:8080"
    }
}

val mockModule = module {
    viewModel {
        UserViewModel(
            userUseCase = get(),
            resource = get()
        )
    }
    factory<UserUseCase> { UserUseCaseImpl(repository = get()) }
    factory<UserRepository> {
        UserRepositoryImpl(
            localDataSource = get(),
            remoteDataSource = get()
        )
    }
    factory<UserLocalDataSource> {
        UserLocalDataSourceImpl(userDao = get())
    }
    factory<UserRemoteDataSource> {
        UserRemoteDataSourceImpl(service = get())
    }

    factory { ResourceManager(context = get()) }

    factory { buildRetrofit((androidApplication() as BaseApplicationTest).getBaseUrl()) }
    factory<UserService> { get<Retrofit>().create(UserService::class.java) }

    factory {
        Room.databaseBuilder(
            androidContext(),
            AppDatabaseConfig::class.java,
            "DATABASE_TEST"
        ).allowMainThreadQueries().build()
    }

    factory {
        get<AppDatabaseConfig>().userDao()
    }
}