package ru.prsolution.winstrike.ui.main

import com.arellomobile.mvp.MvpPresenter
import ru.prsolution.winstrike.mvp.apimodels.OrderModel
import ru.prsolution.winstrike.mvp.models.FCMModel
import ru.prsolution.winstrike.mvp.models.MessageResponse
import ru.prsolution.winstrike.mvp.models.ProfileModel
import ru.prsolution.winstrike.mvp.views.MainScreenView
import ru.prsolution.winstrike.networking.RetrofitService
import rx.subscriptions.CompositeSubscription
import timber.log.Timber

/**
 * Created by terrakok 25.11.16
 */
class MainScreenPresenter(//    private Router router;
        private val service: RetrofitService) : MvpPresenter<MainScreenView>() {
    private val subscriptions: CompositeSubscription

    init {
        this.subscriptions = CompositeSubscription()
    }//        this.router = router;

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


    fun onStop() {
        subscriptions.unsubscribe()
    }
}
