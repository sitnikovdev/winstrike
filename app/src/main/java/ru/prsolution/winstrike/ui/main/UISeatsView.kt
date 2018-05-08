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


class DrawView(context: Context, room: GameRoom) : View(context) {

    private val mSeats: List<Seat> = room.seats
    private val mPaint: Paint = Paint()
    lateinit var mRectWall: Rect
    private val mWall: Wall
    private var mSeatBitmap: Bitmap
    private var mScreenSize: Point;
    var mXScaleFactor: Float
    private val mYScaleFactor: Float
    private val mLabels = room.labels


    init {
        mPaint.color = Color.WHITE
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = 10f

        mSeatBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.seat_darkgrey);

        val height = WinstrikeApp.getInstance().displayHeightPx
        val width = WinstrikeApp.getInstance().displayWidhtPx
        mWall = room.walls[0]

        mXScaleFactor = (width / mWall.end.x)
        mYScaleFactor = (height / mWall.end.y)

        val seatSize: Point
        seatSize = Point()
        seatSize.x = mSeatBitmap.width.toInt()
        seatSize.y = mSeatBitmap.height.toInt()

        mScreenSize = Point()
        mScreenSize = calculateScreenSize(seatSize)
        this.setMinimumWidth(mScreenSize.x)
        this.setMinimumHeight(mScreenSize.y)
    }

    /**вычисляет расстояние от начала координат до начальной точки картинки через гипотенузу*/
    fun getDist(coord: Point): Double {
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
        val point: Point
        point = Point()
        point.x = farthestSeat?.dx?.toInt() ?: 0
        point.y = farthestSeat?.dy?.toInt() ?: 0
        point.x = (point.x * mXScaleFactor).toInt() + mSeatBitmap.width * 2
        point.y = (point.y * mYScaleFactor / 1.5).toInt() + mSeatBitmap.height * 4
        return point
    }


    override fun onDraw(canvas: Canvas) {
        canvas.drawColor(Color.BLACK)

        drawSeats(canvas)
        drawLabels(canvas)
        drawWalls(canvas, mWall)

    }

    private fun drawLabels(canvas: Canvas) {
        mPaint.color = Color.WHITE
        mPaint.style = Paint.Style.FILL
        mPaint.strokeWidth = 3f
        mPaint.textSize = 48f

        for (label in mLabels) {
            val text = label.text
            val dx = label.dx * mXScaleFactor
            val dy = (label.dy * (mYScaleFactor / 1.5).toFloat())+mSeatBitmap.height
            canvas.drawText(text, dx, dy, mPaint)
        }
    }

    private fun drawWalls(canvas: Canvas, wall: Wall) {
        mPaint.color = Color.RED
        mPaint.style = Paint.Style.STROKE
        val leftXTop = wall.start.x * mXScaleFactor.toInt()
        val leftYTop = wall.start.x * (mYScaleFactor / 1.5).toInt()
        val bottomXRight = (wall.end.x * mXScaleFactor.toInt()) + (mSeatBitmap.width / 1.3).toInt()
        val bottomYRight = (wall.end.y * (mYScaleFactor).toInt()) - mSeatBitmap.height
        mRectWall = Rect(leftXTop, leftYTop, bottomXRight, bottomYRight)
        canvas.drawRect(mRectWall, mPaint)
    }

    private fun drawSeats(canvas: Canvas) {
        mSeats.forEachIndexed { index, seat ->

            val dx = seat.dx.toFloat() * mXScaleFactor
            val dy = seat.dy.toFloat() * mYScaleFactor / 1.5f
            val angle = Math.toDegrees(seat.angle)

            Timber.d("mXScaleFactor: %s", mXScaleFactor)
            Timber.d("mYScaleFactor: %s", mYScaleFactor)

            Timber.d("index[%s] - dx: %s, dy: %s", index, seat.dx, seat.dy)
            canvas.save()
            canvas.translate(dx, dy)
            canvas.rotate(angle.toFloat(), mSeatBitmap.width / 2f, mSeatBitmap.height / 2f)
            canvas.drawBitmap(mSeatBitmap, 0f, 0f, mPaint)
            canvas.restore()
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

