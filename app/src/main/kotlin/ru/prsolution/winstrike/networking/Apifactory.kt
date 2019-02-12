package ru.prsolution.winstrike.networking

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.readystatesoftware.chuck.ChuckInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import ru.prsolution.winstrike.App
import ru.prsolution.winstrike.BuildConfig
import ru.prsolution.winstrike.datasource.remote.ArenaApi
import ru.prsolution.winstrike.datasource.remote.CityApi
import java.util.concurrent.TimeUnit

/**
 * Created by Oleg Sitnikov on 2019-02-12
 */

object ApiFactory{

    //Creating Auth Interceptor to add api_key query in front of all the requests.
    private val authInterceptor = Interceptor {chain->
        val newUrl = chain.request().url()
                .newBuilder()
//                .addQueryParameter("api_key", AppConstants.tmdbApiKey)
                .build()

        val newRequest = chain.request()
                .newBuilder()
                .url(newUrl)
                .build()

        chain.proceed(newRequest)
    }


    private fun makeLoggingInterceptor(): Interceptor {
        val logging = HttpLoggingInterceptor()
        logging.level =
                HttpLoggingInterceptor.Level.BODY
        return logging
    }

    //OkhttpClient for building http request url
    private val apiClient = OkHttpClient().newBuilder()
//            .addInterceptor(authInterceptor)
            .addInterceptor(ChuckInterceptor(App.instance))
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(90, TimeUnit.SECONDS)
            .build()



    fun retrofit() : Retrofit = Retrofit.Builder()
            .client(apiClient)
            .baseUrl(BuildConfig.DEBUGURL)
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()


    val cityApi : CityApi = retrofit().create(CityApi::class.java)

    val arenaApi : ArenaApi = retrofit().create(ArenaApi::class.java)

}

