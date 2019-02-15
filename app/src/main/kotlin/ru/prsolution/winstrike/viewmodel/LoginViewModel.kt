package ru.prsolution.winstrike.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import ru.prsolution.winstrike.domain.models.login.AuthResponse
import ru.prsolution.winstrike.domain.models.login.LoginModel
import ru.prsolution.winstrike.domain.usecases.LoginUseCase
import ru.prsolution.winstrike.presentation.utils.SingleLiveEvent
import kotlin.coroutines.CoroutineContext

/**
 * Created by Oleg Sitnikov on 2019-02-12
 */


class LoginViewModel constructor(val loginUseCase: LoginUseCase) : ViewModel() {


    private val parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)

    private val loginModel = LoginModel("+79520757099", "123456")

    val authResponse = SingleLiveEvent<AuthResponse>()


    fun getUser() {
        scope.launch {
            val response = loginUseCase.get(loginModel)
            authResponse.postValue(response)
        }
    }


    private fun cancelAllRequests() = coroutineContext.cancel()

    override fun onCleared() {
        super.onCleared()
        cancelAllRequests()
    }

}