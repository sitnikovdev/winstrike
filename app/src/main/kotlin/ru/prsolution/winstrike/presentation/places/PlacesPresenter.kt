package ru.prsolution.winstrike.presentation.places

import ru.prsolution.winstrike.domain.models.orders.OrderModel
import rx.subscriptions.CompositeSubscription
import java.util.ArrayList

/**
 * Created by oleg 24.01.2019
 */

class PlacesPresenter(private val orderModels: ArrayList<OrderModel>)  {
	private val subscriptions: CompositeSubscription

	val orders: List<OrderModel>
		get() = orderModels

	init {
		this.subscriptions = CompositeSubscription()
	}


	fun onBackPressed() {
//		router.exit()
	}

	fun onStop() {
		subscriptions.unsubscribe()
	}
}
