package ru.prsolution.winstrike.ui.main

import android.graphics.Point
import ru.prsolution.winstrike.mvp.models.GameRoom
import ru.prsolution.winstrike.mvp.models.SeatType
import java.util.Collections.rotate
import android.R.attr.y
import android.R.attr.x
import android.widget.ImageView
import ru.prsolution.winstrike.mvp.models.Seat


/*protocol UISeatsViewDelegate: class {
    func seatPicked(id: String, unselect: Bool, publicPid: String)
}*/

class UISeatsView {
//    weak var delegate: UISeatsViewDelegate?

    lateinit var gameRoom: GameRoom

    var pickedSeats = mutableSetOf<Int>()

    fun setData(gameRoom: GameRoom) {
        this.gameRoom = gameRoom
        this.drawRoom()
    }

    /**вычисляет расстояние от начала координат до начальной точки картинки через гипотенузу*/
    fun getDist(coord: Point): Double {
        val d = Math.sqrt(Math.pow(coord.x.toDouble(), 2.0) + Math.pow(coord.y.toDouble(), 2.0))
        return d
    }

    private fun drawRoom() {
//        var mainGroup = Group()
        //добавляем кресла
        gameRoom.seats.forEachIndexed { index, seat ->
//            var seatView = createMImage(seat)
        }
    }

/*    private fun createMImage(seat: Seat): ImageView {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        var image = SeatType.getImage(seat.type)
//        var seatView = MImage(image: image, opaque: false)

//        var animation = seatView.srcVar.value = "ChooseSeat/seatGrey.png";
        var seatTransform = Transform
                .move(dx: seat.dx, dy: seat.dy)
                .rotate(
                        angle: seat.angle,
                        x: Double(image.size.width) / 2,
                        y: Double(image.size.height) / 2
        )
        seatView.place = seatTransform
        return seatView
    }*/
}
