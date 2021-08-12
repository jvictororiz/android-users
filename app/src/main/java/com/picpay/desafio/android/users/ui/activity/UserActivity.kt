package com.picpay.desafio.android.users.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.picpay.desafio.android.R
import com.picpay.desafio.android.users.di.usersModule
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules

class UserActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users)
        loadKoinModules(usersModule)
    }

    override fun onDestroy() {
        unloadKoinModules(usersModule)
        super.onDestroy()
    }
}