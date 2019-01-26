package ru.prsolution.winstrike.datasource.model

import com.squareup.moshi.Json

class WallEntity(

		@field:Json(name = "start")
		val start: StartEntity? = null,

		@field:Json(name = "end")
		val end: EndEntity? = null
)
