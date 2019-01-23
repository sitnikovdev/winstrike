package ru.prsolution.winstrike.datasource.model

/**
 * Created by oleg on 07/03/2018.
 */

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import java.util.ArrayList
import java.util.LinkedHashMap

import timber.log.Timber

class PaymentModel {

	@SerializedName("start_at")
	@Expose
	var startAt: String? = null
	@SerializedName("end_at")
	@Expose
	var end_at: String? = null
	@SerializedName("places")
	@Expose
	var placesPid: List<String>? = null
		private set

	fun setPlacesPid(placesPid: LinkedHashMap<Int, String>) {
		// TODO: 13/05/2018   convert to List<String>
		val pids = mutableListOf<String>()
		Timber.d("pids than need to converted: %s", placesPid)
		val entrySet = placesPid.entries
		val iterator = entrySet.iterator()
		while (iterator.hasNext()) {
			val entry = iterator.next()
			val value = entry.value
			pids.add(value)
		}
		this.placesPid = pids
	}

	override fun toString(): String {
		return "start_at: " + startAt + "end_at: " + end_at + " Places: " + placesPid!!.toString()
	}
}

