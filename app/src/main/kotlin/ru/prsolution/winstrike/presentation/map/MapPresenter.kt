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

class MapPresenter(private val service: Service?, private val fragment: MapScreenFragment) {
	private val subscriptions: CompositeSubscription

	init {
		this.subscriptions = CompositeSubscription()
	}


	fun readMap() {
		val roomLayoutFactory: RoomLayoutFactory

		roomLayoutFactory = RoomLayoutFactory()
		roomLayoutFactory.roomLayout = WinstrikeApp.instance.roomLayout

		// Init models:
		val room = GameRoom(roomLayoutFactory.roomLayout)

		fragment.showSeat(room)
	}


	/**
	 * Yo, now we can go to yandex casa and bye some cool seats in Winstrike PC club!
	 */
	fun onBookingClick() {
		val payModel: PaymentModel
		payModel = PaymentModel()

		// TODO: 12/05/2018 Replace with TimeDataModel.
		payModel.startAt = TimeDataModel.start
		payModel.end_at = TimeDataModel.end
		payModel.setPlacesPid(TimeDataModel.pids)

		val token = "Bearer " + PrefUtils.token
		getPayment(token, payModel)
		fragment.onSnackBarHide()
	}

	fun initScreen() {
		fragment.onScreenInit()
	}


	fun getPayment(token: String, paymentModel: PaymentModel) {
		fragment.showWait()

		val subscription = service?.getPayment(object : Service.PaymentCallback {
			override fun onSuccess(authResponse: PaymentResponse) {
				fragment.removeWait()
				fragment.onGetPaymentResponseSuccess(authResponse)
			}

			override fun onError(networkError: NetworkError) {
				fragment.removeWait()
				fragment.onGetPaymentFailure(networkError.appErrorMessage)
			}

		}, token, paymentModel)

		subscriptions.add(subscription)
	}

	fun onStop() {
		this.subscriptions.unsubscribe()
	}

}
