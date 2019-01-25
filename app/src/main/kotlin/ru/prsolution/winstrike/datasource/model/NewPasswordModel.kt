package ru.prsolution.winstrike.datasource.model

import com.squareup.moshi.Json

class NewPasswordModel (
    @field:Json(name = "username")
    var username: String? = null,
    @field:Json(name = "new_password")
    var new_password: String? = null
)
