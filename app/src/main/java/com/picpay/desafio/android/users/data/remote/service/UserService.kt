package com.picpay.desafio.android.users.data.remote.service

import com.picpay.desafio.android.users.data.remote.dto.response.UserDto
import io.reactivex.Single
import retrofit2.http.GET

interface UserService {
    @GET("users")
    fun getUsers(): Single<List<UserDto>>
}