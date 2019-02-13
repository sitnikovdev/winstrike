package ru.prsolution.winstrike.data.datasource

import io.reactivex.Single
import ru.prsolution.winstrike.domain.models.Arena

interface ArenaCacheDataSource {

    fun get(): Single<List<Arena>>

    fun set(list: List<Arena>?): Single<List<Arena>>

}

interface ArenaRemoteDataSource {

   suspend fun get(): List<Arena>?

}
