package ru.prsolution.winstrike.presentation.login.register

import ru.prsolution.winstrike.datasource.model.login.SmsEntity
import ru.prsolution.winstrike.domain.models.login.LoginModel

class RegisterPresenter() {

    fun createUser(user: LoginModel) {

/*		val subscription = service.createUser(object : Service.RegisterCallback {
			override fun onSuccess(authResponse: AuthResponse) {
				view.onRegisterSuccess(authResponse)
			}

			override fun onError(networkError: NetworkError) {
				view.onRegisterFailure(networkError.appErrorMessage)
			}

		}, user)

		subscriptions.add(subscription)*/
    }

    /**
	 * Send code by tap on "Again button send"
	 * @param smsModel
	 */
    fun sendSms(smsModel: SmsEntity) {

/*		val subscription = service?.sendSmsByUserRequest(object : Service.SmsCallback {
			override fun onSuccess(authResponse: MessageResponse) {
//				view.onSendSmsSuccess(authResponse)
			}

			override fun onError(networkError: NetworkError) {
//				view.onSmsSendFailure(networkError.appErrorMessage)
			}

		}, smsModel)

		subscriptions.add(subscription)*/
    }

    fun onStop() {
    }
}
