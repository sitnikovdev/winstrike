package ru.prsolution.winstrike.datasource.model

import java.io.Serializable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Layout : Serializable {

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

	companion object {
		private const val serialVersionUID = 2256038506441890104L
	}

}
