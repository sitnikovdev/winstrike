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
            var seatView = createMImage(seat)
        }
    }

    private fun createMImage(seat: Seat): ImageView {
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
    }
}
/*                let image = Asset.ChooseSeat.seatGrey.image

                //прибавляем к ширине и высоте подложки по 5 и меняем координаты соответственно, чтобы увеличить кликабельную зону
                let substrate = Shape(
                    form: Rect(
                        x: seat.dx - 2.5,
                        y: seat.dy - 2.5,
                        w: Double(image.size.width) + 5,
                        h: Double(image.size.height) + 5))

                substrate.onTap { _ in
                    guard seat.type == .available || seat.type == .vip else {
                        seatView.opacityVar.animate(to: 0.5)
                        DispatchQueue.main.asyncAfter(deadline: .now() + 0.3) {
                            seatView.opacityVar.animate(to: 1)
                        }
                        return
                    }

                    if !self.pickedSeats.contains(index) {
                        self.pickedSeats.insert(index)
                        seatView.srcVar.value = SeatType.picked.getSrc()
                        print(seat.pcname)
                        self.delegate?.seatPicked(id: seat.id, unselect: false, publicPid: seat.publicPid)
                    } else {
                        self.pickedSeats.remove(index)
                        seatView.srcVar.value = seat.type.getSrc()
                        self.delegate?.seatPicked(id: seat.id, unselect: true, publicPid: seat.publicPid)
                    }
                }

                mainGroup.contents.append(substrate)
                mainGroup.contents.append(seatView)
            }

        //добавляем стены
        for wall in gameRoom.walls {
            let line = Line(
                x1: Double(wall.start.x),
                y1: Double(wall.start.y),
                x2: Double(wall.end.x),
                y2: Double(wall.end.y))
                .stroke(fill: Color(val: 0xffffff), width: 1)
            mainGroup.contents.append(line)
        }

        for label in gameRoom.labels {
            let title = Text(text: label.text, font: Font(name: "Stem-Bold", size: 12), fill: Color.white, place: .move(dx: label.dx, dy: label.dy))
            mainGroup.contents.append(title)
        }

        self.node = mainGroup

        let screenSize = calculateScreenSize(seatSize: Asset.ChooseSeat.seatGrey.image.size)
        widthAnchor ~= screenSize.width
        heightAnchor ~= screenSize.height
    }

    private func calculateScreenSize(seatSize: CGSize) -> CGSize {
        let farthestSeat = gameRoom.seats.max(by: {(seat1: Seat, seat2: Seat) -> Bool in
            return getDist(CGPoint(x: seat2.dx, y: seat2.dy)) > getDist(CGPoint(x: seat1.dx, y: seat1.dy))
        })
        let width = CGFloat(farthestSeat?.dx ?? 0) + seatSize.width
        let height = CGFloat(farthestSeat?.dy ?? 0) + seatSize.height
        let screenSize = CGSize(width: width, height: height)
        return screenSize
    }

     fun createMImage(seat: Seat, type: SeatType){
        var image = type.image(seat.type)
        var seatView = MImage(image: image, opaque: false)

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
    }
}
