package ru.prsolution.winstrike.domain.models

/**
 * Created by oleg on 07/03/2018.
 */

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ConfirmModel {

	@SerializedName("username")
	@Expose
	var phone: String? = null
}


