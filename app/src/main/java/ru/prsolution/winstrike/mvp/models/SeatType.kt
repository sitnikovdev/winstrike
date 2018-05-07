package ru.prsolution.winstrike.mvp.models

//
//  SeatType.swift
//  winstrike
//
//  Created by PRS on 28/02/2018.
//  Copyright © 2018 PR_Solution. All rights reserved.
//


import ru.prsolution.winstrike.R
import java.util.*

enum class SeatType private constructor(val type: String) {
    FREE("free"), HIDDEN("hidden"), SELF_BOOKING("self_booking"), BOOKING("booking"), VIP("vip");

    val image: Int
        get() {
            if (this == FREE) {
                return R.drawable.seat_grey
            }

            if (this == SELF_BOOKING) {
                return R.drawable.seat_blue
            }

            if (this == BOOKING) {
                return R.drawable.seat_red
            }

            if (this == VIP) {
                return R.drawable.seat_yellow
            }

            return if (this == HIDDEN) {
                R.drawable.seat_darkgrey
            } else R.drawable.seat_grey

        }

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
        operator fun get(status: String): SeatType? {
            return lookup[status]
        }

        // TODO Get image resources for seat status
       fun getImage(status: SeatType): Int? {
            when (status) {
                SeatType.FREE -> return 0
                SeatType.BOOKING -> return 1
                SeatType.SELF_BOOKING -> return 2
                SeatType.HIDDEN -> return 3
                SeatType.VIP -> return 3
                else -> {
                    return 0
                }
            }

        }
    }
}

/* enum SeatType: String {
    case available = "free"
    case vip
    case bookedYou = "self_booking"
    case picked
    case booked = "booking"
    case hidden = "hidden"

    func getImage() -> UIImage {
        switch self {
        case .available:
            return Asset.ChooseSeat.seatGrey.image
        case .booked:
            return Asset.ChooseSeat.seatRed.image
        case .picked:
            return Asset.ChooseSeat.seatWhite.image
        case .bookedYou:
            return Asset.ChooseSeat.seatBlue.image
        case .vip:
            return Asset.ChooseSeat.seatYellow.image
        case .hidden:
            return Asset.ChooseSeat.seatDarkGrey.image
        }
    }

    func getSrc() -> String {
        switch self {
        case .available:
            return "seatGrey.png"
        case .booked:
            return "seatRed.png"
        case .picked:
            return "seatWhite.png"
        case .bookedYou:
            return "seatBlue.png"
        case .vip:
            return "seatYellow.png"
        case .hidden:
            return "seatDarkGrey.png"
        }
    }

    func getDesc() -> String {
        switch self {
        case .available:
            return L10n.seatPickerSeatTypeAvailable
        case .booked:
            return L10n.seatPickerSeatTypeBooked
        case .bookedYou:
            return L10n.seatPickerSeatTypeBookedYou
        case .picked:
            return L10n.seatPickerSeatTypePicked
        case .vip:
            return L10n.seatPickerSeatTypeVip
        case .hidden:
            return L10n.seatPickerSeatTypeHidden
        }
}
    }*/
