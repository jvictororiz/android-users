package com.picpay.desafio.android.users.domain.exception

data class NetworkException(val errorMessage: String) : Exception()
data class DefaultException(val errorMessage: String) : Exception()