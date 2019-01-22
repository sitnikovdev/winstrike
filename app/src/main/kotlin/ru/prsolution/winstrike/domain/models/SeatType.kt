package ru.prsolution.winstrike.domain.models


import java.util.*

enum class SeatType private constructor(val type: String) {
    FREE("free"), HIDDEN("hidden"), SELF_BOOKING("self_booking"), BOOKING("booking"), VIP("vip");

    companion object {


        //Lookup table
        private val lookup = HashMap<String, SeatType>()

        //Populate the lookup table on loading time
        init {
            for (status in SeatType.values()) {
                lookup[status.type] = status
            }
        }

        //This method can be used for reverse lookup purpose
        operator fun get(status: String?): SeatType? {
            return lookup[status]
        }
    }
}

