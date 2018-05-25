package ru.prsolution.winstrike.common

//
//  SeatType.swift
//  winstrike
//
//  Created by PRS on 28/02/2018.
//  Copyright © 2018 PR_Solution. All rights reserved.
//


import ru.prsolution.winstrike.R
import java.util.*

enum class ScreenType private constructor(val type: String) {
    MAIN("main"), PLACES("places"), PROFILE("profile"), CHOOSE("choose"), MAP("map");

/*
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
*/

    companion object {


        //Lookup table
        private val lookup = HashMap<String, ScreenType>()

        //Populate the lookup table on loading time
        init {
            for (status in ScreenType.values()) {
                lookup[status.type] = status
            }
        }

        //This method can be used for reverse lookup purpose
        operator fun get(status: String): ScreenType? {
            return lookup[status]
        }

        // TODO Get image resources for seatApi status
       fun getMenuResorce(status: ScreenType): Int? {
            when (status) {
                MAP -> return R.menu.main_toolbar_menu
                else -> {
                    return R.menu.main_toolbar_menu
                }
            }

        }
    }
}

