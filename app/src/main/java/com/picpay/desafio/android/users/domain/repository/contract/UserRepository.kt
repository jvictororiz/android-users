package com.picpay.desafio.android.users.domain.repository.contract

import com.picpay.desafio.android.users.domain.model.User
import io.reactivex.Single

interface UserRepository {
    fun fetchAll(): Single<List<User>>
    fun getLocalUsers(): Single<List<User>>
}