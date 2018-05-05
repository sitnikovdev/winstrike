package ru.prsolution.winstrike.networking;

import android.content.Context;

import ru.prsolution.winstrike.oldapi.ApiService;
import ru.prsolution.winstrike.oldapi.ServiceGenerator;
import ru.prsolution.winstrike.common.utils.TinyDB;

public class NetworkServiceImpl {
    private static TinyDB tinyDB;
    private static ApiService apiService;
    private static Context mContent;


    private static NetworkServiceImpl newInstance = new NetworkServiceImpl();


    public static NetworkServiceImpl getNewInstance(Context c) {
        mContent = c;
        tinyDB = new TinyDB(c);
        apiService = ServiceGenerator.createService(ApiService.class, c);
        return newInstance;
    }


    public ApiService getApi() {
        return apiService;
    }


}
