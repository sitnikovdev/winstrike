package ru.prsolution.winstrike.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import ru.prsolution.winstrike.domain.models.payment.Payment
import ru.prsolution.winstrike.domain.models.payment.mapToPresentation
import ru.prsolution.winstrike.presentation.model.payment.PaymentResponseItem
import ru.prsolution.winstrike.domain.usecases.PaymentUseCase
import ru.prsolution.winstrike.presentation.utils.SingleLiveEvent
import kotlin.coroutines.CoroutineContext

/**
 * Created by Oleg Sitnikov on 2019-02-12
 */


class MapViewModel constructor(val paymentUseCase: PaymentUseCase) : ViewModel() {


    private val parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)

    // Ответ от Яндекс Кассы
    val paymentResponse = SingleLiveEvent<PaymentResponseItem>()

    fun getPayment(token: String, paymentModel: Payment) {
        scope.launch {
            val response = paymentUseCase.pay(token, paymentModel)
            paymentResponse.postValue(response?.mapToPresentation())
        }
    }


    private fun cancelAllRequests() = coroutineContext.cancel()

    override fun onCleared() {
        super.onCleared()
        cancelAllRequests()
    }

}