package ru.prsolution.winstrike.presentation.utils.vpadapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import java.util.HashMap
import java.util.Hashtable
import java.util.LinkedList

import androidx.annotation.LayoutRes
import androidx.annotation.Nullable
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

abstract class ViewModelAdapter : RecyclerView.Adapter<ViewModelAdapter.ViewHolder>() {

	private val mGlobalObjects = Hashtable<Int, Any>()

	private val mCellInfoMap = HashMap<Class<*>, CellInfo>()

	protected val mItems: List<Any> = LinkedList()

	private var mBeginUpdateItemsSize = 0

	protected fun addGlobalItem(bindingId: Int, `object`: Any) {
		mGlobalObjects[bindingId] = `object`
	}

	protected fun registerCell(objectClass: Class<*>, @LayoutRes layoutId: Int, bindingId: Int?) {
		val cellInfo = CellInfo()
		cellInfo.mLayoutId = layoutId
		cellInfo.mBindingId = bindingId!!
		mCellInfoMap[objectClass] = cellInfo
	}

	fun reload() {
		reload(null)
	}

	abstract fun reload(@Nullable refreshLayout: SwipeRefreshLayout?)

	protected fun loadMore() {

	}

	protected fun beginUpdates() {
		mBeginUpdateItemsSize = itemCount
	}

	protected fun endUpdates() {
		val changed = Math.min(mBeginUpdateItemsSize, itemCount)
		val diff = Math.max(mBeginUpdateItemsSize, itemCount) - changed

		if (diff == 0 && changed > 1) {
			notifyDataSetChanged()
			return
		}

		if (changed != 0) notifyItemRangeChanged(0, changed)

		if (diff > 0) {
			if (mBeginUpdateItemsSize > itemCount) {
				notifyItemRangeRemoved(changed, diff)
			} else {
				notifyItemRangeInserted(changed, diff)
			}
		}
	}

	override fun getItemCount(): Int {
		return mItems.size
	}

	protected fun getItemAt(position: Int): Any {
		return mItems[position]
	}

	override fun getItemViewType(position: Int): Int {
		return getCellInfo(getItemAt(position))!!.mLayoutId
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val holder = ViewHolder(LayoutInflater.from(parent.context).inflate(viewType, parent, false))
		for ((key, value) in mGlobalObjects) {
//			binding!!.setVariable(key, value)
		}
		return holder
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		val item = getItemAt(position)
		val cellInfo = getCellInfo(item)

		if (cellInfo!!.mBindingId != 0) {
//			binding!!.setVariable(cellInfo!!.mBindingId, item)
		}

		if (position == itemCount - 2) loadMore()
	}

	protected fun getCellInfo(`object`: Any): CellInfo? {
		for ((key, value) in mCellInfoMap) {
			if (key.isInstance(`object`))
				return value
		}
		return null
	}

	class CellInfo {
		@LayoutRes
		var mLayoutId: Int = 0
		var mBindingId: Int = 0
	}

	inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


		init {
		}

	}

}
