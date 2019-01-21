package ru.prsolution.winstrike.presentation.splash


import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.HttpException
import ru.prsolution.winstrike.mvp.apimodels.ConfirmSmsModel
import ru.prsolution.winstrike.networking.RetrofitFactory
import ru.prsolution.winstrike.networking.Service
import rx.subscriptions.CompositeSubscription
import timber.log.Timber

class SplashPresenter(private val service: Service, private val activity: SplashActivity) {
    private val subscriptions: CompositeSubscription = CompositeSubscription()

    private val retrofitService = RetrofitFactory.makeRetrofitService()

    fun sendSms(smsModel: ConfirmSmsModel) {

        GlobalScope.launch {
            val request = retrofitService.sendSmsByUserRequest(smsModel)
            try {
                val response = request.await()
                response.body()?.let { activity.onSendSmsSuccess() }
            } catch (e: HttpException) {
                Timber.e(e.message())
            } catch (e: Throwable) {
                Timber.e(e)
            }
        }
    }


    fun getActiveArena() {

        GlobalScope.launch {
            val request = retrofitService.arenas
            try {
                val response = request.await()
                response.body()?.let { activity.onGetArenasResponseSuccess(it) }
            } catch (e: HttpException) {
                Timber.e(e.message())
            } catch (e: Throwable) {
                Timber.e(e)
            }
        }
    }


    fun onStop() {
        subscriptions.unsubscribe()
    }
}
