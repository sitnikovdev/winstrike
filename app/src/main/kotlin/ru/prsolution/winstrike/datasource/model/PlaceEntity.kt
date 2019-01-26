package ru.prsolution.winstrike.datasource.model

import com.squareup.moshi.Json

class PlaceEntity(

		@field:Json(name = "offer_pid")
		val offerPid: String? = null,

		@field:Json(name = "is_hidden")
		val isHidden: Boolean? = null,

		@field:Json(name = "computer")
		val computer: ComputerEntity? = null,

		@field:Json(name = "public_id")
		val publicId: String? = null,

		@field:Json(name = "offer")
		val offer: OfferEntity? = null,

		@field:Json(name = "computer_pid")
		val computerPid: String? = null,

		@field:Json(name = "room_layout_pid")
		val roomLayoutPid: String? = null,

		@field:Json(name = "name")
		val name: String? = null,

		@field:Json(name = "create_at")
		val createAt: String? = null,

		@field:Json(name = "coors")
		val coors: CoorsEntity? = null,

		@field:Json(name = "status")
		val status: String? = null
)
