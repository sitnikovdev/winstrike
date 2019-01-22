package ru.prsolution.winstrike.datasource.model

import java.io.Serializable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class PaymentResponse : Serializable {

	@SerializedName("message")
	@Expose
	var message: String? = null
	@SerializedName("redirect_url")
	@Expose
	var redirectUrl: String? = null

	companion object {
		private const val serialVersionUID = 2905219164848985577L
	}

}
