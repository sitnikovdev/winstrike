package ru.prsolution.winstrike.presentation.login


import ru.prsolution.winstrike.datasource.model.AuthResponse
import ru.prsolution.winstrike.domain.models.MessageResponse
import ru.prsolution.winstrike.domain.models.LoginViewModel
import ru.prsolution.winstrike.datasource.model.ConfirmSmsModel
import ru.prsolution.winstrike.networking.NetworkError
import ru.prsolution.winstrike.networking.Service
import rx.subscriptions.CompositeSubscription

class SignInPresenter(private val service: Service?, private val activity: SignInActivity) {
	private val subscriptions: CompositeSubscription

	init {
		this.subscriptions = CompositeSubscription()
	}

	fun signIn(user: LoginViewModel) {
		activity.showWait()

		val subscription = service?.authUser(object : Service.AuthCallback {
			override fun onSuccess(authResponse: AuthResponse) {
				activity.removeWait()
				activity.onAuthResponseSuccess(authResponse)
			}

			override fun onError(networkError: NetworkError) {
				activity.removeWait()
				activity.onAuthFailure(networkError.appErrorMessage)
			}

		}, user)

		subscriptions.add(subscription)
	}


	fun sendSms(smsModel: ConfirmSmsModel) {
		activity.showWait()

		val subscription = service?.sendSmsByUserRequest(object : Service.SmsCallback {
			override fun onSuccess(authResponse: MessageResponse) {
				activity.removeWait()
				activity.onSendSmsSuccess(authResponse)
			}

			override fun onError(networkError: NetworkError) {
				activity.removeWait()
				activity.onAuthFailure(networkError.appErrorMessage)
			}

		}, smsModel)

		subscriptions.add(subscription)
	}

	fun onStop() {
		subscriptions.unsubscribe()
	}
}
