package ru.prsolution.winstrike.datasource.model.login

import com.squareup.moshi.Json

class User(

    @field:Json(name = "confirmed")
    val confirmed: Boolean? = null,

    @field:Json(name = "confirmed_on")
    val confirmedOn: Any? = null,

    @field:Json(name = "email")
    val email: String? = null,

    @field:Json(name = "name")
    val name: String? = null,

    @field:Json(name = "phone")
    val phone: String? = null,

    @field:Json(name = "public_id")
    val publicId: String? = null,

    @field:Json(name = "registered_on")
    val registeredOn: String? = null,

    @field:Json(name = "social_id")
    val socialId: Any? = null,

    @field:Json(name = "role")
    val role: String? = null,

    @field:Json(name = "user_id")
    val id: String? = null,
    val password: String? = null
)
