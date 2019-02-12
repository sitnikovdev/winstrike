package ru.prsolution.winstrike.datasource.model.city

import com.squareup.moshi.Json
import ru.prsolution.winstrike.domain.models.city.City

class CityListEntity(
    @Json(name = "cities")
    val cities: List<CityEntity>
)

class CityEntity(
    @Json(name = "city_name")
    val cityName: String,
    @Json(name = "public_id")
    val publicId: String
)

fun CityEntity.mapToDomain(): City = City(
    name = cityName,
    publicId = publicId
)

fun List<CityEntity>.mapToDomain(): List<City> = map { it.mapToDomain() }

