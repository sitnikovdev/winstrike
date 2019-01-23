package ru.prsolution.winstrike.presentation.splash


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.HttpException
import ru.prsolution.winstrike.datasource.model.ConfirmSmsModel
import ru.prsolution.winstrike.datasource.model.Room
import ru.prsolution.winstrike.domain.models.MessageResponse
import ru.prsolution.winstrike.networking.RetrofitFactory
import ru.prsolution.winstrike.networking.Service
import ru.prsolution.winstrike.presentation.utils.resouces.Resource
import ru.prsolution.winstrike.presentation.utils.setSuccess
import rx.subscriptions.CompositeSubscription
import timber.log.Timber

class SplashViewModel : ViewModel() {

	private val retrofitService = RetrofitFactory.makeRetrofitService()

	val messageResponse = MutableLiveData<Resource<MessageResponse>>()


	fun sendSms(smsModel: ConfirmSmsModel) {

		GlobalScope.launch {
			val request = retrofitService.sendSmsByUserRequest(smsModel)
			try {
				val response = request.await()
				response.body()?.let {
					// activity.onSendSmsSuccess()
					messageResponse.setSuccess(it)
				}
			} catch (e: HttpException) {
				Timber.e(e.message())
			} catch (e: Throwable) {
				Timber.e(e)
			}
		}
	}

}
