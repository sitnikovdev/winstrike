package ru.prsolution.winstrike.datasource.remote

import ru.prsolution.winstrike.data.datasource.LoginRemoteDataSource
import ru.prsolution.winstrike.data.repository.resouces.Resource
import ru.prsolution.winstrike.datasource.model.login.LoginEntity
import ru.prsolution.winstrike.datasource.model.login.NewUserEntity
import ru.prsolution.winstrike.datasource.model.login.SmsEntity
import ru.prsolution.winstrike.datasource.model.login.mapToDomain
import ru.prsolution.winstrike.domain.models.common.MessageResponse
import ru.prsolution.winstrike.domain.models.login.AuthResponse
import ru.prsolution.winstrike.domain.models.login.SmsModel
import ru.prsolution.winstrike.presentation.model.login.SmsInfo

class LoginRemoteDataSourceImpl constructor(
    private val api: UserApi
) : LoginRemoteDataSource, BaseRepository() {

    override suspend fun get(loginModel: LoginEntity): Resource<AuthResponse>? {
        val response = safeApiCall(
            call = { api.getLogin(loginModel).await() },
            errorMessage = "Error Fetching Cities List"
        )
        return response?.mapToDomain()
    }

    override suspend fun getUser(newUserModel: NewUserEntity): Resource<AuthResponse>? {
        val response = safeApiCall(
            call = { api.getUser(newUserModel).await() },
            errorMessage = "Error Fetching Cities List"
        )
        return response?.mapToDomain()
    }

    override suspend fun sendSms(smsEntity: SmsEntity): Resource<MessageResponse>? {
        val response = safeApiCall(
            call = { api.sendSms(smsEntity).await() },
            errorMessage = "Error Fetching Cities List"
        )
        return response
    }

    override suspend fun confirm(smsCode: String, smsEntity: SmsEntity): Resource<MessageResponse>? {
        val response = safeApiCall(
            call = { api.confirmUser(smsCode, smsEntity).await() },
            errorMessage = "Error Fetching Cities List"
        )
        return response
    }


}
