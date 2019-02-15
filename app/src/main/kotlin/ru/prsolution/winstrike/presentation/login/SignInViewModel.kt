package ru.prsolution.winstrike.presentation.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.prsolution.winstrike.datasource.model.login.AuthResponseEntity
import ru.prsolution.winstrike.domain.models.common.MessageResponse
import ru.prsolution.winstrike.networking.RetrofitFactory
import ru.prsolution.winstrike.data.repository.resouces.Resource

class SignInViewModel : ViewModel() {

    private val retrofitService = RetrofitFactory.makeRetrofitService()

/*    private val userConfidence: LoginViewModel? = LoginViewModel(
            "+79520757099", "123456")*/

/*    private val smsModel: ConfirmSmsModel? = ConfirmSmsModel(
            userConfidence?.username)*/

    val authResponse = MutableLiveData<Resource<AuthResponseEntity>>()

    val messageResponse = MutableLiveData<Resource<MessageResponse>>()

/*
    fun signIn() {
        checkNotNull(userConfidence)

        GlobalScope.launch {
            val request = retrofitService.authUserAsync(userConfidence)
            try {
                val response = request.await()
                response.body()?.let {
                    // activity.onSendSmsSuccess()
                    authResponse.setSuccess(it)
                }
            } catch (e: HttpException) {
                // TODO: make it more isClear
                Timber.e(e.message())
            } catch (e: Throwable) {
                Timber.e(e)
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }
*/

    fun sendSms() {
//        checkNotNull(smsModel)

        GlobalScope.launch {
            /*            val request = retrofitService.sendSmsByUserRequestAsync(smsModel)
                        try {
                            val response = request.await()
                            response.body()?.let {
                                // activity.onSendSmsSuccess()
                                messageResponse.setSuccess(it)
                            }
                        } catch (e: HttpException) {
                            Timber.e(e.message())
                        } catch (e: Throwable) {
                            Timber.e(e)
                        }*/
        }
    }
/*	fun sendSms(smsModel: ConfirmSmsModel) {
//		activity.showWait()

		val subscription = service?.sendSmsByUserRequest(object : Service.SmsCallback {
			override fun onSuccess(authResponse: MessageResponse) {
//				activity.removeWait()
				activity.onSendSmsSuccess(authResponse)
			}

			override fun onError(networkError: NetworkError) {
//				activity.removeWait()
				activity.onAuthFailure(networkError.appErrorMessage)
			}

		}, smsModel)

		subscriptions.add(subscription)
	}*/

// 	fun signIn(user: LoginViewModel) {
// 		activity.showWait()

/*		val subscription = service?.authUser(object : Service.AuthCallback {
			override fun onSuccess(authResponse: AuthResponse) {
//				activity.removeWait()
				activity.onAuthResponseSuccess(authResponse)
			}

			override fun onError(networkError: NetworkError) {
//				activity.removeWait()
				activity.onAuthFailure(networkError.appErrorMessage)
			}
		}, user)

		subscriptions.add(subscription)*/
// 	}
}
