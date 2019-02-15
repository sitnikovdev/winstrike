package ru.prsolution.winstrike.domain.models.payment

/**
 * Created by oleg on 07/03/2018.
 */

import ru.prsolution.winstrike.presentation.model.payment.PaymentItem
import timber.log.Timber
import java.util.LinkedHashMap

class Payment(
    val start_at: String?,
    val end_at: String?,
    var places: List<String>?
) {
    override fun toString(): String {
        return "start_at: " + start_at + "end_at: " + end_at + " Places: " + places!!.toString()
    }
}

fun Payment.setPlacesPid(placesPid: LinkedHashMap<Int, String>) {
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
    this.places = pids
}


fun Payment.mapToPresentation(): PaymentItem = PaymentItem(
    startAt = start_at,
    endAt = end_at,
    placesPid = places
)

