package ru.prsolution.winstrike.presentation.places

import ru.prsolution.winstrike.domain.models.orders.OrderModel
import java.util.ArrayList

/**
 * Created by oleg 24.01.2019
 */

class PlacesPresenter(private val orderModels: ArrayList<OrderModel>)  {

	val orders: List<OrderModel>
		get() = orderModels

	init {
	}


	fun onBackPressed() {
//		router.exit()
	}

	fun onStop() {
	}
}
