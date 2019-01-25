package ru.prsolution.winstrike.presentation.map

import ru.prsolution.winstrike.WinstrikeApp
import ru.prsolution.winstrike.presentation.utils.pref.PrefUtils
import ru.prsolution.winstrike.datasource.model.PaymentModel
import ru.prsolution.winstrike.datasource.model.PaymentResponse
import ru.prsolution.winstrike.datasource.model.RoomLayoutFactory
import ru.prsolution.winstrike.domain.models.GameRoom
import ru.prsolution.winstrike.presentation.utils.date.TimeDataModel
import ru.prsolution.winstrike.networking.NetworkError
import ru.prsolution.winstrike.networking.Service
import rx.subscriptions.CompositeSubscription

/**
 * Created by oleg 24.01.2019
 */

class MapPresenter(private val service: Service?, private val fragment: MapFragment) {


	/**
	 * Yo, now we can go to yandex casa and bye some cool seats in Winstrike PC club!
	 */
	fun onBookingClick() {
		val payModel = PaymentModel()

		// TODO: 12/05/2018 Replace with TimeDataModel.
		payModel.startAt = TimeDataModel.start
		payModel.end_at = TimeDataModel.end
		payModel.setPlacesPid(TimeDataModel.pids)

		val token = "Bearer " + PrefUtils.token
		getPayment(token, payModel)
		fragment.onSnackBarHide()
	}


	fun getPayment(token: String, paymentModel: PaymentModel) {

		service?.getPayment(object : Service.PaymentCallback {
			override fun onSuccess(authResponse: PaymentResponse) {
				fragment.onGetPaymentResponseSuccess(authResponse)
			}

			override fun onError(networkError: NetworkError) {
				fragment.onGetPaymentFailure(networkError.appErrorMessage)
			}

		}, token, paymentModel)

	}


}
