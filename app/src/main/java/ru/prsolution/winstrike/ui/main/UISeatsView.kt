package ru.prsolution.winstrike.ui.main

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.VectorDrawable
import android.provider.MediaStore.Images.Media.getBitmap
import android.support.annotation.StyleRes
import android.support.v4.content.ContextCompat
import android.support.v4.content.res.ResourcesCompat
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import ru.prsolution.winstrike.R
import ru.prsolution.winstrike.WinstrikeApp
import ru.prsolution.winstrike.mvp.models.GameRoom
import ru.prsolution.winstrike.mvp.models.Seat
import ru.prsolution.winstrike.mvp.models.SeatType
import ru.prsolution.winstrike.mvp.models.Wall
import timber.log.Timber


class DrawView(context: Context, room: GameRoom) : View(context) {

    private val mSeats: List<Seat> = room.seats
    private val mPaint: Paint = Paint()
    lateinit var mRectWall: Rect
    private val mWall: Wall
    private var mScreenSize: Point;
    private var mXScaleFactor: Float
    private val mYScaleFactor: Float
    private val mLabels = room.labels
    private var mSeatFreeBtm: Bitmap
    val seatSize = Point()
    var hmap = HashMap<SeatType, Bitmap>()


    init {
        mPaint.color = Color.WHITE
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = 10f

        mSeatFreeBtm = getBitmap(context, R.drawable.ic_seat_gray)


        val height = WinstrikeApp.getInstance().displayHeightPx
        val width = WinstrikeApp.getInstance().displayWidhtPx
        mWall = room.walls[0]

        mXScaleFactor = (width / mWall.end.x)
        mYScaleFactor = (height / mWall.end.y) + 1


        seatSize.x = mSeatFreeBtm.width
        seatSize.y = mSeatFreeBtm.height


        mScreenSize = Point()
        mScreenSize = calculateScreenSize(seatSize)
        this.minimumWidth = mScreenSize.x
        this.minimumHeight = mScreenSize.y
        //fill bitmaps
        var seatBitmap: Bitmap
        for (seat in mSeats) {
            when (seat.type) {
                SeatType.FREE -> {
                    seatBitmap = getBitmap(context, R.drawable.ic_seat_gray)
                }
                SeatType.HIDDEN -> {
                    seatBitmap = getBitmap(context, R.drawable.ic_seat_darkgray)
                }
                SeatType.SELF_BOOKING -> {
                    seatBitmap = getBitmap(context, R.drawable.ic_seat_blue)
                }
                SeatType.BOOKING -> {
                    seatBitmap = getBitmap(context, R.drawable.ic_seat_red)
                }
                SeatType.VIP -> {
                    seatBitmap = getBitmap(context, R.drawable.ic_seat_yellow)
                }
            }
            hmap.put(seat.type, seatBitmap)
        }

    }



    /**вычисляет расстояние от начала координат до начальной точки картинки через гипотенузу*/
    private fun getDist(coord: Point): Double {
        val d = Math.sqrt(Math.pow(coord.x.toDouble(), 2.0) + Math.pow(coord.y.toDouble(), 2.0))
        return d
    }


    private fun calculateScreenSize(seatSize: Point): Point {
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


    override fun onDraw(canvas: Canvas) {
        canvas.drawColor(Color.BLACK)

        drawSeats(canvas)
        drawLabels(canvas)
        drawWalls(canvas, mWall)

    }

    private fun drawSeats(canvas: Canvas) {
        mSeats.forEachIndexed { index, seat ->

            val seatBitmap: Bitmap;
            when (seat.type) {
                SeatType.FREE -> {
                    seatBitmap = hmap[seat.type]!!
                }
                SeatType.HIDDEN -> {
                    seatBitmap = hmap[seat.type]!!
                }
                SeatType.SELF_BOOKING -> {
                    seatBitmap = hmap[seat.type]!!
                }
                SeatType.BOOKING -> {
                    seatBitmap = hmap[seat.type]!!
                }
                SeatType.VIP -> {
                    seatBitmap = hmap[seat.type]!!
                }
            }

            Timber.d("index:%s, type: %s", index, seat.type)
            val dx = seat.dx.toFloat() * mXScaleFactor
            val dy = seat.dy.toFloat() * mYScaleFactor / 1.5f
            val angle = Math.toDegrees(seat.angle)

            Timber.d("mXScaleFactor: %s", mXScaleFactor)
            Timber.d("mYScaleFactor: %s", mYScaleFactor)

            Timber.d("index[%s] - dx: %s, dy: %s", index, seat.dx, seat.dy)
            canvas.save()
            canvas.translate(dx, dy)
            canvas.rotate(angle.toFloat(), seatBitmap.width / 2f, seatBitmap.height / 2f)
            canvas.drawBitmap(seatBitmap, 0f, 0f, mPaint)
            canvas.restore()
        }
    }

    private fun drawLabels(canvas: Canvas) {
        mPaint.color = Color.WHITE
        mPaint.style = Paint.Style.FILL
        mPaint.strokeWidth = 3f
        mPaint.textSize = 48f

        for (label in mLabels) {
            val text = label.text
            val dx = label.dx * mXScaleFactor
            val dy = (label.dy * (mYScaleFactor / 1.5).toFloat()) + seatSize.y
            canvas.drawText(text, dx, dy, mPaint)
            // Draw horizontal line after end main hall section
            if (text.equals("HP STAGE 1")) {
                val colorOld = mPaint.color
                mPaint.color = Color.GRAY
                canvas.drawLine(dx, dy - seatSize.y * 2.5f, mScreenSize.x.toFloat() - seatSize.x, dy - seatSize.y * 2.5f, mPaint);
                mPaint.color = colorOld
            }
        }
    }

    private fun drawWalls(canvas: Canvas, wall: Wall) {
        mPaint.color = 0xffffff
        mPaint.style = Paint.Style.STROKE
        val leftXTop = wall.start.x * mXScaleFactor.toInt()
        val leftYTop = wall.start.x * (mYScaleFactor / 1.5).toInt()
        val bottomXRight = (wall.end.x * mXScaleFactor.toInt()) + (seatSize.x / 1.3).toInt()
        val bottomYRight = (wall.end.y * (mYScaleFactor).toInt()) - seatSize.y


        mRectWall = Rect(leftXTop, leftYTop, bottomXRight, bottomYRight)
        canvas.drawRect(mRectWall, mPaint)
    }


    private fun getBitmap(vectorDrawable: VectorDrawable): Bitmap {
        val bitmap = Bitmap.createBitmap(vectorDrawable.intrinsicWidth,
                vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
        vectorDrawable.draw(canvas)
        Timber.d("getBitmap: 1")
        return bitmap
    }

    private fun getBitmap(context: Context, drawableId: Int): Bitmap {
        Timber.d("getBitmap: 2")
        val drawable = ContextCompat.getDrawable(context, drawableId)
        return if (drawable is BitmapDrawable) {
            BitmapFactory.decodeResource(context.resources, drawableId)
        } else if (drawable is VectorDrawable) {
            getBitmap((drawable as VectorDrawable?)!!)
        } else {
            throw IllegalArgumentException("unsupported drawable type")
        }
    }


}

class UISeatsView(context: Context) : SurfaceView(context), SurfaceHolder.Callback {
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

}

