package ru.prsolution.winstrike.datasource.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AuthResponse {

	@SerializedName("message")
	@Expose
	var message: String? = null
	@SerializedName("token")
	@Expose
	var token: String? = null
	@SerializedName("user")
	@Expose
	var user: User? = null

}
