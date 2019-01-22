package ru.prsolution.winstrike.datasource.model

import java.io.Serializable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Coors : Serializable {

	@SerializedName("id")
	@Expose
	var id: String? = null
	@SerializedName("x")
	@Expose
	var x: Int? = null
	@SerializedName("y")
	@Expose
	var y: Int? = null
	@SerializedName("angle")
	@Expose
	var angle: Double? = null
	@SerializedName("type")
	@Expose
	var type: Int? = null

	companion object {
		private const val serialVersionUID = -6921130380543258340L
	}

}
