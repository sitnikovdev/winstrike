package ru.prsolution.winstrike.datasource.model

import java.io.Serializable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Offer : Serializable {

	@SerializedName("name")
	@Expose
	var name: String? = null
	@SerializedName("cost")
	@Expose
	var cost: Int? = null
	@SerializedName("public_id")
	@Expose
	var publicId: String? = null
	@SerializedName("create_at")
	@Expose
	var createAt: String? = null

	companion object {
		private const val serialVersionUID = 4831832608759182598L
	}

}
