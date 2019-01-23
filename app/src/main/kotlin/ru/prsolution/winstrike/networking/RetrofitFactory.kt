package ru.prsolution.winstrike.networking

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.readystatesoftware.chuck.ChuckInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import ru.prsolution.winstrike.BuildConfig
import ru.prsolution.winstrike.WinstrikeApp
import java.util.concurrent.TimeUnit

/**
 * Created by Oleg Sitnikov on 2019-01-19
 */

object RetrofitFactory {

    fun makeRetrofitService(): RetrofitService {
        return Retrofit.Builder()
                .baseUrl(BuildConfig.DEBUGURL)
                .client(makeOkHttpClient())
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .build().create(RetrofitService::class.java)
    }

    private fun makeOkHttpClient(): OkHttpClient {
        return OkHttpClient().newBuilder()
                .addInterceptor(makeLoggingInterceptor())
                .addInterceptor( ChuckInterceptor(WinstrikeApp.instance))
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(90, TimeUnit.SECONDS)
                .build()

    }

    private fun makeLoggingInterceptor(): Interceptor {
        val logging = HttpLoggingInterceptor()
        logging.level =
                HttpLoggingInterceptor.Level.BODY
        return logging
    }
}