package ru.prsolution.winstrike.domain.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/*
 * Created by oleg on 01.02.2018.
 */

@Parcelize
data class SeatCarousel(
    val type: RoomSeatType = RoomSeatType.COMMON,
    val title: String = when (type) {
        RoomSeatType.COMMON -> {
            "Вы выбрали: Общий зал"
        }
        RoomSeatType.VIP -> {
            "Вы выбрали: VIP"
        }
    },

    val imageUrl: String?,
    val description: String?
) : Parcelable

enum class RoomSeatType {
    COMMON, VIP
}
