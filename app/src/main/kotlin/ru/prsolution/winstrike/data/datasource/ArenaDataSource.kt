package ru.prsolution.winstrike.data.datasource

import io.reactivex.Single
import ru.prsolution.winstrike.domain.models.Arena
import ru.prsolution.winstrike.domain.models.ArenaSchema

interface ArenaCacheDataSource {

    fun get(): Single<List<Arena>>

    fun set(list: List<Arena>?): Single<List<Arena>>

}

interface ArenaRemoteDataSource {

   suspend fun getArenas(): List<Arena>?

   suspend fun getSchema(arenaPid: String?, time: Map<String, String>): ArenaSchema?

}
