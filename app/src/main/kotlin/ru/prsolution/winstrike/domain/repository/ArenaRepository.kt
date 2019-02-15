package ru.prsolution.winstrike.domain.repository

import ru.prsolution.winstrike.domain.models.arena.Arena
import ru.prsolution.winstrike.domain.models.arena.ArenaSchema
import ru.prsolution.winstrike.domain.models.payment.Payment
import ru.prsolution.winstrike.domain.models.payment.PaymentResponse

interface ArenaRepository {

    suspend fun get(refresh: Boolean): List<Arena>?

    suspend fun get(arenaPid: String?, time: Map<String, String>, refresh: Boolean): ArenaSchema?

    suspend fun get(token: String, paymentModel: Payment): PaymentResponse?
}
