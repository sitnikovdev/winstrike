package ru.prsolution.winstrike.data.datasource

import io.reactivex.Single
import ru.prsolution.winstrike.data.repository.resouces.Resource
import ru.prsolution.winstrike.datasource.model.login.LoginEntity
import ru.prsolution.winstrike.datasource.model.login.NewUserEntity
import ru.prsolution.winstrike.datasource.model.login.SmsEntity
import ru.prsolution.winstrike.domain.models.common.MessageResponse
import ru.prsolution.winstrike.domain.models.login.LoginModel
import ru.prsolution.winstrike.domain.models.login.AuthResponse
import ru.prsolution.winstrike.domain.models.login.SmsModel
import ru.prsolution.winstrike.domain.models.login.UserModel

interface LoginCacheDataSource {

    fun get(): Single<UserModel>

    fun set(user: UserModel?): Single<UserModel>

}

interface LoginRemoteDataSource {

    suspend fun get(loginModel: LoginEntity): Resource<AuthResponse>?

    suspend fun getUser(newUserModel: NewUserEntity): Resource<AuthResponse>?

    suspend fun sendSms(smsEntity: SmsEntity): Resource<MessageResponse>?

}
