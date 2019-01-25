package ru.prsolution.winstrike.datasource.model

import com.squareup.moshi.Json

class Rooms (
	@field:Json(name = "room")
	val room: Room? = null
)
