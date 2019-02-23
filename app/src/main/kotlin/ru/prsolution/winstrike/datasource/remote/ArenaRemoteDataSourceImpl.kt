package ru.prsolution.winstrike.datasource.remote

import ru.prsolution.winstrike.data.datasource.ArenaRemoteDataSource
import ru.prsolution.winstrike.data.repository.resouces.Resource
import ru.prsolution.winstrike.datasource.model.arena.ScheduleEntity
import ru.prsolution.winstrike.datasource.model.arena.SchedulersEntity
import ru.prsolution.winstrike.datasource.model.arena.mapToDomain
import ru.prsolution.winstrike.datasource.model.orders.mapToDomain
import ru.prsolution.winstrike.datasource.model.payment.PaymentEntity
import ru.prsolution.winstrike.datasource.model.payment.mapToDomain
import ru.prsolution.winstrike.domain.models.arena.Arena
import ru.prsolution.winstrike.domain.models.arena.ArenaSchema
import ru.prsolution.winstrike.domain.models.arena.Schedule
import ru.prsolution.winstrike.domain.models.orders.OrderModel
import ru.prsolution.winstrike.domain.models.payment.PaymentModel
import ru.prsolution.winstrike.domain.models.payment.PaymentResponse
import ru.prsolution.winstrike.presentation.utils.pref.PrefUtils.token

class ArenaRemoteDataSourceImpl constructor(
    private val api: ArenaApi
) : ArenaRemoteDataSource, BaseRepository() {


    override suspend fun getArenas(): Resource<List<Arena>>? {
        val arenaResponse = safeApiCall(
            call = { api.getArenaAsync().await() },
            errorMessage = "Error Fetching Cities List"
        )

        return arenaResponse?.mapToDomain()
    }

    override suspend fun getSchema(arenaPid: String?, time: Map<String, String>): Resource<ArenaSchema>? {
        val response = safeApiCall(
            call = { api.getSchemaAsync(arenaPid, time).await() },
            errorMessage = "Error Fetching Cities List"
        )

        return response?.mapToDomain()
    }

    override suspend fun getPayment(payment: PaymentEntity): Resource<PaymentResponse>? {
        val response = safeApiCall(
            call = { api.getPaymentAsync(payment).await() },
            errorMessage = "Error pay payment response"
        )

        return response?.mapToDomain()
    }

    override suspend fun getSchedule(): Resource<List<Schedule>>? {
        val response = safeApiCall(
            call = { api.getSchedules().await() },
            errorMessage = "Error pay payment response"
        )

        return response?.mapToDomain()
    }


    override suspend fun getOrders(): Resource<List<OrderModel>>? {
        val response = safeApiCall(
            call = { api.getOrders().await() },
            errorMessage = "Error pay payment response"
        )

        return response?.mapToDomain()
    }



}
