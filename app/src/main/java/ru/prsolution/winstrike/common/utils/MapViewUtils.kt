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

        fun getSeatOffsetY(seat: Seat): Int {
            var value: Int = 0
            val id = seat.id
            // First two blocks (1 - 20)
            if (arrayListOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9").contains(id)) {
                value = -12
            } else if (arrayListOf("10", "11", "12", "13", "14", "15", "16", "17", "18", "19").contains(id)) {
                value = -7

            // Rotated rows (21 - 65)
            } else if (arrayListOf("20", "21", "22", "23", "24", "25").contains(id)) {
                value = -24
            } else if (arrayListOf("31", "32", "33", "34", "35", "36").contains(id)) {
                value = -14
            } else if (arrayListOf("42", "43", "44", "45", "46", "47").contains(id)) {
                value = -2
            } else if (arrayListOf("48", "49", "50", "51", "52", "53").contains(id)) {
                value = 5
            } else if (arrayListOf("59", "60", "61", "62", "63", "64").contains(id)) {
                value = 18

           // 27 - 42
            } else if (arrayListOf("26", "27", "28", "29", "30").contains(id)) {
                value = 0
            } else if (arrayListOf("37", "38", "39", "40", "41").contains(id)) {
                value = 8

            // 71 - 85
            } else if (arrayListOf("70", "71", "72", "73", "74").contains(id)) {
                value = 12
            } else if (arrayListOf("80", "81", "82", "83", "84").contains(id)) {
                value = 20

            // 55 - 70
            } else if (arrayListOf("54", "55", "56", "57", "58").contains(id)) {
                value = 6
            } else if (arrayListOf("65", "66", "67", "68", "69").contains(id)) {
                value = 14

             // 76 - 90
            } else if (arrayListOf("75", "76", "77", "78", "79").contains(id)) {
                value = 12
            } else if (arrayListOf("85", "86", "87", "88", "89").contains(id)) {
                value = 20

            // HP STAGE 1
            } else if (arrayListOf("90", "91", "92", "93", "94").contains(id)) {
                value = 2
            // HP STAGE 2
            } else if (arrayListOf("95", "96", "97", "98", "99").contains(id)) {
                value = 2

            // VIP ROOM && LG ROOM
            } else if (arrayListOf("100", "101", "102", "103", "104", "105", "106", "107", "108", "109").contains(id)) {
                value = 2
            } else if (arrayListOf("110", "111", "112", "113", "114").contains(id)) {
                value = 8
            }
            return value
        }

        fun getSeatOffsetX(seat: Seat): Int {
            var value: Int = 0
            val id = seat.id
            if (arrayListOf("5", "6", "7", "8", "9", "15", "16", "17", "18", "19").contains(id)) {
                value = 20
            } else if (arrayListOf("26", "27", "28", "29", "30").contains(id)) {
                value = 20
            } else if (arrayListOf("37", "38", "39", "40", "41").contains(id)) {
                value = 20
            } else if (arrayListOf("54", "55", "56", "57", "58").contains(id)) {
                value = 20
            } else if (arrayListOf("65", "66", "67", "68", "69").contains(id)) {
                value = 20
            } else if (arrayListOf("75", "76", "77", "78", "79").contains(id)) {
                value = 20
            } else if (arrayListOf("85", "86", "87", "88", "89").contains(id)) {
                value = 20
            } else if (arrayListOf("95", "96", "97", "98", "99").contains(id)) {
                value = 20
            } else if (arrayListOf("105", "106", "107", "108", "109").contains(id)) {
                value = 20
            } else if (arrayListOf("110", "111", "112", "113", "114").contains(id)) {
                value = 20
            }
            return value
        }


        fun getLabelOffsetX(label: String): Int {
            return when (label) {
                "ЗАЛ" -> 0
                "HP STAGE 1" -> -2
                "HP STAGE 2" -> 16
                "SENNHEISER ROOM" -> 30
                "LG ROOM" -> 16
                else -> 16
            }
        }

        fun getLabelOffsetY(label: String): Int {
            return when (label) {
                "ЗАЛ" -> 0
                "HP STAGE 1" -> 4
                "HP STAGE 2" -> 4
                "SENNHEISER ROOM" ->  4
                "LG ROOM" -> 4
                else -> 4
            }
        }
    }
}
