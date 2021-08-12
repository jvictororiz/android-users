package com.picpay.desafio.android.domain.usecase.contract

import com.picpay.desafio.android.domain.model.User
import io.reactivex.Single

interface UserUseCase {
    fun getAll(): Single<List<User>>
    fun getAllLocal(): Single<List<User>>
}