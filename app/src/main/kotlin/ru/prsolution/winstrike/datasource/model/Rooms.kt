package ru.prsolution.winstrike.datasource.model

import java.io.Serializable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Rooms : Serializable {

	@SerializedName("room")
	@Expose
	var room: Room? = null

	companion object {
		private const val serialVersionUID = 3350672797887197123L
	}

}
