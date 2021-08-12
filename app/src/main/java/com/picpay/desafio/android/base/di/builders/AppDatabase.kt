package com.picpay.desafio.android.base.di.builders

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.picpay.desafio.android.BuildConfig
import com.picpay.desafio.android.users.data.local.dao.UserDao
import com.picpay.desafio.android.users.data.local.entity.UserDB

class AppDatabase(private val applicationContext: Context) {
    fun create() = Room.databaseBuilder(
        applicationContext,
        AppDatabaseConfig::class.java,
        BuildConfig.DATABASE_NAME
    ).build()
}

@Database(entities = [UserDB::class], version = 1)
abstract class AppDatabaseConfig : RoomDatabase() {
    abstract fun userDao(): UserDao
}
