package com.picpay.desafio.android.ui.viewmodel.model

import com.picpay.desafio.android.domain.model.User

sealed class UserState {
    data class SuccessState(val users: List<User>) : UserState()
    data class SuccessLocalState(val users: List<User>) : UserState()
    data class LoadingState(val load: Boolean) : UserState()
    data class ErrorState(val messageError: String, val retryMessage: String) : UserState()
}