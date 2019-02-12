package ru.prsolution.winstrike.datasource.remote

import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import ru.prsolution.winstrike.datasource.model.city.CityListEntity

interface CityApi {

    // Получение  списка городов
    @GET("cities")
    fun getCityAsync(): Deferred<Response<CityListEntity>>

/*    @GET("posts/")
    fun getCitys(): Single<List<CityEntity>>

    @GET("posts/{id}")
    fun getCity(@Path("id") postId: String): Single<CityEntity>*/
}

interface UsersApi {

/*    @GET("users/")
    fun getUsers(): Single<List<UserEntity>>

    @GET("users/{id}")
    fun getUser(@Path("id") userId: String): Single<UserEntity>*/
}

interface CommentsApi {

/*    @GET("comments/")
    fun getComments(@Query("postId") postId: String): Single<List<CommentEntity>>*/
}
