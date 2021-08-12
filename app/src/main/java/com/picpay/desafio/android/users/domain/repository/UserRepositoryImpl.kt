package com.picpay.desafio.android.domain.repository

import com.picpay.desafio.android.data.local.contract.UserLocalDataSource
import com.picpay.desafio.android.unit.users.data.local.mapperExt.toListUser
import com.picpay.desafio.android.unit.users.data.local.mapperExt.toListUserDB
import com.picpay.desafio.android.data.remote.contract.UserRemoteDataSource
import com.picpay.desafio.android.data.remote.mapper.toListUser
import com.picpay.desafio.android.domain.model.User
import com.picpay.desafio.android.domain.repository.contract.UserRepository
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