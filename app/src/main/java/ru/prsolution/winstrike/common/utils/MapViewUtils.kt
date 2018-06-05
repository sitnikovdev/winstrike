package ru.prsolution.winstrike.common.utils

import android.graphics.Point
import ru.prsolution.winstrike.mvp.models.Seat
import timber.log.Timber

class MapViewUtils() {

    companion object {

        /**вычисляет расстояние от начала координат до начальной точки картинки через гипотенузу*/
        private fun getDist(coord: Point): Double {
            val d = Math.sqrt(Math.pow(coord.x.toDouble(), 2.0) + Math.pow(coord.y.toDouble(), 2.0))
            return d
        }

        fun calculateScreenSize(seatSize: Point, seats: List<Seat>, mXScaleFactor: Float, mYScaleFactor: Float): Point {
            val mSeats = seats
            val farthestSeat = mSeats.maxWith(Comparator<Seat> { p1, p2 ->
                when {
                    getDist(Point(p1.dx.toInt(), p1.dy.toInt())) > getDist(Point(p2.dx.toInt(), p2.dy.toInt())) -> 1
                    getDist(Point(p1.dx.toInt(), p1.dy.toInt())) == getDist(Point(p2.dx.toInt(), p2.dy.toInt())) -> 0
                    else -> -1
                }
            })
            Timber.d("farthestSeat: %s", farthestSeat?.pcname)
            val point = Point()
            point.x = farthestSeat?.dx?.toInt() ?: 0
            point.y = farthestSeat?.dy?.toInt() ?: 0
            point.x = (point.x * mXScaleFactor).toInt() + seatSize.x * 2
            point.y = (point.y * mYScaleFactor / 1.5).toInt() + seatSize.y * 4

            return point
        }

        // Declare an extension function that calls a lambda called block if the value is null
        inline fun <T> T.guard(block: T.() -> Unit): T {
            if (this == null) block(); return this
        }

        fun calculateSubst(seat: Seat): Int {
            var value: Int = 0
//            val textvalue: Double = 18
            val id = seat.id
            if (arrayListOf("10", "11", "12", "13", "14", "15", "16", "17", "18", "19").contains(id)) {
                value = 8
            } else if (arrayListOf("26", "27", "28", "29", "30").contains(id)) {
                value = 22
            } else if (arrayListOf("31", "32", "33", "34", "35", "36").contains(id)) {
                value = 13
            } else if (arrayListOf("42", "43", "44", "45", "46", "47").contains(id)) {
                value = 25
            } else if (arrayListOf("48", "49", "50", "51", "52", "53").contains(id)) {
                value = 39
            } else if (arrayListOf("59", "60", "61", "62", "63", "64").contains(id)) {
                value = 48
            } else if (arrayListOf("38", "39", "40", "41", "37").contains(id)) {
                value = 26
            } else if (arrayListOf("54", "55", "56", "57", "58").contains(id)) {
                value = 36
            } else if (arrayListOf("65", "66", "67", "68", "69").contains(id)) {
                value = 42
            } else if (arrayListOf("70", "71", "72", "73", "74", "75", "76", "77", "78", "79").contains(id)) {
                value = 48
            } else if (arrayListOf("80", "81", "82", "83", "84", "85", "86", "87", "88", "89").contains(id)) {
                value = 54
            } else if (arrayListOf("90", "91", "92", "93", "94", "95", "96", "97", "98", "99").contains(id)) {
                value = 34
            } else if (arrayListOf("100", "101", "102", "103", "104", "105", "106", "107", "108", "109").contains(id)) {
                value = 44
            } else if (arrayListOf("110", "111", "112", "113", "114").contains(id)) {
                value = 50
            }
            return value
        }

        fun getLabelOffsetY(label: String): Int {
            return when (label) {
                "ЗАЛ" -> 0
                "HP STAGE 1" -> 35
                "HP STAGE 2" -> 35
                "VIP ROOM" -> 45
                "LG ROOM" -> 45
                else -> 50
            }
        }
    }
}
