package ru.prsolution.winstrike.presentation.cities

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.prsolution.winstrike.R
import ru.prsolution.winstrike.domain.models.city.City
import ru.prsolution.winstrike.presentation.utils.inflate

/**
 * Created by Oleg Sitnikov on 2019-02-12
 */

class CityListAdapter constructor(private val itemClick: (City) -> Unit) :
    ListAdapter<City, CityListAdapter.ViewHolder>(CityDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityListAdapter.ViewHolder =
        ViewHolder(parent)

    inner class ViewHolder(parent: ViewGroup) :
        RecyclerView.ViewHolder(parent.inflate(R.layout.item_city)) {

        fun bind(item: City) {
        }
    }
}

private class CityDiffCallback : DiffUtil.ItemCallback<City>() {
    override fun areItemsTheSame(oldItem: City, newItem: City): Boolean =
        oldItem.publicId == newItem.publicId

    override fun areContentsTheSame(oldItem: City, newItem: City): Boolean =
        oldItem.name == newItem.name
}