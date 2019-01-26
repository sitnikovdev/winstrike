package ru.prsolution.winstrike.presentation.main

import android.text.TextUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.prsolution.winstrike.datasource.model.ArenaEntity
import ru.prsolution.winstrike.datasource.model.RoomsEntity
import ru.prsolution.winstrike.datasource.model.mapRoomToDomain
import ru.prsolution.winstrike.domain.models.Room
import ru.prsolution.winstrike.domain.models.common.FCMModel
import ru.prsolution.winstrike.domain.models.common.MessageResponse
import ru.prsolution.winstrike.domain.models.SeatCarousel
import ru.prsolution.winstrike.networking.RetrofitFactory
import ru.prsolution.winstrike.presentation.utils.resouces.Resource
import ru.prsolution.winstrike.presentation.utils.setError
import ru.prsolution.winstrike.presentation.utils.setSuccess
import timber.log.Timber

/**
 * Created by oleg 23.01.2019
 */
class MainViewModel : ViewModel() {

	private val retrofitService = RetrofitFactory.makeRetrofitService()

	var active = MutableLiveData<Fragment>()

	val fcmResponse = MutableLiveData<Resource<MessageResponse>>()

	// Список арен
	val rooms = MutableLiveData<Resource<List<ArenaEntity>>>()
	// Выбранная пользователем арена по времени
	val arena = MutableLiveData<Resource<Room?>>()

	val currentArena = MutableLiveData<Resource<Int>>()
	val currentSeat = MutableLiveData<SeatCarousel>()
	val currentDate = MutableLiveData<String>()
	val currentTime = MutableLiveData<String>()


	// Получение списка арен (новый API)
	fun getRooms() {
		GlobalScope.launch {
			val request = retrofitService.arenaList
			try {
				val response = request.await()
				response.body()?.let { rooms.setSuccess(it.rooms) }
			} catch (e: Throwable) {
				rooms.setError(e.message)
				Timber.e(e)
			}
		}
	}

	fun getArena(arenaPid: String?, time: Map<String, String>) {
		GlobalScope.launch {
			val request = retrofitService.arenaAsync(arenaPid, time)
			try {
				val response = request.await()
//				Timber.d("response: ${response.body()}")
				response.body()?.let { arena.setSuccess(it.roomLayout?.mapRoomToDomain()) }
			} catch (e: Throwable) {
				arena.setError(e.message)
				Timber.e(e)
			}
		}
	}


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


}
