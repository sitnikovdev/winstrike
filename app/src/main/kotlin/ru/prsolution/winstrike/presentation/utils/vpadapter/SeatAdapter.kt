package ru.prsolution.winstrike.presentation.utils.vpadapter


import androidx.annotation.Nullable
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import ru.prsolution.winstrike.R
import ru.prsolution.winstrike.datasource.model.OrderModel


class SeatAdapter(orders: List<OrderModel>) : ViewModelAdapter() {
	var refreshLayout: SwipeRefreshLayout? = null

	override fun reload(@Nullable refreshLayout: SwipeRefreshLayout?) {
		this.refreshLayout = refreshLayout
	}


	init {
		registerCell(OrdersViewModel::class.java, R.layout.item_paid, null)
		for (order in orders) {
			val vm = OrdersViewModel(order.arenaName,
			                         order.placeName,
			                         order.date,
			                         order.time,
			                         order.startAt,
			                         order.endAt,
			                         order.pcName,
			                         order.accessCode,
			                         order.cost,
			                         order.thumbnail)
//			mItems.add(vm)
		}
	}

}
