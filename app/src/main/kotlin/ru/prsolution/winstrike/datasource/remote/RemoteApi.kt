package ru.prsolution.winstrike.datasource.remote

import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.*
import ru.prsolution.winstrike.datasource.model.ArenaListEntity
import ru.prsolution.winstrike.datasource.model.SchemaEntity
import ru.prsolution.winstrike.datasource.model.city.CityListEntity
import ru.prsolution.winstrike.datasource.model.payment.PaymentEntity
import ru.prsolution.winstrike.datasource.model.payment.PaymentResponseEntity
import ru.prsolution.winstrike.domain.models.payment.Payment
import ru.prsolution.winstrike.presentation.model.payment.PaymentResponseItem

interface CityApi {
    // Получение  списка городов
    @GET("cities")
    fun getCityAsync(): Deferred<Response<CityListEntity>>
}

interface ArenaApi {
    // Получение  списка имеющихся арен
    @GET("rooms")
    fun getArenaAsync(): Deferred<Response<ArenaListEntity>>


    // Получение арены по  id  и диапазону времени
    @GET("room_layouts/{active_layout_pid}")
    fun getSchemaAsync(
        @Path(
            "active_layout_pid"
        ) active_layout_pid: String?,
        @QueryMap time: Map<String, String>
    ): Deferred<Response<SchemaEntity>>

    //  Создание платежа для указанных мест и диапазона времени.
    //  В случае успеха в ответ приходит ссылка на Яндекс кассу для оплаты.
    @POST("payments")
    fun getPaymentAsync(
        @Header(
            "authorization"
        ) token: String,
        @Body payment: Payment
    ): Deferred<Response<PaymentResponseEntity>>

}

