package ru.prsolution.winstrike.data.repository

import ru.prsolution.winstrike.data.datasource.LoginCacheDataSource
import ru.prsolution.winstrike.data.datasource.LoginRemoteDataSource
import ru.prsolution.winstrike.domain.models.login.AuthResponse
import ru.prsolution.winstrike.domain.models.login.LoginModel
import ru.prsolution.winstrike.domain.models.login.mapToDataSource
import ru.prsolution.winstrike.domain.repository.LoginRepository

class LoginRepositoryImpl constructor(
    private val cacheDataSource: LoginCacheDataSource,
    private val remoteDataSource: LoginRemoteDataSource
) : LoginRepository {

    override suspend fun get(loginModel: LoginModel): AuthResponse? =
        remoteDataSource.get(loginModel.mapToDataSource())
}

