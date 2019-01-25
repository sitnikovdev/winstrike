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
import ru.prsolution.winstrike.datasource.model.Arenas
import ru.prsolution.winstrike.datasource.model.AuthResponse
import ru.prsolution.winstrike.datasource.model.ConfirmSmsModel
import ru.prsolution.winstrike.datasource.model.NewPasswordModel
import ru.prsolution.winstrike.datasource.model.Orders
import ru.prsolution.winstrike.datasource.model.PaymentModel
import ru.prsolution.winstrike.datasource.model.PaymentResponse
import ru.prsolution.winstrike.datasource.model.RoomLayoutFactory
import ru.prsolution.winstrike.domain.models.ConfirmModel
import ru.prsolution.winstrike.domain.models.FCMModel
import ru.prsolution.winstrike.domain.models.LoginModel
import ru.prsolution.winstrike.domain.models.LoginViewModel
import ru.prsolution.winstrike.domain.models.MessageResponse
import ru.prsolution.winstrike.domain.models.ProfileModel

interface RetrofitService {

/*	// Получение списка ар
	@get:GET("rooms")
	val rooms: Deferred<Response<Rooms>>*/

	// Получение списка арен (новый API)
	@get:GET("rooms")
	val arenaList: Deferred<Response<Arenas>>

/*	// Получение списка мест по  arena id (дефолтный диапазон времени на 30 мин)
	@GET("room_layouts/{active_layout_pid}")
	fun getArena(@Path("active_layout_pid") active_layout_pid: String): Deferred<Response<RoomLayoutFactory>>*/

	//Получение арены по  id  и диапазону времени
	@GET("room_layouts/{active_layout_pid}")
	fun arenaAsync(@Path(
			"active_layout_pid") active_layout_pid: String?, @QueryMap time: Map<String, String>): Deferred<Response<RoomLayoutFactory>>

	// Авторизация пользователя
	@POST("login")
	fun authUserAsync(@Body loginViewModel: LoginViewModel?): Deferred<Response<AuthResponse>>

	// Отправка смс c кодом подтверждения
	@POST("confirm_codes")
	fun sendSmsByUserRequestAsync(@Body confirmModel: ConfirmSmsModel): Deferred<Response<MessageResponse>>

	// Повторная отправка пароля:
	@POST("refresh_password/{confirm_code}")
	fun refreshPassword(@Body confirmModel: NewPasswordModel, @Path(
			"confirm_code") confirm_code: String): Deferred<Response<MessageResponse>>

	// Создание пользователя
	@POST("users")
	fun createUser(@Body loginModel: LoginModel): Deferred<Response<AuthResponse>>

	// Update user profile
	@PUT("users/{public_id}")
	fun updateUser(@Header("authorization") token: String, @Body loginModel: ProfileModel, @Path(
			"public_id") public_id: String): Deferred<Response<MessageResponse>>

	// Send fcm tocken to server
	@POST("fcm_codes")
	fun sendTocken(@Header(
			"authorization") token: String, @Body tokenModel: FCMModel): Deferred<Response<MessageResponse>>


	// Подтверждение пользоватея по sms коду
	@POST("confirm_user/{sms_code}")
	fun confirmUser(@Path(
			"sms_code") sms_code: String, @Body confirmModel: ConfirmModel): Deferred<Response<MessageResponse>>

	//  Создание платежа
	@POST("payments")
	fun getPayment(@Header(
			"authorization") token: String, @Body paymentModel: PaymentModel): Deferred<Response<PaymentResponse>>


	// Получение списка оплаченных мест пользователя
	@GET("orders")
	fun getOrders(@Header("authorization") token: String): Deferred<Response<Orders>>
}
