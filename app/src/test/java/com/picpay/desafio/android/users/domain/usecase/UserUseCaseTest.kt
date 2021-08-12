package com.picpay.desafio.android.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.picpay.desafio.android.domain.exception.DefaultException
import com.picpay.desafio.android.domain.model.User
import com.picpay.desafio.android.domain.repository.contract.UserRepository
import io.reactivex.Single
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class UserUseCaseTest {

    private val repository: UserRepository = mock()
    private val useCase = UserUseCaseImpl(repository)

    @Rule
    @JvmField
    val schedulers = InstantTaskExecutorRule()

    @Test
    fun `when call fetchAll with success then return success`() {
        val expectedDomain = mockUsersResponse()
        whenever(repository.fetchAll()).then { Single.just(expectedDomain) }

        repository.fetchAll()
            .test()
            .assertValue(expectedDomain)
            .assertNoErrors()
            .assertOf { verify(repository).fetchAll() }
            .assertComplete()
            .dispose()
    }

    @Test
    fun `when call fetchAll with error then not convert dto and not save local`() {
        val errorExpected = DefaultException("erro")
        whenever(repository.fetchAll()).then { Single.error<List<User>>(errorExpected) }

        repository.fetchAll()
            .test()
            .assertError(errorExpected)
            .assertNotComplete()
            .assertOf { verify(repository).fetchAll() }
            .dispose()
    }

    @Test
    fun `when call getLocalUsers with success then  convert dto and call getLocalUsers`() {
        val expectedDomain = mockUsersResponse()
        whenever(repository.getLocalUsers()).then { Single.just(expectedDomain) }

        repository.getLocalUsers()
            .test()
            .assertValue(expectedDomain)
            .assertNoErrors()
            .assertOf { verify(repository).getLocalUsers() }
            .assertComplete()
            .dispose()
    }

    @Test
    fun `when call getLocalUsers with error then not convert dto and not call getLocalUsers`() {
        val errorExpected = DefaultException("erro")
        whenever(repository.getLocalUsers()).then { Single.error<List<User>>(errorExpected) }

        repository.getLocalUsers()
            .test()
            .assertError(errorExpected)
            .assertNotComplete()
            .assertOf { verify(repository).getLocalUsers() }
            .dispose()
    }

    private fun mockUsersResponse() = listOf(User("img", "name", 1, "username"))
}