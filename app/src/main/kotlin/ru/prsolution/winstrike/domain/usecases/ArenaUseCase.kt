package ru.prsolution.winstrike.domain.usecases

import ru.prsolution.winstrike.data.repository.resouces.Resource
import ru.prsolution.winstrike.domain.models.arena.Arena
import ru.prsolution.winstrike.domain.models.arena.ArenaSchema
import ru.prsolution.winstrike.domain.models.arena.Schedule
import ru.prsolution.winstrike.domain.repository.ArenaRepository

class ArenaUseCase constructor(private val arenaRepository: ArenaRepository) {

    suspend fun get(refresh: Boolean): Resource<List<Arena>>? =
        arenaRepository.get(refresh)


    suspend fun get(arenaPid: String?, time: Map<String, String>, refresh: Boolean): Resource<ArenaSchema>? =
            arenaRepository.get(arenaPid, time,refresh)

    suspend fun getSchedule(): Resource<List<Schedule>>? =
        arenaRepository.getSchedule()
}
