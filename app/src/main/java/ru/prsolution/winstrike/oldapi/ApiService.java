package ru.prsolution.winstrike.oldapi;

import org.json.JSONObject;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import ru.prsolution.winstrike.common.logging.SignInModel;
import ru.prsolution.winstrike.common.logging.ConfirmModel;
import ru.prsolution.winstrike.common.logging.LoginModel;
import ru.prsolution.winstrike.common.logging.MessageResponse;
import ru.prsolution.winstrike.common.logging.UsersServices;
import ru.prsolution.winstrike.mvp.apimodels.AuthResponse;
import ru.prsolution.winstrike.mvp.apimodels.PaymentModel;
import ru.prsolution.winstrike.mvp.apimodels.RoomLayoutFactory;
import ru.prsolution.winstrike.mvp.apimodels.ConfirmSmsModel;

/**
 * Created by designer on 06/03/2018.
 */

public interface ApiService {

    // Users
    @POST("users")
    Single<Response<AuthResponse>> create(@Body LoginModel loginModel);

    @POST("confirm_user/{sms_code}")
    Single<Response<MessageResponse>> confirm(@Path("sms_code") String sms_code, @Body ConfirmModel confirmModel);

    @POST("confirm_codes")
    Single<Response<MessageResponse>> sendConfirmCode(@Body ConfirmSmsModel loginModel);

    @POST("login")
    Single<Response<AuthResponse>> login(@Body SignInModel signInModel);

    @GET("users")
    Single <Response<UsersServices>> getUsersList();

    @DELETE("users/{id}")
    Single <Response<MessageResponse>>  delete(@Path("id") String id);

    // Create Payment
    @POST("payments")
    Single <Response<List>> createPayment(@Body PaymentModel paymentModel);


    // Список мест
    @GET("rooms")
    Observable<Response<JSONObject>> getMap();

    // Получить арену по  id
    @GET("room_layouts/{active_layout_pid}")
    Single <Response<RoomLayoutFactory>> getArena(@Path("active_layout_pid") String active_layout_pid);

}
