package ru.prsolution.winstrike.datasource.remote

import ru.prsolution.winstrike.data.datasource.CityRemoteDataSource
import ru.prsolution.winstrike.datasource.model.city.mapToDomain
import ru.prsolution.winstrike.domain.models.city.City

class CityRemoteDataSourceImpl constructor(
    private val api: CityApi
) : CityRemoteDataSource, BaseRepository() {

    override suspend fun get(): List<City>? {
        val cityResponse = safeApiCall(
                call = { api.getCityAsync().await() },
                errorMessage = "Error Fetching Cities List"
        )

        return cityResponse?.cities?.map { it.mapToDomain() }

    }

}
