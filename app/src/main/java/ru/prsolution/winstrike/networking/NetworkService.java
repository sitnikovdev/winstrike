package ru.prsolution.winstrike.networking;


import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import ru.prsolution.winstrike.datasource.model.Arenas;
import ru.prsolution.winstrike.datasource.model.AuthResponse;
import ru.prsolution.winstrike.datasource.model.ConfirmSmsModel;
import ru.prsolution.winstrike.datasource.model.NewPasswordModel;
import ru.prsolution.winstrike.datasource.model.Orders;
import ru.prsolution.winstrike.datasource.model.PaymentModel;
import ru.prsolution.winstrike.datasource.model.PaymentResponse;
import ru.prsolution.winstrike.datasource.model.RoomLayoutFactory;
import ru.prsolution.winstrike.datasource.model.Rooms;
import ru.prsolution.winstrike.domain.models.FCMModel;
import ru.prsolution.winstrike.domain.models.LoginViewModel;
import ru.prsolution.winstrike.domain.models.ConfirmModel;
import ru.prsolution.winstrike.domain.models.LoginModel;
import ru.prsolution.winstrike.domain.models.MessageResponse;
import ru.prsolution.winstrike.domain.models.ProfileModel;
import rx.Observable;

public interface NetworkService {

  // Получение арены
  @GET("rooms")
  Observable<Rooms> getRooms();

  // Получение списка арен (новый API)
  @GET("rooms")
  Observable<Arenas> getArenas();

  // Получение списка мест по  arena id (дефолтный диапазон времени на 30 мин)
  @GET("room_layouts/{active_layout_pid}")
  Observable<RoomLayoutFactory> getArena(@Path("active_layout_pid") String active_layout_pid);

  //Получение списка мест  по  id  и диапазону времени
  @GET("room_layouts/{active_layout_pid}")
  Observable<RoomLayoutFactory> getArenaByTimeRange(@Path("active_layout_pid") String active_layout_pid, @QueryMap Map<String, String> time);

  // Авторизация пользователя
  @POST("login")
  Observable<AuthResponse> authUser(@Body LoginViewModel loginViewModel);

  // Отправка смс c кодом подтверждения
  @POST("confirm_codes")
  Observable<MessageResponse> sendSmsByUserRequest(@Body ConfirmSmsModel confirmModel);

  // Повторная отправка пароля:
  @POST("refresh_password/{confirm_code}")
  Observable<MessageResponse> refreshPassword(@Body NewPasswordModel confirmModel, @Path("confirm_code") String confirm_code);

  // Создание пользователя
  @POST("users")
  Observable<AuthResponse> createUser(@Body LoginModel loginModel);

  // Update user profile
  @PUT("users/{public_id}")
  Observable<MessageResponse> updateUser(@Header("authorization") String token, @Body ProfileModel loginModel, @Path("public_id") String public_id);

  // Send fcm tocken to server
  @POST("fcm_codes")
  Observable<MessageResponse> sendTocken(@Header("authorization") String token, @Body FCMModel tokenModel);


  // Подтверждение пользоватея по sms коду
  @POST("confirm_user/{sms_code}")
  Observable<MessageResponse> confirmUser(@Path("sms_code") String sms_code, @Body ConfirmModel confirmModel);

  //  Создание платежа
  @POST("payments")
  Observable<PaymentResponse> getPayment(@Header("authorization") String token, @Body PaymentModel paymentModel);


  // Получение списка оплаченных мест пользователя
  @GET("orders")
  Observable<Orders> getOrders(@Header("authorization") String token);
}
