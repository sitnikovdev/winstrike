package ru.prsolution.winstrike.datasource.model.login

import com.squareup.moshi.Json
import ru.prsolution.winstrike.domain.models.login.AuthResponse

class AuthResponseEntity(
    @field:Json(name = "message")
    val message: String?,
    @field:Json(name = "token")
    val token: String?,
    @field:Json(name = "user")
    val user: UserEntity?
)

fun AuthResponseEntity.maptoDomain(): AuthResponse = AuthResponse(
    message = this.message,
    token = this.token,
    user = this.user
)
