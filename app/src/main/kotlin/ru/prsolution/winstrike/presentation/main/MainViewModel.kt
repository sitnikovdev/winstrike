package ru.prsolution.winstrike.presentation.main

import android.text.TextUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.HttpException
import ru.prsolution.winstrike.datasource.model.mapRoomToDomain
import ru.prsolution.winstrike.datasource.model.mapToDomain
import ru.prsolution.winstrike.domain.models.Arena
import ru.prsolution.winstrike.domain.models.ArenaSchema
import ru.prsolution.winstrike.domain.models.common.FCMModel
import ru.prsolution.winstrike.domain.models.common.MessageResponse
import ru.prsolution.winstrike.domain.models.SeatCarousel
import ru.prsolution.winstrike.domain.payment.PaymentModel
import ru.prsolution.winstrike.domain.payment.PaymentResponse
import ru.prsolution.winstrike.networking.RetrofitFactory
import ru.prsolution.winstrike.presentation.utils.SingleLiveEvent
import ru.prsolution.winstrike.presentation.utils.resouces.Resource
import ru.prsolution.winstrike.presentation.utils.setError
import ru.prsolution.winstrike.presentation.utils.setLoading
import ru.prsolution.winstrike.presentation.utils.setSuccess
import timber.log.Timber

/**
 * Created by oleg 23.01.2019
 */
class MainViewModel : ViewModel() {

	// TODO: Use Koin to decouple dependencies from here.
	private val retrofitService = RetrofitFactory.makeRetrofitService()

	// Активный фрагмент
	var active = MutableLiveData<Fragment>()

	// Ответ от FCM сервера на запрос токена
	val fcmResponse = MutableLiveData<Resource<MessageResponse>>()

	// Список имеющихся арен
	val arenaList = SingleLiveEvent<Resource<List<Arena>>>()

	// Выбранная  арена по времени
	val arena = MutableLiveData<Resource<ArenaSchema?>>()

	// Обновление информации о выбранном месте и времени
	val currentArena = MutableLiveData<String>()
	val currentSeat = MutableLiveData<SeatCarousel>()
	val currentDate = MutableLiveData<String>()
	val currentTime = MutableLiveData<String>()

	// Ответ от Яндекс Кассы
	val paymentResponse = SingleLiveEvent<Resource<PaymentResponse>>()

	// Url на страницу оплаты Яндекс кассы
	val redirectUrl = SingleLiveEvent<String>()


	// Получение списка имеющихся арен на сервере
	fun getArenaList() {
		GlobalScope.launch {
			val request = retrofitService.arenaListAsync()
			try {
				val response = request.await()
				response.body()?.let {
					arenaList.setSuccess(it.rooms.mapToDomain())
					Timber.tag("$$$").d("arena list: ${it.rooms.mapToDomain().size}")
				}
			} catch (e: Throwable) {
				arenaList.setError(e.message)
				Timber.e(e)
			}
		}
	}

	// Получение схемы выбранной арены по пиду и времени
	fun getArenaSchema(arenaPid: String?, time: Map<String, String>) {
		GlobalScope.launch {
			val request = retrofitService.arenaSchemaAsync(arenaPid, time)
			try {
				val response = request.await()
				response.body()?.let { arena.setSuccess(it.roomLayout?.mapRoomToDomain()) }
			} catch (e: Throwable) {
				arena.setError(e.message)
				Timber.e(e)
			}
		}
	}

	// Оплата выбраных мест через Яндекс Кассу
	fun getPayment(token: String, paymentModel: PaymentModel) {
		GlobalScope.launch {
			val request = retrofitService.getPaymentAsync(token, paymentModel)
			try {
				paymentResponse.setLoading()
				val response = request.await()
				response.body()?.let {
					Timber.tag("$$$").d("payment: $it")
					paymentResponse.setSuccess(it)
				}
				response.errorBody()?.let {
					paymentResponse.setError(it.string())
				}
			} catch (e: HttpException) {
				paymentResponse.setError(e.message)
				Timber.e(e)
			} catch (e: Exception) {
				Timber.e(e)
			}
		}
	}


	// Отправка токена FCM серверу
	fun sendFCMToken(userToken: String, fcmBody: FCMModel) {
		require(!TextUtils.isEmpty(userToken)) {
			"User's token don't have to be empty."
		}
		require(!TextUtils.isEmpty(fcmBody.token)) {
			"FCM token don't have to be empty."
		}

		GlobalScope.launch {
			val request = retrofitService.sendTockenAsync(userToken, fcmBody)
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


}
