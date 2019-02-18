package ru.prsolution.winstrike.domain.usecases

import ru.prsolution.winstrike.data.repository.resouces.Resource
import ru.prsolution.winstrike.domain.models.payment.Payment
import ru.prsolution.winstrike.domain.models.payment.PaymentResponse
import ru.prsolution.winstrike.domain.repository.ArenaRepository

class PaymentUseCase constructor(private val arenaRepository: ArenaRepository) {

    suspend fun pay(token: String, paymentModel: Payment): Resource<PaymentResponse>? =
        arenaRepository.get(token, paymentModel)
}
