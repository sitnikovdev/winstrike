package ru.prsolution.winstrike.domain.repository

import ru.prsolution.winstrike.data.repository.resouces.Resource
import ru.prsolution.winstrike.domain.models.arena.Arena
import ru.prsolution.winstrike.domain.models.arena.ArenaSchema
import ru.prsolution.winstrike.domain.models.arena.Schedule
import ru.prsolution.winstrike.domain.models.payment.PaymentModel
import ru.prsolution.winstrike.domain.models.payment.PaymentResponse

interface ArenaRepository {

    suspend fun get(refresh: Boolean): Resource<List<Arena>>?

    suspend fun get(arenaPid: String?, time: Map<String, String>, refresh: Boolean): Resource<ArenaSchema>?

    suspend fun pay(paymentModel: PaymentModel): Resource<PaymentResponse>?

    suspend fun getSchedule(): Resource<List<Schedule>>?
}
