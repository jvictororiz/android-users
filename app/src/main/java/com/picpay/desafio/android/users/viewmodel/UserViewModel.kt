package com.picpay.desafio.android.users.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.picpay.desafio.android.R
import com.picpay.desafio.android.base.di.builders.ResourceManager
import com.picpay.desafio.android.users.domain.exception.NetworkException
import com.picpay.desafio.android.users.domain.model.User
import com.picpay.desafio.android.users.domain.usecase.contract.UserUseCase
import com.picpay.desafio.android.users.viewmodel.model.UserState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class UserViewModel(
    private val resource: ResourceManager,
    private val userUseCase: UserUseCase,
    private val state: MutableLiveData<UserState>
) : BaseViewModel<UserState>() {

    val stateLiveData: LiveData<UserState> get() = state

    init {
        getUsers()
    }

    fun tapOnRetry() {
        getUsers()
    }

    private fun getUsers() {
        notifyScreen { UserState.LoadingState }
        disposable.add(
            userUseCase.getAll()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    usersOnSuccess(it)
                }, {
                    usersOnError(it)
                })
        )
    }

    private fun getLocalUsers() {
        userUseCase.getAllLocal()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnSubscribe(disposable::add)
            .doOnSuccess(::onLocalSuccess)
            .subscribe()
    }

    private fun usersOnSuccess(users: List<User>) = notifyScreen {
        UserState.SuccessState(users)
    }

    private fun onLocalSuccess(users: List<User>) = notifyScreen {
        UserState.SuccessLocalState(users)
    }

    private fun usersOnError(throwable: Throwable) {
        when (throwable) {
            is NetworkException -> notifyScreen {
                UserState.ErrorState(
                    messageError = resource.message(R.string.error_without_connection),
                    retryMessage = resource.message(R.string.retry)
                )
            }
            else -> notifyScreen {
                UserState.ErrorState(
                    messageError = resource.message(R.string.error_default_server),
                    retryMessage = resource.message(R.string.retry)
                )
            }
        }
        getLocalUsers()
    }

    private fun notifyScreen(block: () -> UserState) {
        state.value = ((block()))
    }
}