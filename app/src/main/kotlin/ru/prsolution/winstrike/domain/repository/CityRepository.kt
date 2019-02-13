package ru.prsolution.winstrike.domain.repository

import ru.prsolution.winstrike.domain.models.city.City

interface CityRepository {

   suspend fun get(): List<City>?
}
