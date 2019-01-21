package ru.prsolution.winstrike.presentation.main

/*
 * Created by oleg on 31.01.2018.
 */

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import java.util.ArrayList
import ru.prsolution.winstrike.R
import ru.prsolution.winstrike.ui.main.ArenaItem

class ArenaSelectAdapter(private val mContext: Context, val itemArenaClickListener: OnItemArenaClickListener, internal var arenaItems: ArrayList<ArenaItem>) : RecyclerView.Adapter<ArenaSelectAdapter.ArenaViewHolder>() {

    interface OnItemArenaClickListener {

        fun onArenaSelectItem(v: View, layoutPosition: Int)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArenaViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_arena, parent, false)
        return ArenaViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArenaViewHolder, position: Int) {
        val item = arenaItems[position]
        holder.tv_title!!.text = item.title
        holder.tv_addres!!.text = item.address
        if (position == SELECTED_ITEM) {
            holder.tv_title!!.setTextColor(mContext.getColor(R.color.color_accent))
            holder.tv_addres!!.setTextColor(mContext.getColor(R.color.color_accent))
            holder.iv_checked!!.setImageResource(R.drawable.ic_checked)
            holder.iv_checked!!.visibility = View.VISIBLE
        } else {
            holder.tv_title!!.setTextColor(mContext.getColor(R.color.color_black))
            holder.tv_addres!!.setTextColor(mContext.getColor(R.color.color_black))
            holder.iv_checked!!.visibility = View.GONE
        }

    }

    override fun getItemCount(): Int {
        return arenaItems.size
    }


     inner class ArenaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        @BindView(R.id.title)
        var tv_title: TextView? = null
        @BindView(R.id.address)
        var tv_addres: TextView? = null
        @BindView(R.id.iv_checked)
        var iv_checked: ImageView? = null

        init {
            ButterKnife.bind(this, itemView)
            itemView.setOnClickListener(this)

        }

        override fun onClick(view: View) {
            itemArenaClickListener.onArenaSelectItem(view, layoutPosition)
        }
    }

    companion object {

        var SELECTED_ITEM: Int? = -1
    }
}
