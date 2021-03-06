package com.picpay.desafio.android.users.ui.fragment.users

import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeDown
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.picpay.desafio.android.R
import com.picpay.desafio.android.base.di.builders.AppDatabase
import com.picpay.desafio.android.users.data.local.entity.UserDB
import com.picpay.desafio.android.users.ui.activity.UserActivity
import com.picpay.desafio.android.users.ui.fragment.basetest.BaseRobotTest
import org.hamcrest.Matchers.not
import org.koin.test.KoinTest
import org.koin.test.inject

class UserFragmentRobot : BaseRobotTest<UserActivity>(), KoinTest {

    private val database by inject<AppDatabase>()

    fun startTest() = apply {
        startServer()
    }

    fun launchActivity() = apply {
        scenario = launchActivity(null)
    }

    fun swipeRefreshLoad() = apply {
        onView(withId(R.id.swipeRefresh)).perform(swipeDown())
    }

    fun tapOnRetry() = apply {
        onView(withId(R.id.btn_retry)).perform(click())
    }

    fun injectUsersSuccessMock() = apply {
        prepareSuccess(PATH_USERS)
    }

    fun injectUsersErrorNoConnectionMock() = apply {
        prepareError(500, true)
    }

    fun injectUsersErrorServerMock() = apply {
        prepareError(404)
    }

    fun injectUsersLocalDatabase() = apply {
        val myType = object : TypeToken<List<UserDB>>() {}.type
        val users = Gson().fromJson<List<UserDB>>(readJson(PATH_USERS), myType)
        database.userDao().saveAll(users).subscribe()
    }

    fun validateTitle() = apply {
        val expectedTitle = "Contatos"
        onView(withText(expectedTitle)).check(matches(isDisplayed()))
    }

    fun validateUsersScreen() = apply {
        onView(withText("Sandrine Spinka")).check(matches(isDisplayed()))
        onView(withText("Tod86")).check(matches(isDisplayed()))
    }

    fun validateNotContainsErrorScreen() = apply {
        onView(withId(R.id.contentError)).check(matches(not(isDisplayed())))
    }

    fun validateNoConnectionMessageError() = apply {
        val expectedMessageError =
            "N??o foi poss??vel conecar a internet, por favor, cheque sua conex??o e tente novamente."
        onView(withText(expectedMessageError)).check(matches(isDisplayed()))
    }

    fun validateServerMessageError() = apply {
        val expectedMessageError =
            "N??o foi poss??vel conectar ao servidor, por favor, tente novamente mais tarde"
        onView(withText(expectedMessageError)).check(matches(isDisplayed()))
    }

    fun validateContainsErrorScreen() = apply {
        onView(withId(R.id.contentError)).check(matches(isDisplayed()))
    }

    fun validateEmptyUsersScreen() = apply {
        onView(withText("Carli Carroll")).check(doesNotExist())
    }

    fun endTest() = apply {
        database.close()
        closeActivity()
    }

    companion object {
        private const val PATH_USERS = "/json/response_users.json"
        fun instance() = UserFragmentRobot()
    }
}