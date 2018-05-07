package ru.prsolution.winstrike.ui.main

import android.graphics.Point
import ru.prsolution.winstrike.mvp.models.GameRoom
import ru.prsolution.winstrike.mvp.models.SeatType
import java.util.Collections.rotate
import android.R.attr.y
import android.R.attr.x
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.view.SurfaceView
import android.widget.ImageView
import ru.prsolution.winstrike.mvp.models.Seat
import android.view.SurfaceHolder
import android.graphics.Paint.ANTI_ALIAS_FLAG




/*protocol UISeatsViewDelegate: class {
    func seatPicked(id: String, unselect: Bool, publicPid: String)
}*/

class BubbleSurfaceView(context: Context) : SurfaceView(context), SurfaceHolder.Callback {
    private val sh: SurfaceHolder
    private val paint = Paint(ANTI_ALIAS_FLAG)

    init {
        sh = holder
        sh.addCallback(this)
        paint.setColor(Color.BLUE)
        paint.setStyle(Paint.Style.FILL)
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        val canvas = sh.lockCanvas()
        canvas.drawColor(Color.BLACK)
        canvas.drawCircle(100f, 200f, 50f, paint)
        sh.unlockCanvasAndPost(canvas)
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int,
                                height: Int) {
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {}
}

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
//            var seatView = createMImage(seatApi)
        }
    }

/*    private fun createMImage(seatApi: SeatApi): ImageView {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        var image = SeatType.getImage(seatApi.type)
//        var seatView = MImage(image: image, opaque: false)

//        var animation = seatView.srcVar.value = "ChooseSeat/seatGrey.png";
        var seatTransform = Transform
                .move(dx: seatApi.dx, dy: seatApi.dy)
                .rotate(
                        angle: seatApi.angle,
                        x: Double(image.size.width) / 2,
                        y: Double(image.size.height) / 2
        )
        seatView.place = seatTransform
        return seatView
    }*/
}
