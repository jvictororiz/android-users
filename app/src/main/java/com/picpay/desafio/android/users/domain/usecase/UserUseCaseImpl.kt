package com.picpay.desafio.android.domain.usecase

import com.picpay.desafio.android.domain.model.User
import com.picpay.desafio.android.domain.repository.contract.UserRepository
import com.picpay.desafio.android.domain.usecase.contract.UserUseCase
import io.reactivex.Single

class UserUseCaseImpl(private val repository: UserRepository) : UserUseCase {
    override fun getAll(): Single<List<User>> = repository.fetchAll()
    override fun getAllLocal(): Single<List<User>> = repository.getLocalUsers()
}