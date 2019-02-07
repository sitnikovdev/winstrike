package ru.prsolution.winstrike.presentation.setup

class ChooseScreenPresenter() {

    init {
// 		this.subscriptions = CompositeSubscription()
    }

    fun getActiveArena(selectedArena: Int) {
/*
		fragment.showWait()

		val subscription = service.getArenaList(object : Service.ArenasCallback {
			override fun onSuccess(authResponse: Arenas) {
				fragment.onGetArenasResponseSuccess(authResponse, selectedArena)
			}

			override fun onError(networkError: NetworkError) {
				fragment.removeWait()
				fragment.onGetAcitivePidFailure(networkError.appErrorMessage)
			}
*/
        }

// 		subscriptions.add(subscription)
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
