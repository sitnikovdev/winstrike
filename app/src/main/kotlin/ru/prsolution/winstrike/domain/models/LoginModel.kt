package ru.prsolution.winstrike.domain.models

/**
 * Created by designer on 07/03/2018.
 */
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LoginModel {

	@SerializedName("phone")
	@Expose
	var phone: String? = null
	@SerializedName("password")
	@Expose
	var password: String? = null

}

