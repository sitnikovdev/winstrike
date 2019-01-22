package ru.prsolution.winstrike.datasource.model

import java.io.Serializable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class End : Serializable {

	@SerializedName("x")
	@Expose
	var x: Int? = null
	@SerializedName("y")
	@Expose
	var y: Int? = null

	companion object {
		private const val serialVersionUID = 327424254676383776L
	}

}
