package com.picpay.desafio.android.base

fun <T> T.runIf(add: Boolean, block: (T) -> T): T {
    return if (add) {
        block(this)
    } else {
        this
    }
}