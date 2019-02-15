package ru.prsolution.winstrike.domain.usecases

import ru.prsolution.winstrike.domain.models.login.AuthResponse
import ru.prsolution.winstrike.domain.models.login.LoginModel
import ru.prsolution.winstrike.domain.repository.LoginRepository

class LoginUseCase constructor(private val loginRepository: LoginRepository) {

    suspend fun get(loginModel: LoginModel): AuthResponse? =
        loginRepository.get(loginModel)
}
