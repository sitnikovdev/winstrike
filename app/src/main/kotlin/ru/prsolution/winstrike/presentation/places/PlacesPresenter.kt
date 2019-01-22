package ru.prsolution.winstrike.presentation.places

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import ru.prsolution.winstrike.datasource.model.OrderModel

import java.util.ArrayList

import ru.prsolution.winstrike.mvp.views.PlacesView
import ru.prsolution.winstrike.networking.Service
import ru.terrakok.cicerone.Router
import rx.subscriptions.CompositeSubscription

/**
 * Created by terrakok 26.11.16
 */

@InjectViewState
class PlacesPresenter(private val service: Service, private val router: Router,
                      private val orderModels: ArrayList<OrderModel>) : MvpPresenter<PlacesView>() {
	private val subscriptions: CompositeSubscription

	val orders: List<OrderModel>
		get() = orderModels

	init {
		this.subscriptions = CompositeSubscription()
	}


	fun onBackPressed() {
		router.exit()
	}

	fun onStop() {
		subscriptions.unsubscribe()
	}
}
