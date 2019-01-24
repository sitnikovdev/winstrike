package ru.prsolution.winstrike.presentation.main

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ositnikov.preference.LiveSharedPreferences
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.prsolution.winstrike.WinstrikeApp
import ru.prsolution.winstrike.datasource.model.OrderModel
import ru.prsolution.winstrike.datasource.model.Room
import ru.prsolution.winstrike.domain.models.FCMModel
import ru.prsolution.winstrike.domain.models.MessageResponse
import ru.prsolution.winstrike.domain.models.ProfileModel
import ru.prsolution.winstrike.domain.models.SeatModel
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

	val fcmResponse = MutableLiveData<Resource<MessageResponse>>()

	val rooms = MutableLiveData<Resource<List<Room>>>()

	val currentArena = MutableLiveData<Resource<Int>>()
	val currentSeat = MutableLiveData<SeatModel>()

	fun getRooms() {
		GlobalScope.launch {
			val request = retrofitService.arenas
			try {
				val response = request.await()
				response.body()?.let { rooms.setSuccess(it.rooms) }
			} catch (e: Throwable) {
				rooms.setError(e.message)
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
