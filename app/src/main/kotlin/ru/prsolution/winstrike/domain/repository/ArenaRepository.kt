package ru.prsolution.winstrike.domain.repository

import ru.prsolution.winstrike.domain.models.Arena

interface ArenaRepository {

   suspend fun get(refresh: Boolean): List<Arena>?
}
