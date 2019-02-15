package ru.prsolution.winstrike.datasource.remote

import ru.prsolution.winstrike.data.datasource.ArenaRemoteDataSource
import ru.prsolution.winstrike.datasource.model.mapRoomToDomain
import ru.prsolution.winstrike.datasource.model.mapToDomain
import ru.prsolution.winstrike.domain.models.Arena
import ru.prsolution.winstrike.domain.models.ArenaSchema

class ArenaRemoteDataSourceImpl constructor(
    private val api: ArenaApi
) : ArenaRemoteDataSource, BaseRepository() {


    override suspend fun getArenas(): List<Arena>? {
        val arenaResponse = safeApiCall(
            call = { api.getArenaAsync().await() },
            errorMessage = "Error Fetching Cities List"
        )

        return arenaResponse?.rooms?.map { it.mapToDomain() }
    }

    override suspend fun getSchema(arenaPid: String?, time: Map<String, String>): ArenaSchema? {
        val response = safeApiCall(
            call = { api.getSchemaAsync(arenaPid, time).await() },
            errorMessage = "Error Fetching Cities List"
        )

        return response?.roomLayout?.mapRoomToDomain()
    }

}
