package ru.prsolution.winstrike.datasource.model.orders

import com.squareup.moshi.Json
import ru.prsolution.winstrike.datasource.model.SeatEntity

class OrdersEntity {
	@field:Json(name = "orders")
	val orders: List<OrderEntity>? = null
}

class OrderEntity(

		@field:Json(name = "room_name")
		val roomName: String? = null,

		@field:Json(name = "place")
		val place: SeatEntity? = null,

		@field:Json(name = "cost")
		val cost: Int? = null,

		@field:Json(name = "end_at")
		val endAt: String? = null,

		@field:Json(name = "access_code")
		val accessCode: String? = null,

		@field:Json(name = "place_pid")
		val placePid: String? = null,

		@field:Json(name = "start_at")
		val startAt: String? = null,

		@field:Json(name = "user_pid")
		val userPid: String? = null,

		@field:Json(name = "public_id")
		val publicId: String? = null,

		@field:Json(name = "create_at")
		val createAt: String? = null
)
