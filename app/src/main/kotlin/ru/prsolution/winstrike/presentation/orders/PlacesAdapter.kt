package ru.prsolution.winstrike.presentation.orders

/*
 * Created by oleg on 31.01.2018.
 */

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView
import ru.prsolution.winstrike.R
import ru.prsolution.winstrike.domain.models.orders.OrderModel

class PlacesAdapter(
    private val context: Context,
    internal var payList: List<OrderModel>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.item_paid, parent, false)
        val rvHolder: RecyclerView.ViewHolder

        rvHolder = PayViewHolder(view)

        return rvHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        initPayFill(position, holder as PayViewHolder)
    }

    private fun initPayFill(position: Int, holder: PayViewHolder) {
        val pay = payList[position]

/*
		tv_date.text = pay.date
		tv_time!!.text = pay.time
		holder.pc!!.text = pay.pcName
		holder.pcCode!!.text = pay.accessCode

		Glide.with(context)
				.load(pay.thumbnail)
				.into(holder.thumbnail!!)
*/
    }

    override fun getItemCount(): Int {
        return payList.size
    }

    inner class PayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

/*		@BindView(R.id.tv_date)
		internal var date: TextView? = null
		@BindView(R.id.tv_time)
		internal var time: TextView? = null
		@BindView(R.id.tv_pc)
		internal var pc: TextView? = null
		@BindView(R.id.tv_pccode)
		internal var pcCode: TextView? = null
		@BindView(R.id.thumbnail)
		internal var thumbnail: ImageView? = null*/

        init {
        }
    }

    inner class PayEmptyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

/*		@BindView(R.id.thumbnail)
		internal var thumbnail: ImageView? = null*/

        init {
        }
    }
}
