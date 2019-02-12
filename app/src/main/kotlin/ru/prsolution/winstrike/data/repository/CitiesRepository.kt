package ru.prsolution.winstrike.data.repository

import ru.prsolution.winstrike.datasource.model.city.mapToDomain
import ru.prsolution.winstrike.datasource.remote.CityApi
import ru.prsolution.winstrike.domain.models.city.City

/**
 * Created by Oleg Sitnikov on 2019-02-12
 */

class CityRepository(private val api: CityApi) : BaseRepository() {

    suspend fun get(): List<City>? {

        val cityResponse = safeApiCall(
                call = { api.getCityAsync().await() },
                errorMessage = "Error Fetching Cities List"
        )

        return cityResponse?.cities?.map { it.mapToDomain() }

    }

}

