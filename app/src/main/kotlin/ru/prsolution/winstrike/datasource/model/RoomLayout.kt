package ru.prsolution.winstrike.datasource.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import java.io.Serializable

class RoomLayout : Serializable {

	@SerializedName("places")
	@Expose
	var places: List<Place>? = null
	@SerializedName("room_pid")
	@Expose
	var roomPid: String? = null
	@SerializedName("public_id")
	@Expose
	var publicId: String? = null
	@SerializedName("name")
	@Expose
	var name: String? = null
	@SerializedName("create_at")
	@Expose
	var createAt: String? = null
	@SerializedName("walls")
	@Expose
	var walls: List<Wall>? = null
	@SerializedName("labels")
	@Expose
	var labels: List<Label>? = null

	companion object {

		private const val serialVersionUID = -7881296003903503498L
	}

}
