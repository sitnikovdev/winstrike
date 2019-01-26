package ru.prsolution.winstrike.datasource.model

import com.squareup.moshi.Json

class OrdersEntity {
	@field:Json(name = "orders")
	val orders: List<OrderEntity>? = null
}
