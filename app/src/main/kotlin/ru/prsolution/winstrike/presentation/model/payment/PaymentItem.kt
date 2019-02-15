package ru.prsolution.winstrike.presentation.model.payment

import ru.prsolution.winstrike.domain.models.payment.Payment

/**
 * Created by oleg on 07/03/2018.
 */


class PaymentItem(
    val startAt: String?,
    val endAt: String?,
    val placesPid: List<String>?
) {

    override fun toString(): String {
        return "start_at: " + startAt + "end_at: " + endAt + " Places: " + placesPid!!.toString()
    }

}

fun PaymentItem.mapToDomain(): Payment = Payment(
    start_at = this.startAt,
    end_at = this.endAt,
    places = this.placesPid
)
