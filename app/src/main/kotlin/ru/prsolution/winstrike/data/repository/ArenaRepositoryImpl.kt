package ru.prsolution.winstrike.data.repository

import ru.prsolution.winstrike.data.datasource.ArenaCacheDataSource
import ru.prsolution.winstrike.data.datasource.ArenaRemoteDataSource
import ru.prsolution.winstrike.domain.models.Arena
import ru.prsolution.winstrike.domain.repository.ArenaRepository

class ArenaRepositoryImpl constructor(
        private val cacheDataSource: ArenaCacheDataSource,
        private val remoteDataSource: ArenaRemoteDataSource
) : ArenaRepository {


    override suspend fun get(refresh: Boolean): List<Arena>? =
            remoteDataSource.get()
}

