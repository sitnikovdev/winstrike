package ru.prsolution.winstrike.domain.models.orders

/*
 * Created by oleg on 31.01.2018.
 */

data class OrderModel(
    val arenaName: String? = null,
    val placeName: String? = null,
    val date: String? = null,
    val time: String? = null,
    val startAt: String? = null,
    val endAt: String? = null,
    val placePid: String? = null,
    val publicPid: String? = null,
    val computerIsActive: Boolean? = null,
    val computerPid: String? = null,
    val pcName: String? = null,
    val accessCode: String? = null,
    val cost: Int? = null,
    val thumbnail: Int = 0
)
