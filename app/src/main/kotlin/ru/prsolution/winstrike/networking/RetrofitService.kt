package ru.prsolution.winstrike.networking

import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.QueryMap
import ru.prsolution.winstrike.datasource.model.ArenaListEntity
import ru.prsolution.winstrike.datasource.model.login.AuthResponse
import ru.prsolution.winstrike.datasource.model.login.ConfirmSmsModel
import ru.prsolution.winstrike.datasource.model.login.NewPasswordModel
import ru.prsolution.winstrike.datasource.model.orders.OrdersEntity
import ru.prsolution.winstrike.domain.payment.PaymentModel
import ru.prsolution.winstrike.domain.payment.PaymentResponse
import ru.prsolution.winstrike.datasource.model.SchemaEntity
import ru.prsolution.winstrike.datasource.model.city.CityListEntity
import ru.prsolution.winstrike.domain.models.login.ConfirmModel
import ru.prsolution.winstrike.domain.models.common.FCMModel
import ru.prsolution.winstrike.domain.models.login.LoginModel
import ru.prsolution.winstrike.domain.models.login.LoginViewModel
import ru.prsolution.winstrike.domain.models.common.MessageResponse
import ru.prsolution.winstrike.domain.models.login.ProfileModel

interface RetrofitService {

/*    // Получение  списка городов
    @GET("cities")
    fun cityListAsync(): Deferred<Response<CityListEntity>>*/

    // Получение  списка имеющихся арен
    @GET("rooms")
    fun arenaListAsync(): Deferred<Response<ArenaListEntity>>

    // Получение арены по  id  и диапазону времени
    @GET("room_layouts/{active_layout_pid}")
    fun arenaSchemaAsync(
        @Path(
"active_layout_pid") active_layout_pid: String?,
        @QueryMap time: Map<String, String>
    ):
            Deferred<Response<SchemaEntity>>

    // Авторизация пользователя
    @POST("login")
    fun authUserAsync(@Body loginViewModel: LoginViewModel?): Deferred<Response<AuthResponse>>

    // Отправка смс c кодом подтверждения
    @POST("confirm_codes")
    fun sendSmsByUserRequestAsync(@Body confirmModel: ConfirmSmsModel): Deferred<Response<MessageResponse>>

    //  Создание платежа для указанных мест и диапазона времени.
    //  В случае успеха в ответ приходит ссылка на Яндекс кассу для оплаты.
    @POST("payments")
    fun getPaymentAsync(
        @Header(
"authorization") token: String,
        @Body paymentModel: PaymentModel
    ): Deferred<Response<PaymentResponse>>

    // Повторная отправка пароля:
    @POST("refresh_password/{confirm_code}")
    fun refreshPassword(
        @Body confirmModel: NewPasswordModel,
        @Path(
                    "confirm_code") confirm_code: String
    ): Deferred<Response<MessageResponse>>

    // Создание пользователя
    @POST("users")
    fun createUser(@Body loginModel: LoginModel): Deferred<Response<AuthResponse>>

    // Update user profile
    @PUT("users/{public_id}")
    fun updateUser(
        @Header("authorization") token: String,
        @Body loginModel: ProfileModel,
        @Path(
                    "public_id") public_id: String
    ): Deferred<Response<MessageResponse>>

    // Send fcm tocken to server
    @POST("fcm_codes")
    fun sendTockenAsync(
        @Header(
"authorization") token: String,
        @Body tokenModel: FCMModel
    ): Deferred<Response<MessageResponse>>

    // Подтверждение пользоватея по sms коду
    @POST("confirm_user/{sms_code}")
    fun confirmUser(
        @Path(
"sms_code") sms_code: String,
        @Body confirmModel: ConfirmModel
    ): Deferred<Response<MessageResponse>>

    // Получение списка оплаченных мест пользователя
    @GET("orders")
    fun getOrders(@Header("authorization") token: String): Deferred<Response<OrdersEntity>>
}
