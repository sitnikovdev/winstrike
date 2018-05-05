package ru.prsolution.winstrike.mvp.models;//
//  SeatStatus.swift
//  winstrike
//
//  Created by PRS on 28/02/2018.
//  Copyright © 2018 PR_Solution. All rights reserved.
//


import java.util.HashMap;
import java.util.Map;

import ru.prsolution.winstrike.R;

public enum SeatStatus {
    FREE("free"), HIDDEN("hidden"), SELF_BOOKING("self_booking"), BOOKING("booking"),VIP("vip");

    private String type;

     SeatStatus(String type) {
         this.type = type;
    }

    public String getType() {
        return type;
    }

    public int getImage() {
        if (this.equals(FREE)) {
            return R.drawable.seat_grey;
        }

        if (this.equals(SELF_BOOKING)) {
            return R.drawable.seat_blue;
        }

        if (this.equals(BOOKING)) {
            return R.drawable.seat_red;
        }

        if (this.equals(VIP)) {
            return R.drawable.seat_yellow;
        }

        if (this.equals(HIDDEN)) {
            return R.drawable.seat_darkgrey;
        }

        return R.drawable.seat_grey;
    }


    //Lookup table
    private static final Map<String, SeatStatus> lookup = new HashMap<>();

    //Populate the lookup table on loading time
    static
    {
        for(SeatStatus status : SeatStatus.values())
        {
            lookup.put(status.getType(), status);
        }
    }

    //This method can be used for reverse lookup purpose
    public static SeatStatus get(String status)
    {
        return lookup.get(status);
    }

}



/* enum SeatStatus: String {
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
