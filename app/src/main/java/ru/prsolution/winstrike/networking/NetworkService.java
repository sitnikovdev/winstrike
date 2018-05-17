package ru.prsolution.winstrike.networking;


import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import ru.prsolution.winstrike.common.logging.SignInModel;
import ru.prsolution.winstrike.common.logging.ConfirmModel;
import ru.prsolution.winstrike.common.logging.LoginModel;
import ru.prsolution.winstrike.common.logging.MessageResponse;
import ru.prsolution.winstrike.mvp.apimodels.AuthResponse;
import ru.prsolution.winstrike.mvp.apimodels.Orders;
import ru.prsolution.winstrike.mvp.apimodels.PaymentModel;
import ru.prsolution.winstrike.mvp.apimodels.PaymentResponse;
import ru.prsolution.winstrike.mvp.apimodels.RoomLayoutFactory;
import ru.prsolution.winstrike.mvp.apimodels.Rooms;
import ru.prsolution.winstrike.mvp.apimodels.ConfirmSmsModel;
import rx.Observable;

/**
 * Created by ennur on 6/25/16.
 */
public interface NetworkService {

    // Авторизация пользователя
    @POST("login")
    Observable<AuthResponse> authUser(@Body SignInModel signInModel);

    // Отправка смс c кодом подтверждения
    @POST("confirm_codes")
    Observable<MessageResponse> sendSmsByUserRequest(@Body ConfirmSmsModel confirmModel);

    // Создание пользователя
    @POST("users")
    Observable<AuthResponse> createUser(@Body LoginModel loginModel);


    // Подтверждение пользоватея по sms коду
    @POST("confirm_user/{sms_code}")
    Observable<MessageResponse> confirmUser(@Path("sms_code") String sms_code, @Body ConfirmModel confirmModel);

    // Получение активной арены по active pid layout
    @GET("rooms")
    Observable<Rooms> getActivePid();

    // Получение списка мест по  arena id (дефолтный диапазон времени на 30 мин)
    @GET("room_layouts/{active_layout_pid}")
    Observable<RoomLayoutFactory> getArena(@Path("active_layout_pid") String active_layout_pid);

    //Получение списка мест  по  id  и диапазону времени
    @GET("room_layouts/{active_layout_pid}")
    Observable<RoomLayoutFactory> getArenaByTimeRange(@Path("active_layout_pid") String active_layout_pid, @QueryMap Map<String, String> time);

    //  Создание платежа
    @POST("payments")
    Observable <PaymentResponse> getPayment(@Header("authorization") String token, @Body PaymentModel paymentModel);


    // Получение списка оплаченных мест пользователя
    @GET("orders")
    Observable<Orders> getOrders(@Header("authorization") String token);
}
