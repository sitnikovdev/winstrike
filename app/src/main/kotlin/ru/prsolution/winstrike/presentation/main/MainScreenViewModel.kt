package ru.prsolution.winstrike.presentation.main

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.prsolution.winstrike.datasource.model.OrderModel
import ru.prsolution.winstrike.domain.models.FCMModel
import ru.prsolution.winstrike.domain.models.MessageResponse
import ru.prsolution.winstrike.domain.models.ProfileModel
import ru.prsolution.winstrike.networking.RetrofitFactory
import ru.prsolution.winstrike.networking.RetrofitService
import ru.prsolution.winstrike.presentation.utils.pref.AuthUtils
import ru.prsolution.winstrike.presentation.utils.resouces.Resource
import ru.prsolution.winstrike.presentation.utils.setSuccess
import rx.subscriptions.CompositeSubscription
import timber.log.Timber

/**
 * Created by oleg 23.01.2019
 */
class MainScreenViewModel : ViewModel() {

	val retrofitService = RetrofitFactory.makeRetrofitService()

	val fcmResponse = MutableLiveData<Resource<MessageResponse>>()

//	val userToken = AuthUtils.token
//	val fcmToken: FCMModel = FCMModel(AuthUtils.fcmtoken)

	fun sendFCMToken(userToken: String, fcmBody: FCMModel) {
		require(!TextUtils.isEmpty(userToken)) {
			"User's token don't have to be empty."
		}
		require(!TextUtils.isEmpty(fcmBody.token)) {
			"FCM token don't have to be empty."
		}

		GlobalScope.launch {
			val request = retrofitService.sendTocken(userToken, fcmBody)
			try {
				val response = request.await()
				response.body()?.let {
					fcmResponse.setSuccess(it)
				}
			} catch (e: Throwable) {
				Timber.e(e)
			}
		}

	}

	fun sendFCMTokenToServer(token: String, fcmToken: FCMModel) {

/*        val subscription = service.sendToken(object : Service.FcmTokenCallback {
            override fun onSuccess(messageResponse: MessageResponse) {
                onTokenSendSuccessfully(messageResponse)
            }

            override fun onError(networkError: NetworkError) {
                onFailtureTokenSend(networkError.appErrorMessage)
            }
        }, token, fcmToken)

        subscriptions.add(subscription)*/
	}


	fun onCreate() {
//        viewState.highlightTab(MainScreenView.HOME_TAB_POSITION)
		//        router.replaceScreen(Screens.START_SCREEN);
	}


	fun onTabPlaceClick(mPayList: List<OrderModel>) {
//        viewState.highlightTab(MainScreenView.PLACE_TAB_POSITION)
		//        router.replaceScreen(Screens.PLACE_SCREEN,mPayList);
	}


	fun onTabUserClick() {
//        viewState.highlightTab(MainScreenView.USER_TAB_POSITION)
		//        router.replaceScreen(Screens.USER_SCREEN);
	}

	fun onTabHomeClick() {
//        viewState.highlightTab(MainScreenView.HOME_TAB_POSITION)
		//        router.replaceScreen(Screens.START_SCREEN);
	}

	fun onMapShowClick() {
		//        router.replaceScreen(Screens.MAP_SCREEN,0);
	}

	fun onChooseScreenClick() {
//        viewState.hideBottomTab()
		//        router.replaceScreen(Screens.CHOOSE_SCREEN);
	}


	fun onBackPressed() {
		//        router.exit(); // used Back command
//        viewState.showBottomTab()
//        viewState.setProfileScreenVisibility(false)
		//        router.replaceScreen(Screens.START_SCREEN,0);
	}


	fun updateProfile(token: String, profile: ProfileModel, publicId: String) {

/*        val subscription = service.updateUser(object : Service.ProfileCallback {
            override fun onSuccess(authResponse: MessageResponse) {
                viewState.onProfileUpdateSuccessfully(authResponse)
            }

            override fun onError(networkError: NetworkError) {
                viewState.onFailureUpdateProfile(networkError.appErrorMessage)
            }

        }, token, profile, publicId)

        subscriptions.add(subscription)*/
	}


	private fun onFailtureTokenSend(appErrorMessage: String) {
		Timber.d("On failure send token: %s", appErrorMessage)
	}

	private fun onTokenSendSuccessfully(authResponse: MessageResponse) {
		Timber.d("Successfully send token: %s", authResponse.message)
	}


	fun getOrders(token: String) {
//        viewState.showWait()

/*        val subscription = service.getOrders(object : Service.OrdersCallback {
            override fun onSuccess(orders: Orders) {
                viewState.removeWait()

                val orderModels = ArrayList<OrderModel>()

                for (order in orders.orders) {
                    val orderModel = OrderModel()
                    orderModel.arenaName = order.roomName
                    orderModel.cost = order.cost
                    orderModel.accessCode = order.accessCode
                    orderModel.startAt = order.startAt
                    orderModel.endAt = order.endAt
                    orderModel.pcName = order.place.computer.name

                    val date = Utils.getFormattedDate(order.startAt)
                    val time = formatTime(order.startAt) + "-" + formatTime(order.endAt)
                    orderModel.date = date
                    orderModel.time = time
                    orderModel.thumbnail = R.drawable.main_screen
                    orderModels.add(orderModel)
                }

                viewState.onGetOrdersSuccess(orderModels)
            }

            override fun onError(networkError: NetworkError) {
                viewState.removeWait()
                viewState.onGetOrdersFailure(networkError.appErrorMessage)
            }

        }, token)

        subscriptions.add(subscription)*/
	}

}
