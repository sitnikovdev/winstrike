package ru.prsolution.winstrike.datasource.model

import java.io.Serializable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Place : Serializable {

	@SerializedName("offer_pid")
	@Expose
	var offerPid: String? = null
	@SerializedName("is_hidden")
	@Expose
	var isHidden: Boolean? = null
	@SerializedName("computer")
	@Expose
	var computer: Computer? = null
	@SerializedName("public_id")
	@Expose
	var publicId: String? = null
	@SerializedName("offer")
	@Expose
	var offer: Offer? = null
	@SerializedName("computer_pid")
	@Expose
	var computerPid: String? = null
	@SerializedName("room_layout_pid")
	@Expose
	var roomLayoutPid: String? = null
	@SerializedName("name")
	@Expose
	var name: String? = null

	@SerializedName("create_at")
	@Expose
	var createAt: String? = null

	@SerializedName("coors")
	@Expose
	var coors: Coors? = null

	@SerializedName("status")
	@Expose
	var status: String? = null

	companion object {


		private const val serialVersionUID = 2061867234598659099L
	}

}
