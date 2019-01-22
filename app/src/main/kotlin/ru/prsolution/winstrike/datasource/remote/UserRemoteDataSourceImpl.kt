package ru.prsolution.winstrike.datasource.remote

import ru.prsolution.winstrike.datasource.model.User
import rx.Single

class UserRemoteDataSourceImpl constructor(
    private val api: UsersApi
)   {

/*    override fun get(): Single<List<User>> =
        api.getUsers()
            .map { it.mapToDomain() }

    override fun get(userId: String): Single<User> =
        api.getUser(userId)
            .map { it.mapToDomain() }*/
}
