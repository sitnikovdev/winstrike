package ru.prsolution.winstrike.datasource.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Orders {

	@SerializedName("orders")
	@Expose
	var orders: List<Order>? = null

}
