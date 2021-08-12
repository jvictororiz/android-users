package com.picpay.desafio.android.config

import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

private val trampoline = Schedulers.trampoline()

class RxSchedulerRule : TestRule {
    override fun apply(base: Statement, description: Description) =
        object : Statement() {
            override fun evaluate() {
                RxAndroidPlugins.reset()
                RxAndroidPlugins.setInitMainThreadSchedulerHandler { trampoline }

                RxJavaPlugins.reset()
                RxJavaPlugins.setIoSchedulerHandler { trampoline }
                RxJavaPlugins.setNewThreadSchedulerHandler { trampoline }
                RxJavaPlugins.setComputationSchedulerHandler { trampoline }

                base.evaluate()
            }
        }
}