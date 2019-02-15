package ru.prsolution.winstrike.datasource.remote

import ru.prsolution.winstrike.data.datasource.ArenaRemoteDataSource
import ru.prsolution.winstrike.datasource.model.arena.mapRoomToDomain
import ru.prsolution.winstrike.datasource.model.arena.mapToDomain
import ru.prsolution.winstrike.datasource.model.payment.mapToDomain
import ru.prsolution.winstrike.domain.models.arena.Arena
import ru.prsolution.winstrike.domain.models.arena.ArenaSchema
import ru.prsolution.winstrike.domain.models.payment.Payment
import ru.prsolution.winstrike.domain.models.payment.PaymentResponse

class ArenaRemoteDataSourceImpl constructor(
    private val api: ArenaApi
) : ArenaRemoteDataSource, BaseRepository() {


    override suspend fun getArenas(): List<Arena>? {
        val arenaResponse = safeApiCall(
            call = { api.getArenaAsync().await() },
            errorMessage = "Error Fetching Cities List"
        )

        return arenaResponse?.rooms?.map { it.mapToDomain() }
    }

    override suspend fun getSchema(arenaPid: String?, time: Map<String, String>): ArenaSchema? {
        val response = safeApiCall(
            call = { api.getSchemaAsync(arenaPid, time).await() },
            errorMessage = "Error Fetching Cities List"
        )

        return response?.roomLayout?.mapRoomToDomain()
    }

    override suspend fun getPayment(token: String, payment: Payment): PaymentResponse? {
        val response = safeApiCall(
            call = { api.getPaymentAsync(token, payment).await() },
            errorMessage = "Error get payment response"
        )

        return response?.mapToDomain()
    }


}
