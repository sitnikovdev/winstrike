package ru.prsolution.winstrike.datasource.model

import com.squareup.moshi.Json

class OfferEntity(

		@field:Json(name = "name")
		val name: String? = null,

		@field:Json(name = "cost")
		val cost: Int? = null,

		@field:Json(name = "public_id")
		val publicId: String? = null,

		@field:Json(name = "create_at")
		val createAt: String? = null
)
