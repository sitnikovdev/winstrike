package ru.prsolution.winstrike.datasource.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Order {

	@SerializedName("room_name")
	@Expose
	var roomName: String? = null
	@SerializedName("place")
	@Expose
	var place: Place? = null
	@SerializedName("cost")
	@Expose
	var cost: Int? = null
	@SerializedName("end_at")
	@Expose
	var endAt: String? = null
	@SerializedName("access_code")
	@Expose
	var accessCode: String? = null
	@SerializedName("place_pid")
	@Expose
	var placePid: String? = null
	@SerializedName("start_at")
	@Expose
	var startAt: String? = null
	@SerializedName("user_pid")
	@Expose
	var userPid: String? = null
	@SerializedName("public_id")
	@Expose
	var publicId: String? = null
	@SerializedName("create_at")
	@Expose
	var createAt: String? = null
}
