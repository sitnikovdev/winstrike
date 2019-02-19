package ru.prsolution.winstrike.domain.repository

import ru.prsolution.winstrike.data.repository.resouces.Resource
import ru.prsolution.winstrike.domain.models.common.MessageResponse
import ru.prsolution.winstrike.domain.models.login.AuthResponse
import ru.prsolution.winstrike.domain.models.login.LoginModel
import ru.prsolution.winstrike.domain.models.login.NewUserModel
import ru.prsolution.winstrike.domain.models.login.SmsModel

interface LoginRepository {

    suspend fun get(loginModel: LoginModel): Resource<AuthResponse>?

    suspend fun getUser(newUserModel: NewUserModel): Resource<AuthResponse>?

    suspend fun sendSms(smsModel: SmsModel): Resource<MessageResponse>?

}
