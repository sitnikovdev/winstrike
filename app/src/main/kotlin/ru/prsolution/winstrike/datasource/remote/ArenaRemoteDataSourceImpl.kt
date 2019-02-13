package ru.prsolution.winstrike.datasource.remote

import ru.prsolution.winstrike.data.datasource.ArenaRemoteDataSource
import ru.prsolution.winstrike.data.repository.BaseRepository
import ru.prsolution.winstrike.datasource.model.mapToDomain
import ru.prsolution.winstrike.domain.models.Arena

class ArenaRemoteDataSourceImpl constructor(
    private val api: ArenaApi
) : ArenaRemoteDataSource, BaseRepository() {

    override suspend fun get(): List<Arena>? {
        val arenaResponse = safeApiCall(
                call = { api.getArenaAsync().await() },
                errorMessage = "Error Fetching Cities List"
        )

        return arenaResponse?.rooms?.map { it.mapToDomain() }

    }

}
