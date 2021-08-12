package com.picpay.desafio.android.users.domain.repository

import com.picpay.desafio.android.users.data.local.contract.UserLocalDataSource
import com.picpay.desafio.android.users.data.local.mapperExt.toListUser
import com.picpay.desafio.android.users.data.local.mapperExt.toListUserDB
import com.picpay.desafio.android.users.data.remote.contract.UserRemoteDataSource
import com.picpay.desafio.android.users.data.remote.mapper.toListUser
import com.picpay.desafio.android.users.domain.model.User
import com.picpay.desafio.android.users.domain.repository.contract.UserRepository
import io.reactivex.Single

class UserRepositoryImpl(
    private val localDataSource: UserLocalDataSource,
    private val remoteDataSource: UserRemoteDataSource
) : UserRepository {
    override fun fetchAll(): Single<List<User>> {
        return remoteDataSource.getAllUserDto()
            .map {
                it.toListUser()
            }.doOnSuccess {
                localDataSource.saveLocalUsers(it.toListUserDB()).subscribe()
            }
    }

    override fun getLocalUsers() = localDataSource.getLocalUsers().map { it.toListUser() }

}