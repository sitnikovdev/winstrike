package ru.prsolution.winstrike.domain.usecases

import ru.prsolution.winstrike.data.repository.resouces.Resource
import ru.prsolution.winstrike.datasource.model.login.SmsEntity
import ru.prsolution.winstrike.domain.models.common.MessageResponse
import ru.prsolution.winstrike.domain.models.login.AuthResponse
import ru.prsolution.winstrike.domain.models.login.SmsModel
import ru.prsolution.winstrike.domain.repository.LoginRepository
import ru.prsolution.winstrike.presentation.model.login.LoginInfo
import ru.prsolution.winstrike.presentation.model.login.NewUserInfo
import ru.prsolution.winstrike.presentation.model.login.SmsInfo
import ru.prsolution.winstrike.presentation.model.login.mapToDomain

class LoginUseCase constructor(private val loginRepository: LoginRepository) {

    suspend fun get(loginModel: LoginInfo): Resource<AuthResponse>? =
        loginRepository.get(loginModel.mapToDomain())

    suspend fun getUser(newUserModel: NewUserInfo): Resource<AuthResponse>? =
        loginRepository.getUser(newUserModel.mapToDomain())

    suspend fun sendSms(smsInfo: SmsInfo): Resource<MessageResponse>? =
        loginRepository.sendSms(smsInfo.mapToDomain())

    suspend fun confirm(smsCode: String, smsInfo: SmsInfo): Resource<MessageResponse>? =
        loginRepository.confirm(smsCode, smsInfo.mapToDomain())
}
