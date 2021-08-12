package com.picpay.desafio.android.users.data.remote.mapper

import com.picpay.desafio.android.users.data.remote.dto.response.UserDto
import com.picpay.desafio.android.users.domain.model.User

fun List<UserDto>.toListUser() = map {
    User(
        id = it.id,
        img = it.img,
        name = it.name,
        username = it.username,
    )
}
