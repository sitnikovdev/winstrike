package ru.prsolution.winstrike.di.module;


import android.content.Context;
import com.readystatesoftware.chuck.ChuckInterceptor;
import java.io.File;
import java.io.IOException;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import ru.prsolution.winstrike.BuildConfig;
import ru.prsolution.winstrike.networking.NetworkService;
import ru.prsolution.winstrike.networking.Service;
import ru.prsolution.winstrike.utils.Constants;


@Module
public class NetworkModule {
    File cacheFile;
    Context context;

    public NetworkModule(File cacheFile, Context context) {
        this.cacheFile = cacheFile;
        this.context = context;
    }

    @Provides
    @Singleton
   public Retrofit provideCall() {
        Cache cache = null;
        try {
            cache = new Cache(cacheFile, 10 * 1024 * 1024);
        } catch (Exception e) {
            e.printStackTrace();
        }

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request original = chain.request();

                    // Customize the request
                    Request request = original.newBuilder()
                            .header("Content-Type", "application/json")
                            .header("host", Constants.URL_WINSTRIKE_HOST)
                            .removeHeader("Pragma")
                            .build();

                    okhttp3.Response response = chain.proceed(request);
                    response.cacheResponse();
                    // Customize or return the response
                    return response;
                })
                .addInterceptor(new ChuckInterceptor(context))
                .cache(cache)
                .build();

      //Chuck

        return new Retrofit.Builder()
                .baseUrl(BuildConfig.BASEURL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

    }

    @Provides
    @Singleton
    public NetworkService providesNetworkService(
             Retrofit retrofit) {
        return retrofit.create(NetworkService.class);
    }
    @Provides
    @Singleton
    public Service providesService(
            NetworkService networkService) {
        return new Service(networkService);
    }

}
