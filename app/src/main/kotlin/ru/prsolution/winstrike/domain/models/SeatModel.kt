package ru.prsolution.winstrike.domain.models

import java.io.Serializable

/*
 * Created by oleg on 01.02.2018.
 */


class SeatModel(
		val type: RoomSeatType = RoomSeatType.COMMON,
		val title: String = "Вы выбрали: $type",
		val imageUrl: String?,
		val description: String?
) : Serializable

enum class RoomSeatType
{
	COMMON,VIP
}

