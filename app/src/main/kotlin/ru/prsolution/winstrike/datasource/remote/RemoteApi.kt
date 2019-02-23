package ru.prsolution.winstrike.datasource.remote

import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.*
import ru.prsolution.winstrike.datasource.model.arena.ArenaListEntity
import ru.prsolution.winstrike.datasource.model.arena.ScheduleEntity
import ru.prsolution.winstrike.datasource.model.arena.SchedulersEntity
import ru.prsolution.winstrike.datasource.model.arena.SchemaEntity
import ru.prsolution.winstrike.datasource.model.city.CityListEntity
import ru.prsolution.winstrike.datasource.model.login.*
import ru.prsolution.winstrike.datasource.model.orders.OrdersListEntity
import ru.prsolution.winstrike.datasource.model.payment.PaymentEntity
import ru.prsolution.winstrike.datasource.model.payment.PaymentResponseEntity
import ru.prsolution.winstrike.domain.models.common.FCMModel
import ru.prsolution.winstrike.domain.models.common.MessageResponse

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
        @Body payment: PaymentEntity
    ): Deferred<Response<PaymentResponseEntity>>

    // Расписание работы клубов
    @GET("schedules")
    fun getSchedules(): Deferred<Response<SchedulersEntity>>


    // Получение списка оплаченных мест пользователя
    @GET("orders")
    fun getOrders(): Deferred<Response<OrdersListEntity>>

    // Send fcm tocken to server
    @POST("fcm_codes")
    fun sendTockenAsync(
        @Header(
            "authorization"
        ) token: String,
        @Body tokenModel: FCMModel
    ): Deferred<Response<MessageResponse>>

}

interface UserApi {
    // Создание пользователя
    @POST("users")
    fun getUser(@Body newUserEntity: NewUserEntity): Deferred<Response<AuthResponseEntity>>

    // Подтверждение пользоватея по sms коду
    @POST("confirm_user/{sms_code}")
    fun confirmUser(
        @Path(
            "sms_code") sms_code: String,
        @Body confirmModel:SmsEntity
    ): Deferred<Response<MessageResponse>>


    // Авторизация пользователя
    @POST("login")
    fun getLogin(@Body loginModel: LoginEntity): Deferred<Response<AuthResponseEntity>>

    // Отправка смс c кодом подтверждения (повторно)
    @POST("confirm_codes")
    fun sendSms(@Body confirmModel: SmsEntity): Deferred<Response<MessageResponse>>


    // Обновление профиля (имя пользователя)
    @PUT("users/{public_id}")
    fun updateUser(
        @Path(
            "public_id") public_id: String,
        @Body profileEntity: ProfileEntity
    ): Deferred<Response<MessageResponse>>

}

