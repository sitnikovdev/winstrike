package ru.prsolution.winstrike.ui.splash


import ru.prsolution.winstrike.mvp.apimodels.Arenas
import ru.prsolution.winstrike.mvp.apimodels.AuthResponse
import ru.prsolution.winstrike.mvp.apimodels.ConfirmSmsModel
import ru.prsolution.winstrike.mvp.models.LoginViewModel
import ru.prsolution.winstrike.mvp.models.MessageResponse
import ru.prsolution.winstrike.networking.NetworkError
import ru.prsolution.winstrike.networking.Service
import ru.prsolution.winstrike.ui.splash.SplashActivity
import rx.Subscription
import rx.subscriptions.CompositeSubscription

class SplashPresenter(private val service: Service, private val activity: SplashActivity) {
    private val subscriptions: CompositeSubscription

    init {
        this.subscriptions = CompositeSubscription()
    }

    fun signIn(user: LoginViewModel) {
        //        activity.showWait();

        val subscription = service.authUser(object : Service.AuthCallback {
            override fun onSuccess(authResponse: AuthResponse) {
                //                activity.removeWait();
                activity.onAuthResponseSuccess(authResponse)
            }

            override fun onError(networkError: NetworkError) {
                //                activity.removeWait();
                activity.onAuthFailure(networkError.appErrorMessage)
            }

        }, user)

        subscriptions.add(subscription)
    }


    fun sendSms(smsModel: ConfirmSmsModel) {

        val subscription = service.sendSmsByUserRequest(object : Service.SmsCallback {
            override fun onSuccess(authResponse: MessageResponse) {
                //                activity.removeWait();
                activity.onSendSmsSuccess(authResponse)
            }

            override fun onError(networkError: NetworkError) {
                //                activity.removeWait();
                activity.onSendSmsFailure(networkError.appErrorMessage)
            }

        }, smsModel)

        subscriptions.add(subscription)
    }


    fun getActiveArena() {

        val subscription = service.getArenas(object : Service.ArenasCallback {
            override fun onSuccess(authResponse: Arenas) {
                activity.onGetArenasResponseSuccess(authResponse)
            }

            override fun onError(networkError: NetworkError) {
                activity.onGetActivePidFailure(networkError.appErrorMessage)
            }

        })

        subscriptions.add(subscription)
    }


    fun onStop() {
        subscriptions.unsubscribe()
    }
}
