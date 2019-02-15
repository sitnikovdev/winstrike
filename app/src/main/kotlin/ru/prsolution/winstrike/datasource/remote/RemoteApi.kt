package ru.prsolution.winstrike.datasource.remote

import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap
import ru.prsolution.winstrike.datasource.model.ArenaListEntity
import ru.prsolution.winstrike.datasource.model.SchemaEntity
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


    // Получение арены по  id  и диапазону времени
    @GET("room_layouts/{active_layout_pid}")
    fun getSchemaAsync(
        @Path(
            "active_layout_pid") active_layout_pid: String?,
        @QueryMap time: Map<String, String>
    ): Deferred<Response<SchemaEntity>>
}

