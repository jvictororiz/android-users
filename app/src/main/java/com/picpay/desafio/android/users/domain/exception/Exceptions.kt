package com.picpay.desafio.android.domain.exception

data class InternalServerException(val errorMessage: String) : Exception()
data class NetworkException(val errorMessage: String) : Exception()
data class DefaultException(val errorMessage: String) : Exception()