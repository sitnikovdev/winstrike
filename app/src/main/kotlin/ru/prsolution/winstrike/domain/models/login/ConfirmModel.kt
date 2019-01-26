package ru.prsolution.winstrike.domain.models.login

/**
 * Created by oleg on 07/03/2018.
 */

import com.squareup.moshi.Json

class ConfirmModel (
	@field:Json(name = "username")
	val phone: String? = null
)


