package ru.prsolution.winstrike.viewmodel

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.prsolution.winstrike.domain.models.common.FCMModel
import ru.prsolution.winstrike.domain.models.common.MessageResponse
import ru.prsolution.winstrike.networking.RetrofitFactory
import ru.prsolution.winstrike.data.repository.resouces.Resource
import ru.prsolution.winstrike.presentation.utils.setSuccess
import timber.log.Timber

/**
 * Created by oleg 23.01.2019
 */
class MainViewModel : ViewModel() {

    // TODO: Use Koin to decouple dependencies from here.
    private val retrofitService = RetrofitFactory.makeRetrofitService()

    // Ответ от FCM сервера на запрос токена
    val fcmResponse = MutableLiveData<Resource<MessageResponse>>()


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
