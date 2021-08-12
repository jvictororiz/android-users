package com.picpay.desafio.android.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.picpay.desafio.android.R
import com.picpay.desafio.android.ResourceManager
import com.picpay.desafio.android.domain.exception.NetworkException
import com.picpay.desafio.android.domain.model.User
import com.picpay.desafio.android.domain.usecase.contract.UserUseCase
import com.picpay.desafio.android.ui.viewmodel.model.UserState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class UserViewModel(
    private val resource: ResourceManager,
    private val userUseCase: UserUseCase
) : BaseViewModel() {
    val stateLiveData: LiveData<UserState> get() = _state
    private var _state = MutableLiveData<UserState>()

    init {
        getUsers()
        
    }

    fun tapOnRetry() {
        getUsers()
    }

    private fun getUsers() {
        notifyScreen { UserState.LoadingState(true) }
        disposable.add(
            userUseCase.getAll()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doAfterTerminate(::doAfterTerminate)
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

    private fun doAfterTerminate() = notifyScreen {
        UserState.LoadingState(false)
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
        _state.value = ((block()))
    }
}