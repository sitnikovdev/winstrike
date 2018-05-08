package ru.prsolution.winstrike.ui.main

import android.content.Context
import android.graphics.*
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import ru.prsolution.winstrike.R
import ru.prsolution.winstrike.mvp.models.GameRoom
import ru.prsolution.winstrike.mvp.models.Seat
import timber.log.Timber






public class DrawView(context: Context, room: GameRoom) : View(context) {

    var paint: Paint
    var bitmap1: Bitmap
    var bitmap2: Bitmap
    var bitmap3: Bitmap
    var bitmapIcon: Bitmap
    var x = 0.0
    var y = 0.0
    var xp = 0.0
    var yp = 0.0
    private val seats: List<Seat> = room.seats;

    init {
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.style = Paint.Style.STROKE


        bitmap1 = Bitmap.createBitmap(100, 100, Bitmap.Config.RGB_565)
        bitmap2 = Bitmap.createBitmap(100, 100, Bitmap.Config.RGB_565)
        bitmap3 = Bitmap.createBitmap(100, 100, Bitmap.Config.RGB_565)

        bitmapIcon = BitmapFactory.decodeResource(getResources(), R.drawable.seat_grey)

        var canvas = Canvas(bitmap1)
        canvas.drawBitmap(bitmapIcon, 0f, 0f, paint)

        canvas = Canvas(bitmap2)
        canvas.drawBitmap(bitmapIcon, 0f, 0f, paint)

        canvas = Canvas(bitmap3)
        canvas.drawBitmap(bitmapIcon, 0f, 0f, paint)
    }

    override fun onDraw(canvas: Canvas) {
        //canvas.drawARGB(80, 102, 204, 255)
        canvas.drawARGB(255, 255, 255, 255)

/*        Timber.d("dx: %s  dy: %s",seats[0].dx,seats[0].dy)
        canvas.translate(seats[0].dx.toFloat(), seats[0].dy.toFloat())
        canvas.drawBitmap(bitmapIcon, 0f, 0f, paint)*/

        //for (seat in seats.subList(1,3)) {
        canvas.translate(seats[0].dx.toFloat(),seats[0].dy.toFloat())
        xp = seats[0].dx;
        yp = seats[0].dy;
        seats.subList(0,12).forEachIndexed { index, seat ->
            //save previos element:
            if (index - 1 >= 0) {
                 xp = seats[index-1].dx;
                 yp = seats[index-1].dy;
            } else {
                 xp = seats[0].dx
                 yp = seats[0].dy;
            }

            Timber.d("index: %s, [xp: %s  yp: %s]",index, xp, yp)
            Timber.d("index: %s, DxDy [dx: %s  dy: %s]",index, seat.dx, seat.dy)
            x = Math.abs (seat.dx - xp) + bitmapIcon.width
            y = if (Math.abs (seat.dy - yp) > 0 ) seat.dy + 32 else 0.0
            x = if (Math.abs (seat.dy - yp) > 0 ) 32.0   else Math.abs (seat.dx - xp) + bitmapIcon.width
            Timber.d("index: %s, Translate [x: %s  y: %s]",index, x, y)
            canvas.translate(x.toFloat(), y.toFloat())
            canvas.drawBitmap(bitmapIcon, 0f, 0f, paint)

        }

/*        Timber.d("dx: %s  dy: %s",seats[1].dx,seats[1].dy)
        x = (seats[2].dx - seats[1].dx) + bitmapIcon.width
        y = seats[2].dy - seats[1].dy
        Timber.d("x: %s  y: %s",x,y)
        canvas.translate(x.toFloat(),y.toFloat())
        canvas.drawBitmap(bitmapIcon, 0f, 0f, paint)*/

/*        canvas.save()
        Timber.d("dx: %s  dy: %s",seats[2].dx,seats[2].dy)
        canvas.translate(seats[2].dx.toFloat() + bitmapIcon.width, seats[2].dy.toFloat())
//        canvas.drawRect(0f, 0f, 100f, 100f, paint)
        canvas.drawBitmap(bitmapIcon, 0f, 0f, paint)
        canvas.restore()*/
    }

}





/*protocol UISeatsViewDelegate: class {
    func seatPicked(id: String, unselect: Bool, publicPid: String)
}*/


class UISeatsView(context: Context, gameRoom: GameRoom) : SurfaceView(context), SurfaceHolder.Callback {
    //    weak var delegate: UISeatsViewDelegate?
    private val sh: SurfaceHolder = holder
    private val paint = Paint(ANTI_ALIAS_FLAG)
//    private val rect = Rect(50, 50, 100, 100)
    private var bitmap: Bitmap
    private val seats: List<Seat> = gameRoom.seats;
    var bitmap1: Bitmap;
    var bitmap2: Bitmap;
    var bitmap3: Bitmap;

