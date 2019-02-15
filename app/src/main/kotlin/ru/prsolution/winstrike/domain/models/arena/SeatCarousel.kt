package ru.prsolution.winstrike.domain.models.arena

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/*
 * Created by oleg on 01.02.2018.
 */

@Parcelize
data class SeatCarousel(
    val type: Type = Type.COMMON,
    val title: String = when (type) {
        Type.COMMON -> {
            "Вы выбрали: Общий зал"
        }
        Type.VIP -> {
            "Вы выбрали: VIP"
        }
    },

    val imageUrl: String?,
    val description: String?
) : Parcelable

enum class Type {
    COMMON, VIP
}
