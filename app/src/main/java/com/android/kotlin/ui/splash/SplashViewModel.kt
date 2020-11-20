package com.android.kotlin.ui.splash

import com.android.kotlin.data.Repository
import com.android.kotlin.data.errors.NoAuthException
import com.android.kotlin.ui.base.BaseViewModel

class SplashViewModel(private val repository: Repository = Repository) : BaseViewModel<Boolean?, SplashViewState>() {

    fun requestUser() {
        repository.getCurrentUser().observeForever {
            viewStateLiveData.value = it?.let {
                SplashViewState(isAuth = true)
            } ?: let {
                SplashViewState(error = NoAuthException())
            }
        }
    }
}