package ru.prsolution.winstrike.domain.models

/*
 * Created by oleg on 07.03.2018.
 */
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class MessageResponse {

	@SerializedName("message")
	@Expose
	var message: String? = null

}

