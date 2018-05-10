package ru.prsolution.winstrike.ui.main

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
    }
}
