package com.picpay.desafio.android.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.*
import com.picpay.desafio.android.ResourceManager
import com.picpay.desafio.android.unit.config.BaseViewModelTest
import com.picpay.desafio.android.unit.config.RxSchedulerRule
import com.picpay.desafio.android.domain.model.User
import com.picpay.desafio.android.domain.usecase.contract.UserUseCase
import com.picpay.desafio.android.ui.viewmodel.model.UserState
import io.reactivex.Single
import org.junit.Rule
import org.junit.Test


class UserViewModelTest : BaseViewModelTest() {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var schedulers = RxSchedulerRule()


    private val useCase: UserUseCase = mock()
    private val resourceManager: ResourceManager = mock()

    private lateinit var viewModel: UserViewModel

    private var stateLiveData: Observer<UserState> = mock()

    @Test
    fun `getUser with success in network`() {
        val expectedResponse = mockUsersResponse()
        whenever(useCase.getAll()).thenReturn(Single.just(expectedResponse))

        viewModel = UserViewModel(resourceManager, useCase)

        viewModel.stateLiveData.observeForever(stateLiveData)
        inOrder(stateLiveData) {
            verify(stateLiveData).onChanged(UserState.LoadingState(true))
            verify(stateLiveData).onChanged(UserState.SuccessState(expectedResponse))
            verify(stateLiveData).onChanged(UserState.LoadingState(false))
            verifyNoMoreInteractions(stateLiveData)
        }

//        viewModel.init()
    }

    @Test
    fun `getUser with success with error in network and without cache`() {
        val expectedString1 = "teste"
        val expectedString2 = "teste2"
        whenever(resourceManager.message(any()))
            .thenReturn(expectedString1)
            .thenReturn(expectedString2)
        whenever(useCase.getAll()).thenReturn(Single.error(Exception()))
        whenever(useCase.getAllLocal()).thenReturn(Single.just(listOf()))

        viewModel = UserViewModel(resourceManager, useCase)
        viewModel.stateLiveData.observeForever(stateLiveData)

        inOrder(stateLiveData) {
            verify(stateLiveData).onChanged(UserState.LoadingState(true))
            verify(stateLiveData).onChanged(
                UserState.ErrorState(
                    expectedString1,
                    expectedString2
                )
            )
            verify(stateLiveData).onChanged(UserState.SuccessLocalState(listOf()))
                    verify(stateLiveData).onChanged(UserState.LoadingState(false))
            verifyNoMoreInteractions(stateLiveData)
        }


    }

    @Test
    fun `getUser with success with error in network and with cache`() {
        val expectedString1 = "teste"
        val expectedString2 = "teste2"
        whenever(resourceManager.message(any()))
            .thenReturn(expectedString1)
            .thenReturn(expectedString2)
        whenever(useCase.getAll()).thenReturn(Single.error(Exception()))
        whenever(useCase.getAllLocal()).thenReturn(Single.just(mockUsersResponse()))

        viewModel = UserViewModel(resourceManager, useCase)
        viewModel.stateLiveData.observeForever(stateLiveData)
//        viewModel.init()


        inOrder(stateLiveData) {
            verify(stateLiveData).onChanged(UserState.LoadingState(true))
            verify(stateLiveData).onChanged(UserState.ErrorState(expectedString1, expectedString2))
            verify(stateLiveData).onChanged(UserState.SuccessLocalState(mockUsersResponse()))
            verify(stateLiveData).onChanged(UserState.LoadingState(false))
            verifyNoMoreInteractions(stateLiveData)
        }
    }

    @Test
    fun `getUser with error in network`() {
        val expectedString1 = "teste"
        val expectedString2 = "teste2"
        whenever(resourceManager.message(any()))
            .thenReturn(expectedString1)
            .thenReturn(expectedString2)
        whenever(useCase.getAll()).thenReturn(Single.error(Exception()))
        whenever(useCase.getAllLocal()).thenReturn(Single.just(listOf()))

        viewModel = UserViewModel(resourceManager, useCase)
        viewModel.stateLiveData.observeForever(stateLiveData)
//        viewModel.init()

        inOrder(stateLiveData) {
            verify(stateLiveData).onChanged(UserState.LoadingState(true))
            verify(stateLiveData).onChanged(UserState.ErrorState(expectedString1, expectedString2))
            verify(stateLiveData).onChanged(UserState.SuccessLocalState(listOf()))
            verify(stateLiveData).onChanged(UserState.LoadingState(false))
            verifyNoMoreInteractions(stateLiveData)
        }
    }

    private fun mockUsersResponse() = listOf(User("img", "name", 1, "username"))

}
