package ru.prsolution.winstrike.domain.usecases

import ru.prsolution.winstrike.domain.models.Arena
import ru.prsolution.winstrike.domain.repository.ArenaRepository

class ArenaUseCase constructor(private val arenaRepository: ArenaRepository) {

    suspend fun get(refresh: Boolean): List<Arena>? =
        arenaRepository.get(refresh)
}
