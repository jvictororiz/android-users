package com.picpay.desafio.android.base.di.builders

import android.content.Context
import androidx.annotation.StringRes
class ResourceManager(private val context: Context) {
    fun message(@StringRes idString: Int) = context.getString(idString)
}