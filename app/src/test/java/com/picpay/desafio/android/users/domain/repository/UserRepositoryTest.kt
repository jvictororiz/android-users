package com.picpay.desafio.android.users.domain.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import com.nhaarman.mockitokotlin2.whenever
import com.picpay.desafio.android.users.data.remote.dto.response.UserDto
import com.picpay.desafio.android.users.data.local.contract.UserLocalDataSource
import com.picpay.desafio.android.users.data.local.entity.UserDB
import com.picpay.desafio.android.users.data.remote.contract.UserRemoteDataSource
import com.picpay.desafio.android.users.domain.exception.DefaultException
import com.picpay.desafio.android.users.domain.model.User
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class UserRepositoryTest {
    private val localDataSource: UserLocalDataSource = mock()
    private val remoteDataSource: UserRemoteDataSource = mock()
    private val repository = UserRepositoryImpl(localDataSource, remoteDataSource)

    @Rule
    @JvmField
    val schedulers = InstantTaskExecutorRule()

    @Test
    fun `when call fetchAll with success then convert dto and save local`() {
        val expectedDto = mockUsersDtoResponse()
        val expectedDB = mockUsersDBResponse()
        val expectedDomain = mockUsersResponse()
        whenever(remoteDataSource.getAllUserDto()).then { Single.just(expectedDto) }
        whenever(localDataSource.saveLocalUsers(expectedDB)).then { Completable.complete() }

        repository.fetchAll()
            .test()
            .assertValue(expectedDomain)
            .assertNoErrors()
            .assertOf { verify(localDataSource).saveLocalUsers(expectedDB) }
            .assertComplete()
            .dispose()
    }

    @Test
    fun `when call fetchAll with error then not convert dto and not save local`() {
        val errorExpected = DefaultException("erro")
        whenever(remoteDataSource.getAllUserDto()).then { Single.error<List<UserDto>>(errorExpected) }

        repository.fetchAll()
            .test()
            .assertError(errorExpected)
            .assertNotComplete()
            .assertOf { verifyZeroInteractions(localDataSource) }
            .dispose()
    }

    @Test
    fun `when call getLocalUsers with success then  convert dto and call getLocalUsers`() {
        val expectedDB = mockUsersDBResponse()
        val expectedDomain = mockUsersResponse()
        whenever(localDataSource.getLocalUsers()).then { Single.just(expectedDB) }

        repository.getLocalUsers()
            .test()
            .assertValue(expectedDomain)
            .assertNoErrors()
            .assertOf { verify(localDataSource).getLocalUsers() }
            .assertComplete()
            .dispose()
    }

    @Test
    fun `when call getLocalUsers with error then not convert dto and not call getLocalUsers`() {
        val errorExpected = DefaultException("erro")
        whenever(remoteDataSource.getAllUserDto()).then { Single.error<List<UserDB>>(errorExpected) }

        repository.fetchAll()
            .test()
            .assertError(errorExpected)
            .assertNotComplete()
            .assertOf { verify(remoteDataSource).getAllUserDto() }
            .dispose()
    }

    private fun mockUsersResponse() = listOf(User("img", "name", 1, "username"))
    private fun mockUsersDtoResponse() = listOf(UserDto("img", "name", 1, "username"))
    private fun mockUsersDBResponse() = listOf(UserDB("img", "name", 1, "username"))
}