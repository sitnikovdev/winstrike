package ru.prsolution.winstrike.datasource.model

import com.squareup.moshi.Json

class LabelEntity(

		@field:Json(name = "text")
		val text: String? = null,

		@field:Json(name = "x")
		val x: Int? = null,

		@field:Json(name = "y")
		val y: Int? = null
)
