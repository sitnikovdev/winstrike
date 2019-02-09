package ru.prsolution.winstrike.domain.models

import android.os.Parcel
import android.os.Parcelable
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
) : Serializable, Parcelable {
    constructor(parcel: Parcel) : this(
        TODO("type"),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(imageUrl)
        parcel.writeString(description)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SeatCarousel> {
        override fun createFromParcel(parcel: Parcel): SeatCarousel {
            return SeatCarousel(parcel)
        }

        override fun newArray(size: Int): Array<SeatCarousel?> {
            return arrayOfNulls(size)
        }
    }
}

enum class RoomSeatType {
    COMMON, VIP
}
