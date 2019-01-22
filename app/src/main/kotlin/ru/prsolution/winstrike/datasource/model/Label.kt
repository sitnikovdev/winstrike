package ru.prsolution.winstrike.datasource.model

import java.io.Serializable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Label : Serializable {

	@SerializedName("x")
	@Expose
	var x: Int? = null
	@SerializedName("y")
	@Expose
	var y: Int? = null
	@SerializedName("text")
	@Expose
	var text: String? = null

	companion object {
		private const val serialVersionUID = 3470742069396083544L
	}

}
