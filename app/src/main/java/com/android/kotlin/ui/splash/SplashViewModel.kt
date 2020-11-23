package com.android.kotlin.ui.splash

import com.android.kotlin.data.Repository
import com.android.kotlin.data.errors.NoAuthException
import com.android.kotlin.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class SplashViewModel(private val repository: Repository) : BaseViewModel<Boolean?>() {

    fun requestUser() {
        launch {
            repository.getCurrentUser()?.let {
                setData(true)
            } ?: setError(NoAuthException())
        }

    }
}