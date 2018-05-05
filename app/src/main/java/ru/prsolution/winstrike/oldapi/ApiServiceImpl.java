package ru.prsolution.winstrike.oldapi;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import io.reactivex.android.schedulers.AndroidSchedulers;
import ru.prsolution.winstrike.common.utils.TinyDB;
import timber.log.Timber;

public class ApiServiceImpl {
    private static TinyDB tinyDB;
    private static ApiService apiService;
    private static Context mContent;


    private static ApiServiceImpl newInstance = new ApiServiceImpl();


    public static ApiServiceImpl getNewInstance(Context c) {
        mContent = c;
        tinyDB = new TinyDB(c);
        apiService = ServiceGenerator.createService(ApiService.class, c);
        return newInstance;
    }


    public ApiService getApi() {
        return apiService;
    }


// Получение списка пользователей
//        getUsersList();

// Удаление пользователя
/*        String public_id = tinyDB.getString("public_id");
        deleteUser(public_id);*/

// Основные методы используемые в приложении
// Создание пользователя
/*      LoginModel loginModel = new LoginModel();
        loginModel.setPhone("+79520757099");
        loginModel.setEnd_at("test");
        tinyDB.putString("password",loginModel.getEnd_at());
        createNewUser(loginModel);*/

// Подтверждение пользователя
/*        String sms_code = "um232y";
        sendCode(sms_code);*/

//  Аутентификация пользователя
/*
        SignInModel authModel = new SignInModel();
        authModel.setStartAt("+79520757099");
        authModel.setEnd_at("test");
        ApiServiceImpl(authModel);
*/

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    public static void getUsersList() {
        apiService.getUsersList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(users -> {
                    Timber.tag("OkHttp").d("ApiServiceImpl: getUsersList - sizeOf: %s", users.body().getUsers().size());
                    tinyDB.putString("operation", "users");
                }, Throwable::printStackTrace);

    }


    public static void deleteUser(String id) {
        apiService.delete(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(message -> {
                    Timber.tag("OkHttp").d("deleteUser: %s", message.message());
                    tinyDB.putString("operation", "delete");
                    tinyDB.putString("public_id", "");
                    tinyDB.putString("token", "");
                    tinyDB.putBoolean("confirmed", false);
                    tinyDB.putBoolean("loggedin", false);
                });

    }

}
