package ru.prsolution.winstrike.data.datasource

import io.reactivex.Single
import ru.prsolution.winstrike.domain.models.Arena
import ru.prsolution.winstrike.domain.models.ArenaSchema
import ru.prsolution.winstrike.domain.models.payment.Payment
import ru.prsolution.winstrike.domain.models.payment.PaymentResponse

interface ArenaCacheDataSource {

    fun get(): Single<List<Arena>>

    fun set(list: List<Arena>?): Single<List<Arena>>

}

interface ArenaRemoteDataSource {

    suspend fun getArenas(): List<Arena>?

    suspend fun getSchema(arenaPid: String?, time: Map<String, String>): ArenaSchema?

    suspend fun getPayment(token: String, payment: Payment): PaymentResponse?

}
