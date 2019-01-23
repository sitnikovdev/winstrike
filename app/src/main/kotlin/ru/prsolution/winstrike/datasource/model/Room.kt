package ru.prsolution.winstrike.datasource.model

import com.squareup.moshi.Json
import ru.prsolution.winstrike.R

class Room  (
	@field:Json(name= "public_id") val publicId: String? = null,
	@field:Json(name= "name") val name: String? = null,
	@field:Json(name= "metro") val metro: String? = null,
	@field:Json(name= "room_layout_pid") val roomLayoutPid: String? = null,
	@field:Json(name= "description") val description: String? = null,


	@field:Json(name= "usual_description") val usualDescription: String? = null,
	@field:Json(name= "usual_image_url") val usualImageUrl: String? = null,
	@field:Json(name= "vip_image_url") val vipImageUrl: String? = null,
	@field:Json(name= "vip_description") val vipDescription: String? = null,

	@field:Json(name= "locale") val locale: String? = null
)

