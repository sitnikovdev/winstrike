package ru.prsolution.winstrike.domain.usecases

import ru.prsolution.winstrike.domain.models.Arena
import ru.prsolution.winstrike.domain.models.ArenaSchema
import ru.prsolution.winstrike.domain.repository.ArenaRepository

class ArenaUseCase constructor(private val arenaRepository: ArenaRepository) {

    suspend fun get(refresh: Boolean): List<Arena>? =
        arenaRepository.get(refresh)


    suspend fun get(arenaPid: String?, time: Map<String, String>, refresh: Boolean): ArenaSchema? =
            arenaRepository.get(arenaPid, time,refresh)
}
