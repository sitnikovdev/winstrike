package ru.prsolution.winstrike.ui.main

import android.content.Context
import android.graphics.*
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import ru.prsolution.winstrike.R
import ru.prsolution.winstrike.WinstrikeApp
import ru.prsolution.winstrike.mvp.models.GameRoom
import ru.prsolution.winstrike.mvp.models.Seat
import ru.prsolution.winstrike.mvp.models.Wall
import timber.log.Timber


/*protocol UISeatsViewDelegate: class {
    func seatPicked(id: String, unselect: Bool, publicPid: String)
}*/
public class DrawView(context: Context, room: GameRoom) : View(context) {

    val seats: List<Seat> = room.seats
    var p: Paint
    var rect: Rect
    var bitmap: Bitmap
    var dx = 0f
    var dy = 0f
    var dxx = 0f
    var dyy = 0f
    var angle : Double
    var xScaleFactor: Double
    var yScaleFactor: Double


    // Ряд
    var raw: Int

    init {
        raw = 1
        p = Paint()
        // настройка кисти
        // красный цвет
        p.color = Color.WHITE
        p.style = Paint.Style.STROKE
        // толщина линии = 10
        p.setStrokeWidth(10f)
        rect = Rect()
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.seat_darkgrey);

        val height = WinstrikeApp.getInstance().displayHeightPx
        val widht =  WinstrikeApp.getInstance().displayWidhtPx
        val wall:Wall
        wall = room.walls[0]

        xScaleFactor = (widht / wall.end.x).toDouble()
        yScaleFactor = (height/ wall.end.y).toDouble()
        Timber.d("xScaleFactor: %s",xScaleFactor)
        Timber.d("yScaleFactor: %s",yScaleFactor)

        angle = 0.0
    }

    override fun onDraw(canvas: Canvas) {
        // заливка канвы цветом
        //canvas.drawARGB(80, 102, 204, 255)
        canvas.drawColor(Color.BLACK)

        var prevSeatDx = seats[0].dx.toFloat()
        var prevSeatDy = seats[0].dy.toFloat()


        seats.subList(0, 31).forEachIndexed { index, seat ->

            /*
                Если текущий элемент не первый, определяем смещение по X как разность между предыщим элементом и текущим
             */
/*            if (index > 0) {
                dxx = Math.abs(seats[index].dx.toFloat() - seats[index - 1].dx.toFloat()) + bitmap.width
                dx += dxx
            } else {
                dx = seat.dx.toFloat()
            }*/

            /*
              Если координата X текущего элемнта меньше чем предудущего, произошел переход на следущую строку.
             */
/*            if (prevSeatDx > seat.dx) {
                Timber.d("New row!")
                raw += 1
                dx =  seat.dx.toFloat()
            }*/

            /*
              Если ряд первый определяем смещение по Y
             */
/*            if (raw == 1) {
                dy = seat.dy.toFloat()
            } else {
                dy = seat.dy.toFloat() + 32f
            }*/
            dy = seat.dy.toFloat() + bitmap.height + 32f
            if (raw == 3) {
                Timber.d("index[%s] - 3 raw: %s",index, raw)
            }

            angle = Math.toDegrees(seat.angle)

            Timber.d("index[%s] - raw: %s",index, raw)
            Timber.d("index[%s] - x: %s, y: %s", index, seat.dx, seat.dy)
            Timber.d("index[%s] - dx: %s, dy: %s", index, dx, dy)
            canvas.save()
            //canvas.rotate(angle.toFloat(),bitmap.width /2f, bitmap.height/2f)
            canvas.translate(dx, dy)
            canvas.drawBitmap(bitmap, 0f, 0f, p)
            canvas.drawPoint(dx, dy, p)
            canvas.restore()

            prevSeatDx = seat.dx.toFloat()
            prevSeatDy = seat.dy.toFloat()

        }

        class UISeatsView(context: Context) : SurfaceView(context), SurfaceHolder.Callback {
            //    weak var delegate: UISeatsViewDelegate?
            private val sh: SurfaceHolder = holder
            private val paint = Paint(ANTI_ALIAS_FLAG)
            private val rect = Rect(50, 50, 100, 100)

            init {
                sh.addCallback(this)
                paint.color = Color.BLUE
                paint.style = Paint.Style.FILL
            }

            override fun surfaceCreated(holder: SurfaceHolder) {
                val canvas = sh.lockCanvas()
                canvas.drawColor(Color.BLACK)
                canvas.drawRect(rect, paint)
                sh.unlockCanvasAndPost(canvas)
            }

            override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int,
                                        height: Int) {
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {}

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
    }
}
