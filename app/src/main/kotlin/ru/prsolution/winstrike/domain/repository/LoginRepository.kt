package ru.prsolution.winstrike.domain.repository

import ru.prsolution.winstrike.domain.models.login.AuthResponse
import ru.prsolution.winstrike.domain.models.login.LoginModel

interface LoginRepository {

    suspend fun get(loginModel: LoginModel): AuthResponse?

}
