package ru.prsolution.winstrike.datasource.model

import java.io.Serializable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RoomLayoutFactory : Serializable {

	@SerializedName("room_layout")
	@Expose
	var roomLayout: RoomLayout? = null

	companion object {
		private const val serialVersionUID = -6262896985114877791L
	}

}
