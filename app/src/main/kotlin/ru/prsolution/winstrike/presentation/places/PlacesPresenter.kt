package ru.prsolution.winstrike.presentation.places

import ru.prsolution.winstrike.datasource.model.OrderModel
import ru.prsolution.winstrike.networking.Service
import rx.subscriptions.CompositeSubscription
import java.util.ArrayList

/**
 * Created by terrakok 26.11.16
 */

class PlacesPresenter(private val service: Service,
                      private val orderModels: ArrayList<OrderModel>)  {
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
