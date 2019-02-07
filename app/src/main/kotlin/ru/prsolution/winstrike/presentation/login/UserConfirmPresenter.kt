package ru.prsolution.winstrike.presentation.login

import ru.prsolution.winstrike.datasource.model.login.ConfirmSmsModel
import ru.prsolution.winstrike.domain.models.login.ConfirmModel
import ru.prsolution.winstrike.domain.models.login.ProfileModel

/**
 * Created by ennur on 6/25/16.
 */
class UserConfirmPresenter() {

    init {
    }

    fun confirmUser(sms_code: String, confirmPhone: ConfirmModel) {
        //        view.showWait();

/*		val subscription = service?.confirmUser(object : Service.ConfirmCallback {
			override fun onSuccess(authResponse: MessageResponse) {
				//                view.removeWait();
//				view.onUserConfirmSuccess(authResponse)
			}

			override fun onError(networkError: NetworkError) {
				//                view.removeWait();
//				view.onUserConfirmFailure(networkError.appErrorMessage)
			}

		}, sms_code, confirmPhone)

		subscriptions.add(subscription)*/
    }

    /**
	 * Send code by tap on "Again button send"
	 * @param smsModel
	 */
    fun sendSms(smsModel: ConfirmSmsModel) {

/*		val subscription = service.sendSmsByUserRequest(object : Service.SmsCallback {
			override fun onSuccess(authResponse: MessageResponse) {
				view.onSendSmsSuccess(authResponse)
			}

			override fun onError(networkError: NetworkError) {
				view.onSmsSendFailure(networkError.appErrorMessage)
			}

		}, smsModel)

		subscriptions.add(subscription)*/
    }

    /**
	 * Update user profile after successfully confirmed. Set user name.
	 *
	 * @param token saved token
	 * @param profile profile user data
	 * @param publicId user public id
	 */
    fun updateProfile(token: String, profile: ProfileModel, publicId: String) {

/*		val subscription = service.updateUser(object : Service.ProfileCallback {
			override fun onSuccess(authResponse: MessageResponse) {
				view.onProfileUpdateSuccessfully(authResponse)
			}

			override fun onError(networkError: NetworkError) {
				view.onFailtureUpdateProfile(networkError.appErrorMessage)
			}

		}, token, profile, publicId)

		subscriptions.add(subscription)*/
    }

    fun onStop() {
    }
}
