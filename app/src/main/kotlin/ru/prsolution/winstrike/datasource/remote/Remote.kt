package ru.prsolution.winstrike.datasource.remote

import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import ru.prsolution.winstrike.datasource.model.ArenaListEntity
import ru.prsolution.winstrike.datasource.model.city.CityListEntity

interface CityApi {
    // Получение  списка городов
    @GET("cities")
    fun getCityAsync(): Deferred<Response<CityListEntity>>
}

interface ArenaApi {
    // Получение  списка имеющихся арен
    @GET("rooms")
    fun getArenaAsync(): Deferred<Response<ArenaListEntity>>
}

