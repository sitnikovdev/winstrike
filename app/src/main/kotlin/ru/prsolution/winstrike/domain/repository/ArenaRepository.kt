package ru.prsolution.winstrike.domain.repository

import ru.prsolution.winstrike.domain.models.Arena
import ru.prsolution.winstrike.domain.models.ArenaSchema

interface ArenaRepository {

   suspend fun get(refresh: Boolean): List<Arena>?

   suspend fun get(arenaPid: String?, time: Map<String, String>,refresh: Boolean): ArenaSchema?
}
