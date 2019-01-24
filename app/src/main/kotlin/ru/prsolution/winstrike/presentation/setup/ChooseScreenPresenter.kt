package ru.prsolution.winstrike.presentation.setup

import ru.prsolution.winstrike.WinstrikeApp
import ru.prsolution.winstrike.networking.Service
import rx.subscriptions.CompositeSubscription


class ChooseScreenPresenter(service: Service?, private val fragment: SetupFragment) {
	private val subscriptions: CompositeSubscription
	private val service: Service


	init {
		this.subscriptions = CompositeSubscription()
		if (service == null) {
			this.service = WinstrikeApp.instance.service
		} else {
			this.service = service
		}
	}


	fun getActiveArena(selectedArena: Int) {
/*
		fragment.showWait()

		val subscription = service.getArenas(object : Service.ArenasCallback {
			override fun onSuccess(authResponse: Arenas) {
				fragment.onGetArenasResponseSuccess(authResponse, selectedArena)
			}

			override fun onError(networkError: NetworkError) {
				fragment.removeWait()
				fragment.onGetAcitivePidFailure(networkError.appErrorMessage)
			}
*/

		}

//		subscriptions.add(subscription)
	}


	fun getArenaByTimeRange(activeLayoutPid: String?, time: Map<String, String>) {

/*		val subscription = service.getArenaByTimeRange(object : Service.RoomLayoutByTimeCallback {
			override fun onSuccess(authResponse: RoomLayoutFactory) {
				fragment.removeWait()
				fragment.onGetArenaByTimeResponseSuccess(authResponse)
			}

			override fun onError(networkError: NetworkError) {
				fragment.removeWait()
				fragment.onGetArenaByTimeFailure(networkError.appErrorMessage)
			}

		}, activeLayoutPid, time)

		subscriptions.add(subscription)
	}


	fun onStop() {
		subscriptions.unsubscribe()
	}*/

}