    init {
        val metrics = context.getResources().getDisplayMetrics()
        val w = metrics.widthPixels
        val h = metrics.heightPixels
        Timber.d("Screen widht: %s ",w)
        Timber.d("Screen heigh: %s ",h)

        sh.addCallback(this)
        paint.color = Color.BLACK
        paint.style = Paint.Style.FILL
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.seat_grey);

        bitmap1 = Bitmap.createBitmap(100, 100, Bitmap.Config.RGB_565);
        bitmap2 = Bitmap.createBitmap(100, 100, Bitmap.Config.RGB_565);
        bitmap3 = Bitmap.createBitmap(100, 100, Bitmap.Config.RGB_565);
        val info = String.format("Info: size = %s x %s, bytes = %s (%s), config = %s",
                bitmap.width,
                bitmap.height,
                bitmap.byteCount,
                bitmap.rowBytes,
                bitmap.config)
        Timber.d(info)

/*        var canvas = Canvas(bitmap1)
        canvas.drawBitmap(bitmap, 9f, 7f, paint)

        canvas = Canvas(bitmap2)
        canvas.drawBitmap(bitmap, 0f, 0f, paint)

        canvas = Canvas(bitmap3)
        canvas.drawBitmap(bitmap, 0f, 0f, paint)*/

    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        val canvas = sh.lockCanvas()
        //canvas.drawColor(Color.BLACK)
        canvas.drawARGB(80, 102, 204, 255);
        createVImage(canvas, seats)

/*
        canvas.translate(100f, 100f);
        canvas.drawRect(0f,0f,100f,100f,paint);
        canvas.drawBitmap(bitmap1, 0f, 0f, paint);

        canvas.translate(150f, 0f);
        canvas.drawRect(0f,0f,100f,100f,paint);
        canvas.drawBitmap(bitmap2, 0f, 0f, paint);

        canvas.translate(150f, 0f);
        canvas.drawRect(0f,0f,100f,100f,paint);
        canvas.drawBitmap(bitmap3, 0f, 0f, paint);*/

        sh.unlockCanvasAndPost(canvas)
    }

    private fun createVImage(canvas: Canvas, seats: List<Seat>) {
        canvas.save()
        canvas.translate(seats.get(0).dx.toFloat(), seats.get(0).dy.toFloat());
        canvas.drawRect(0f,0f,100f,100f,paint);
        canvas.drawBitmap(bitmap, 0f, 0f, paint);
        canvas.restore()

        canvas.save()
        canvas.translate(seats.get(1).dx.toFloat() + bitmap.width, seats.get(1).dy.toFloat());
        canvas.drawRect(0f,0f,100f,100f,paint);
        canvas.drawBitmap(bitmap, 0f, 0f, paint);
        canvas.restore()

        canvas.save()
        canvas.translate(seats.get(2).dx.toFloat() + bitmap.width, seats.get(2).dy.toFloat());
        canvas.drawRect(0f,0f,100f,100f,paint);
        canvas.drawBitmap(bitmap, 0f, 0f, paint);
        canvas.restore()

        for (seat in seats.slice(1..2)) {
//            canvas.rotate(0f, bitmap.width.toFloat() / 2, bitmap.height.toFloat() / 2)
//            canvas.drawRect(seat.dx.toFloat()-2.5f,seat.dy.toFloat()-2.5f,bitmap.width.toFloat()+5,bitmap.height.toFloat()+5,paint);
//            canvas.translate(bitmap.width.toFloat()+5, seat.dy.toFloat())
//            canvas.drawBitmap(bitmap, seat.dx.toFloat(), seat.dy.toFloat(), paint)

/*            canvas.save()
            canvas.translate(seat.dx.toFloat() + bitmap.width, seat.dy.toFloat());
            canvas.drawRect(0f,0f,100f,100f,paint);
            canvas.drawBitmap(bitmap, 0f, 0f, paint);
            canvas.restore()*/

        }
    }

    fun pad(Src: Bitmap, padding_x: Int, padding_y: Int): Bitmap {
        val outputimage = Bitmap.createBitmap(Src.width + padding_x, Src.height + padding_y, Bitmap.Config.ARGB_8888)
        val can = Canvas(outputimage)
        can.drawARGB(0xFF, 0xFF, 0xFF, 0xFF) //This represents White color
        can.drawBitmap(Src, padding_x.toFloat(), padding_y.toFloat(), null)
        return outputimage
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int,
                                height: Int) {
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {}


    var pickedSeats = mutableSetOf<Int>()

    fun setData(gameRoom: GameRoom) {
//        this.drawRoom()
    }

    /**вычисляет расстояние от начала координат до начальной точки картинки через гипотенузу*/
    fun getDist(coord: Point): Double {
        val d = Math.sqrt(Math.pow(coord.x.toDouble(), 2.0) + Math.pow(coord.y.toDouble(), 2.0))
        return d
    }

    private fun drawRoom() {
//        var mainGroup = Group()
        //добавляем кресла
//        gameRoom.seats.forEachIndexed { index, seat ->
            //            var seatView = createMImage(seatApi)
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

