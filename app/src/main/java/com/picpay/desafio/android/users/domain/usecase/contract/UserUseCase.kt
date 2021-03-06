package com.picpay.desafio.android.users.domain.usecase.contract

import com.picpay.desafio.android.users.domain.model.User
import io.reactivex.Single

interface UserUseCase {
    fun getAll(): Single<List<User>>
    fun getAllLocal(): Single<List<User>>
}