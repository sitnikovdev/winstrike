package ru.prsolution.winstrike.datasource.model.city

import com.squareup.moshi.Json
import ru.prsolution.winstrike.domain.models.city.City

class CityListEntity(
        @field:Json(name = "cities")
        val cities: List<CityEntity>
)

class CityEntity(
        @field:Json(name = "city_name")
        val cityName: String,
        @field:Json(name = "public_id")
        val publicId: String
)

fun CityEntity.mapToDomain(): City = City(
        publicId = publicId,
        name = cityName
        )

fun List<CityEntity>.mapToDomain(): List<City> = map {
        it.mapToDomain() }

