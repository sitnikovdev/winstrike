package ru.prsolution.winstrike.domain.models

import com.squareup.moshi.Json

/**
 * Created by oleg on 07/03/2018.
 */

class LoginModel(
		@field:Json(name = "phone") val phone: String? = null,
		@field:Json(name = "password") val password: String? = null
)

