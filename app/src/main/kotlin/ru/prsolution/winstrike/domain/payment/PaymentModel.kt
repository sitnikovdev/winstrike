package ru.prsolution.winstrike.domain.payment

/**
 * Created by oleg on 07/03/2018.
 */

import com.squareup.moshi.Json
import timber.log.Timber
import java.util.LinkedHashMap

class PaymentModel {

	@field:Json(name = "start_at")
	var startAt: String? = null
	@field:Json(name = "end_at")
	var end_at: String? = null
	@field:Json(name = "places")
	var placesPid: List<String>? = null

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

