package ru.prsolution.winstrike.datasource.model

import com.squareup.moshi.Json

class ComputerEntity(

		@field:Json(name = "active")
		val active: Boolean? = null,

		@field:Json(name = "name")
		val name: String? = null,

		@field:Json(name = "public_id")
		val publicId: String? = null,

		@field:Json(name = "create_at")
		val createAt: String? = null
)
