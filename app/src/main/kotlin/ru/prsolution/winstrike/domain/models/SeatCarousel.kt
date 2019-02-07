package ru.prsolution.winstrike.domain.models

import java.io.Serializable

/*
 * Created by oleg on 01.02.2018.
 */

class SeatCarousel(
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
) : Serializable

enum class RoomSeatType {
    COMMON, VIP
}
