package ru.prsolution.winstrike.datasource.model

import com.squareup.moshi.Json

class RoomLayoutFactory  (
	@field:Json(name = "room_layout")
	var roomLayout: RoomLayout? = null
)
