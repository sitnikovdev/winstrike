package ru.prsolution.winstrike.domain.usecases

import ru.prsolution.winstrike.domain.models.login.AuthResponse
import ru.prsolution.winstrike.domain.repository.LoginRepository
import ru.prsolution.winstrike.presentation.model.login.LoginInfo
import ru.prsolution.winstrike.presentation.model.login.mapToDomain

class LoginUseCase constructor(private val loginRepository: LoginRepository) {

    suspend fun get(loginModel: LoginInfo): AuthResponse? =
        loginRepository.get(loginModel.mapToDomain())
}
