package ru.prsolution.winstrike.presentation.utils.vpadapter

import ru.prsolution.winstrike.domain.models.orders.OrderModel


class SeatAdapter(orders: List<OrderModel>)   {


	init {
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
