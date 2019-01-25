package ru.prsolution.winstrike.datasource.model

import com.squareup.moshi.Json

class Start  (
	@field:Json(name = "x")
	val x: Int? = null,
	@field:Json(name = "y")
	val y: Int? = null
)
