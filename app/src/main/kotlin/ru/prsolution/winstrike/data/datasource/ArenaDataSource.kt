package ru.prsolution.winstrike.data.datasource

import io.reactivex.Single
import ru.prsolution.winstrike.data.repository.resouces.Resource
import ru.prsolution.winstrike.domain.models.arena.Arena
import ru.prsolution.winstrike.domain.models.arena.ArenaSchema
import ru.prsolution.winstrike.domain.models.payment.Payment
import ru.prsolution.winstrike.domain.models.payment.PaymentResponse

interface ArenaCacheDataSource {

    fun get(): Single<List<Arena>>

    fun set(list: List<Arena>?): Single<List<Arena>>

}

interface ArenaRemoteDataSource {

    suspend fun getArenas(): Resource<List<Arena>>?

    suspend fun getSchema(arenaPid: String?, time: Map<String, String>): Resource<ArenaSchema>?

    suspend fun getPayment(token: String, payment: Payment): Resource<PaymentResponse>?

}
