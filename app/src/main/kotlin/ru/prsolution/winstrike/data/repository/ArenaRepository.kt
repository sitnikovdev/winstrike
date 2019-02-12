package ru.prsolution.winstrike.data.repository

import ru.prsolution.winstrike.datasource.model.mapToDomain
import ru.prsolution.winstrike.datasource.remote.ArenaApi
import ru.prsolution.winstrike.domain.models.Arena

/**
 * Created by Oleg Sitnikov on 2019-02-12
 */

class ArenaRepository(private val api: ArenaApi) : BaseRepository() {

    suspend fun get(): List<Arena>? {

        val arenaResponse = safeApiCall(
                call = { api.getArenaAsync().await() },
                errorMessage = "Error Fetching Cities List"
        )

        return arenaResponse?.rooms?.map { it.mapToDomain() }

    }

}

