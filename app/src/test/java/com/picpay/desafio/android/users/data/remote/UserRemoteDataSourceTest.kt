package com.picpay.desafio.android.unit.users.data.remote

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.picpay.desafio.android.data.remote.dto.response.UserDto
import com.picpay.desafio.android.data.remote.service.UserService
import com.picpay.desafio.android.data.local.entity.UserDB
import com.picpay.desafio.android.data.remote.UserRemoteDataSourceImpl
import com.picpay.desafio.android.domain.exception.DefaultException
import com.picpay.desafio.android.domain.model.User
import io.reactivex.Single
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class UserRemoteDataSourceTest {
    private val userService: UserService = mock()
    private val remoteDataSource = UserRemoteDataSourceImpl(userService)

    @Test
    fun `when call getAllUserDto with success then return service users`() {
        val expected = mockUsersDtoResponse()
        whenever(userService.getUsers()).then { Single.just(expected) }

        remoteDataSource.getAllUserDto()
            .test()
            .assertNoErrors()
            .assertValue(expected)
            .assertOf { verify(userService).getUsers() }
            .assertComplete()
            .dispose()
    }

    @Test
    fun `when call getAllUserDto with error then not return users`() {
        val expected = "Erro"
        whenever(userService.getUsers()).then { Single.error<List<UserDB>>(Exception(expected)) }

        remoteDataSource.getAllUserDto()
            .test()
            .assertError(DefaultException(expected))
            .assertNotComplete()
            .assertOf { verify(userService).getUsers() }
            .dispose()
    }

    private fun mockUsersResponse() = listOf(User("img", "name", 1, "username"))
    private fun mockUsersDtoResponse() = listOf(UserDto("img", "name", 1, "username"))
}