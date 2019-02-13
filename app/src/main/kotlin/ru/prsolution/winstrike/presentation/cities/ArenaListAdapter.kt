package ru.prsolution.winstrike.presentation.cities

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_arena.view.*
import kotlinx.android.synthetic.main.item_city.view.*
import kotlinx.android.synthetic.main.item_city_detail.view.*
import ru.prsolution.winstrike.R
import ru.prsolution.winstrike.presentation.model.ArenaItem
import ru.prsolution.winstrike.presentation.utils.inflate

/**
 * Created by Oleg Sitnikov on 2019-02-12
 */

class ArenaListAdapter constructor(private val itemClick: (ArenaItem) -> Unit) :
        ListAdapter<ArenaItem, ArenaListAdapter.ViewHolder>(ArenaItemDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArenaListAdapter.ViewHolder =
            ViewHolder(parent)

    inner class ViewHolder(parent: ViewGroup) :
            RecyclerView.ViewHolder(parent.inflate(R.layout.item_city_detail)) {

        fun bind(item: ArenaItem) {
            itemView.city_tv.text = item.name
            itemView.metro_tv.text = item.metro
            itemView.setOnClickListener { itemClick.invoke(item) }
        }
    }

    companion object {
        var SELECTED_ITEM: String = ""
    }
}

private class ArenaItemDiffCallback : DiffUtil.ItemCallback<ArenaItem>() {
    override fun areItemsTheSame(oldItem: ArenaItem, newItem: ArenaItem): Boolean =
            oldItem.publicId == newItem.publicId

    override fun areContentsTheSame(oldItem: ArenaItem, newItem: ArenaItem): Boolean =
            oldItem.name == newItem.name
}