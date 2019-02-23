package ru.prsolution.winstrike.presentation.orders

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_city.view.*
import kotlinx.android.synthetic.main.item_paid.view.*
import ru.prsolution.winstrike.R
import ru.prsolution.winstrike.presentation.model.orders.Order
import ru.prsolution.winstrike.presentation.utils.inflate
import ru.prsolution.winstrike.presentation.utils.pref.PrefUtils

/**
 * Created by Oleg Sitnikov on 2019-02-12
 */

class OrderListAdapter constructor(private val itemClick: (Order) -> Unit) :
    ListAdapter<Order, OrderListAdapter.ViewHolder>(OrderDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderListAdapter.ViewHolder =
        ViewHolder(parent)

    inner class ViewHolder(parent: ViewGroup) :
        RecyclerView.ViewHolder(parent.inflate(R.layout.item_paid)) {

        fun bind(item: Order) {
            itemView.club_name.text = "Компьютерный клуб: «${PrefUtils.arenaName}»"

            itemView.date.text = item.createAt
            itemView.time.text = item.createAt
            itemView.compute_name.text = item.place.name
            itemView.code.text = item.accessCode

            itemView.setOnClickListener { itemClick.invoke(item) }
        }
    }

}


private class OrderDiffCallback : DiffUtil.ItemCallback<Order>() {
    override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean =
        oldItem.accessCode == newItem.accessCode

    override fun areContentsTheSame(old: Order, new: Order): Boolean =
        old.placePid == new.placePid
}