package ru.prsolution.winstrike.datasource.remote

import ru.prsolution.winstrike.data.datasource.LoginRemoteDataSource
import ru.prsolution.winstrike.datasource.model.login.AuthResponseEntity
import ru.prsolution.winstrike.datasource.model.login.maptoDomain
import ru.prsolution.winstrike.domain.models.login.AuthResponse
import ru.prsolution.winstrike.domain.models.login.LoginModel

class LoginRemoteDataSourceImpl constructor(
    private val api: UserApi
) : LoginRemoteDataSource, BaseRepository() {

    override suspend fun get(loginModel: LoginModel): AuthResponse? {
        val response = safeApiCall(
            call = { api.getLogin(loginModel).await() },
            errorMessage = "Error Fetching Cities List"
        )
        return response?.maptoDomain()
    }

}
