package ru.prsolution.winstrike.presentation.main

import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.dlg_logout.view.title
import kotlinx.android.synthetic.main.item_arena.view.address
import kotlinx.android.synthetic.main.item_arena.view.iv_checked
import ru.prsolution.winstrike.presentation.utils.inflate
import ru.prsolution.winstrike.presentation.utils.setColor
import ru.prsolution.winstrike.R
import ru.prsolution.winstrike.mvp.apimodels.Room

class ArenaListAdapter (private val itemClick: (Room, Int) -> Unit) :
		ListAdapter<Room, ArenaListAdapter.ViewHolder>(PostDiffCallback()) {

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
			ViewHolder(parent)

	override fun onBindViewHolder(holder: ViewHolder, position: Int) =
			holder.bind(getItem(position))

	inner class ViewHolder(parent: ViewGroup) :
			RecyclerView.ViewHolder(parent.inflate(R.layout.item_arena)) {

		fun bind(item: Room) {
			itemView.title.text = item.name
			itemView.address.text = item.metro
			if (position == SELECTED_ITEM) {
				itemView.title.setColor(R.color.color_accent)
				itemView.address.setColor(R.color.color_accent)
				itemView.iv_checked.setImageResource(R.drawable.ic_checked)
				itemView.iv_checked.visibility = View.VISIBLE
			} else {
				itemView.title.setColor(R.color.color_black)
				itemView.address.setColor(R.color.color_black)
				itemView.iv_checked.visibility = View.GONE
			}

			itemView.setOnClickListener { itemClick.invoke(item,position) }
		}
	}

	companion object {
		var SELECTED_ITEM: Int? = -1
	}
}

private class PostDiffCallback : DiffUtil.ItemCallback<Room>() {
	override fun areItemsTheSame(oldItem: Room, newItem: Room): Boolean =
			oldItem.name == newItem.metro

	override fun areContentsTheSame(oldItem: Room, newItem: Room): Boolean =
			oldItem.metro == newItem.metro &&
					oldItem.name == newItem.name
}
