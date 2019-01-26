package ru.prsolution.winstrike.datasource.model

import com.squareup.moshi.Json
import ru.prsolution.winstrike.domain.models.Computer
import ru.prsolution.winstrike.domain.models.Offer
import ru.prsolution.winstrike.domain.models.Room
import ru.prsolution.winstrike.domain.models.Seat
import ru.prsolution.winstrike.domain.models.Wall

class RoomEntity(
		@field:Json(name = "create_at")
		val createAt: String? = null,

		@field:Json(name = "labels")
		val labels: List<LabelEntity>? = null,

		@field:Json(name = "name")
		val name: String? = null,

		@field:Json(name = "places")
		val places: List<PlaceEntity>? = null,

		@field:Json(name = "room_pid")
		val roomPid: String? = null,

		@field:Json(name = "public_id")
		val publicId: String? = null,


		@field:Json(name = "walls")
		val walls: List<WallEntity>? = null

)

