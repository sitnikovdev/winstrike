package ru.prsolution.winstrike.data.datasource

import io.reactivex.Single
import ru.prsolution.winstrike.domain.models.city.City

interface CityCacheDataSource {

    fun get(): Single<List<City>>

    fun set(list: List<City>): Single<List<City>>

}

interface CityRemoteDataSource {

    fun get(): Single<List<City>>

    fun get(postId: String): Single<City>
}
