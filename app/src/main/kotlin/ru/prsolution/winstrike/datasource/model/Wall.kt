package ru.prsolution.winstrike.datasource.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import java.io.Serializable

class Wall : Serializable {

	@SerializedName("start")
	@Expose
	var start: Start? = null
	@SerializedName("end")
	@Expose
	var end: End? = null

	companion object {
		private const val serialVersionUID = 8296872575103507327L
	}

}
