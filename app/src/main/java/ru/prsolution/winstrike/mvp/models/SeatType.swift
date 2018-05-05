//
//  SeatType.swift
//  winstrike
//
//  Created by PRS on 28/02/2018.
//  Copyright © 2018 PR_Solution. All rights reserved.
//

import Foundation
import UIKit

enum SeatType: String {
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
}
