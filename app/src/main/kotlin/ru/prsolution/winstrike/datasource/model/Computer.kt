package ru.prsolution.winstrike.datasource.model

import java.io.Serializable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Computer : Serializable {

	@SerializedName("active")
	@Expose
	var active: Boolean? = null
	@SerializedName("name")
	@Expose
	var name: String? = null
	@SerializedName("public_id")
	@Expose
	var publicId: String? = null
	@SerializedName("create_at")
	@Expose
	var createAt: String? = null

	companion object {
		private const val serialVersionUID = -1318326118487899001L
	}

}
