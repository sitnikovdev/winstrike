package ru.prsolution.winstrike.data.datasource

import io.reactivex.Single
import ru.prsolution.winstrike.domain.models.login.LoginModel
import ru.prsolution.winstrike.domain.models.login.AuthResponse
import ru.prsolution.winstrike.domain.models.login.UserModel

interface LoginCacheDataSource {

    fun get(): Single<UserModel>

    fun set(user: UserModel?): Single<UserModel>

}

interface LoginRemoteDataSource {

   suspend fun get(loginModel: LoginModel): AuthResponse?

}
