package ru.prsolution.winstrike.presentation.map

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Point
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.VectorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.view.animation.AlphaAnimation
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import java.util.LinkedHashMap
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import ru.prsolution.winstrike.R
import ru.prsolution.winstrike.common.utils.MapViewUtils
import ru.prsolution.winstrike.common.utils.Utils
import ru.prsolution.winstrike.datasource.model.PaymentResponse
import ru.prsolution.winstrike.domain.models.GameRoom
import ru.prsolution.winstrike.domain.models.Seat
import ru.prsolution.winstrike.domain.models.SeatType
import ru.prsolution.winstrike.presentation.utils.date.TimeDataModel
import ru.prsolution.winstrike.domain.models.Wall
import ru.prsolution.winstrike.networking.Service
import ru.prsolution.winstrike.presentation.main.MainActivity
import ru.prsolution.winstrike.presentation.utils.Constants
import ru.prsolution.winstrike.presentation.utils.pref.PrefUtils
import timber.log.Timber


/**
 * Created by oleg 24.01.2019
 */
class MapScreenFragment : Fragment() {

	private var mDlgMapLegend: Dialog? = null
	var rootLayout: RelativeLayout? = null
	private var snackbar: Snackbar? = null
	private var snackLayout: Snackbar.SnackbarLayout? = null

	private val RLW = RelativeLayout.LayoutParams.WRAP_CONTENT
	private var tvParams: RelativeLayout.LayoutParams? = null
	private var seatParams: RelativeLayout.LayoutParams? = null
	private var rootLayoutParams: RelativeLayout.LayoutParams? = null
	private val mPickedSeatsIds = LinkedHashMap<Int, String>()
	val height: Float = PrefUtils.displayHeightPx
	val width: Float = PrefUtils.displayWidhtPx
	var mXScaleFactor: Float? = null
	var mYScaleFactor: Float? = null


	var service: Service? = null
	var presenter: MapPresenter? = null

	private var mRoom: GameRoom? = null
	private var tvDivParam: RelativeLayout.LayoutParams? = null


	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val view = inflater.inflate(R.layout.frm_map, container, false)
		return view
	}

	override fun onStart() {
		super.onStart()
		snackbar!!.dismiss()
	}

	private fun initSnackBar() {
		snackbar = Snackbar.make(rootLayout!!, "", Snackbar.LENGTH_INDEFINITE)
		snackbar!!.view.setBackgroundColor(Color.TRANSPARENT)
		snackbar!!.view.setBackgroundResource(R.drawable.btn_bukking)
		val layoutInflater = this.layoutInflater
		val snackView = layoutInflater.inflate(R.layout.my_snackbar, null)
		snackLayout = snackbar!!.view as Snackbar.SnackbarLayout
		snackLayout!!.addView(snackView)
		snackLayout!!.setOnClickListener(BookingBtnListener())
		snackbar!!.dismiss()
	}


	fun onScreenInit() {
		rootLayoutParams = RelativeLayout.LayoutParams(RLW, RLW)
		initSnackBar()
	}


	fun showSeat(room: GameRoom) {
		this.mRoom = room
		drawSeat(mRoom)
	}

	fun drawSeat(room: GameRoom?) {
		var mWall: Wall?


		if (room!!.walls.size > 0) {
			mWall = room.walls[0]
			mXScaleFactor = width / mWall.end.x
			mYScaleFactor = height / mWall.end.y
		} else {
			mXScaleFactor = width / 358
			mYScaleFactor = height / 421
		}

		val seatSize = Point()

		val seatBitmap = getBitmap(context, R.drawable.ic_seat_gray)

		seatSize.set(seatBitmap.width, seatBitmap.height)
		val mScreenSize = MapViewUtils.calculateScreenSize(seatSize, room.seats, mXScaleFactor!! + 0.2f,
		                                                   mYScaleFactor!! - 1.5f)

		val params = rootLayout!!.layoutParams as FrameLayout.LayoutParams
		params.setMargins(-65, -80, 100, 80)

		// Width and Height of screen
		if (height <= Constants.SCREEN_HEIGHT_PX_1280) {
			params.width = mScreenSize.x
			params.height = mScreenSize.y + 250
//			mYScaleFactor = mYScaleFactor!! - 1.5f
		} else if (height <= Constants.SCREEN_HEIGHT_PX_1920) {
			params.setMargins(-35, -80, 100, 80)
			params.width = mScreenSize.x
			params.height = mScreenSize.y + 380
//			mYScaleFactor = mYScaleFactor!! - 2.0f
		} else if (height <= Constants.SCREEN_HEIGHT_PX_2560) {
			params.width = mScreenSize.x
			params.height = mScreenSize.y + 150
//			mYScaleFactor = mYScaleFactor!! - 3f
		} else {
			params.width = mScreenSize.x
			params.height = mScreenSize.y + 250
//			mYScaleFactor = mYScaleFactor!! - 1.5f
		}
		rootLayout!!.layoutParams = params

		for (seat in room.seats) {
			val name = seat.name
			//      Timber.d("id: %s, y: %s",seat.getId(),seat.getDy());
			val seatNumber = Utils.parseNumber(name)
			// TODO: 07/06/2018 For test (number of places by id):
			/*      Integer seatIdInt = Integer.parseInt(seat.getId().toString());
      seatNumber = seatIdInt.toString();*/
			val numberLenth = seatNumber.length
			var textOffsetX = 0
			var textOffsetY = -10
			if (height <= Constants.SCREEN_HEIGHT_PX_1280) {
				textOffsetY = 0
			}

			var dx: Int?
			var dy: Int?

			// Calculate offset by x for diffrent length of numbers (1, 2 and 3)
			if (numberLenth <= 1) {
				textOffsetX = 7
			} else if (numberLenth == 2) {
				textOffsetX = 20
			} else if (numberLenth == 3) {
				textOffsetX = 55
			}

			if (width <= Constants.SCREEN_WIDTH_PX_720) {
				if (numberLenth <= 1) {
					textOffsetX = 7
				} else if (numberLenth == 2) {
					textOffsetX = 10
				} else if (numberLenth == 3) {
					textOffsetX = 25
				}
			}

			dx = ((seat.dx - MapViewUtils.getSeatOffsetXArena1(seat)) * mXScaleFactor!!).toInt()
			dy = ((seat.dy + MapViewUtils.getSeatOffsetYArena1(seat)) * mYScaleFactor!!).toInt()

			// Seats numbers:
			tvParams = RelativeLayout.LayoutParams(RLW, RLW)
			tvParams!!.leftMargin = dx + seatSize.x / 2 - textOffsetX
			tvParams!!.topMargin = dy + seatSize.y - textOffsetY - 10

			val angle = radianToDegrees(seat)
			val angleInt = Math.round(angle)
			val angleAbs = Math.abs(angleInt)

			if (height <= Constants.SCREEN_HEIGHT_PX_1280) {
				if (angleAbs != 90) {
					tvParams!!.topMargin = dy + seatSize.y - textOffsetY - 2
				} else {
					tvParams!!.topMargin = dy + seatSize.y - textOffsetY + 2
				}
			} else if (height <= Constants.SCREEN_HEIGHT_PX_1920) {
				if (angleAbs != 90) {
					tvParams!!.topMargin = dy + seatSize.y - textOffsetY - 10
				} else {
					tvParams!!.topMargin = dy + seatSize.y - textOffsetY
				}
			} else if (height <= Constants.SCREEN_HEIGHT_PX_2560) {
				if (angleAbs != 90) {
					tvParams!!.topMargin = dy + seatSize.y - textOffsetY - 10
				} else {
					tvParams!!.topMargin = dy + seatSize.y - textOffsetY
				}

			} else {
				tvParams!!.topMargin = dy + seatSize.y - textOffsetY
			}

			val textView = TextView(context)
			textView.text = seatNumber

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
				textView.setTextAppearance(R.style.StemRegular10Gray)
			} else {
				textView.setTextAppearance(context, R.style.StemRegular10Gray)
			}
			textView.layoutParams = tvParams
			rootLayout!!.addView(textView)

			val ivSeat = ImageView(context)
			setImage(ivSeat, seat)

			rotateSeat(seatBitmap, seat, ivSeat)

			ivSeat.layoutParams = seatParams

			val mSeatViewOnClickListener = mSeatViewOnClickListener(textView, seat, ivSeat, seatBitmap, mPickedSeatsIds)
			ivSeat.setOnClickListener(mSeatViewOnClickListener)
			rootLayout!!.addView(ivSeat)
		}

		// Labels of Rooms
		for (label in room.labels) {
			val text = label.text
			var dx: Int?
			var dy: Int?

			dx = ((label.dx - MapViewUtils.getLabelOffsetXArena1(text)) * mXScaleFactor!!).toInt()
			dy = ((label.dy + MapViewUtils.getLabelOffsetYArena1(text)) * mYScaleFactor!!).toInt()

			tvParams = RelativeLayout.LayoutParams(RLW, RLW)
			tvParams!!.leftMargin = dx
			tvParams!!.topMargin = dy
			if (height <= Constants.SCREEN_HEIGHT_PX_1280) {
				tvParams!!.topMargin = dy - 5
			}
			val textView = TextView(context)
			textView.text = text
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
				textView.setTextAppearance(R.style.StemMedium15Primary)
			} else {
				textView.setTextAppearance(context, R.style.StemMedium15Primary)
			}

			textView.layoutParams = tvParams
			// Add horizontal line
			if (text == "HP STAGE 1") {
				dx = ((label.dx - MapViewUtils.getLabelOffsetXArena1(text)) * mXScaleFactor!!).toInt()
				dy = ((label.dy - MapViewUtils.getLabelOffsetYArena1(text)) * mYScaleFactor!!).toInt()
				tvDivParam = RelativeLayout.LayoutParams(RLW, RLW)
				tvDivParam!!.leftMargin = dx
				tvDivParam!!.topMargin = dy
			}

			rootLayout!!.addView(textView)
		}

	}


	override fun onDestroy() {
		super.onDestroy()
		snackLayout!!.setOnClickListener(null)
		for (i in 0 until rootLayout!!.childCount) {
			val v = rootLayout!!.getChildAt(i)
			if (v is ImageView) {
				v.setOnClickListener(null)
			}

		}
		if (this.presenter != null) {
			presenter!!.onStop()
			presenter = null
		}
		if (this.service != null) {
			this.service = null
		}
	}

	/**
	 * Save selected seat's pids in db. For offline mode compatibility.
	 *
	 * @param id - seat id
	 * @param unselect - is seat selected
	 * @param publicPid - pid of selected seat
	 */

	private fun onSelectSeat(id: String, unselect: Boolean, publicPid: String) {

		if (!unselect) {
			mPickedSeatsIds[Integer.parseInt(id)] = publicPid
		} else {
			mPickedSeatsIds.remove(Integer.parseInt(id))
		}

		TimeDataModel.pids = mPickedSeatsIds

		onPickedSeatChanged()
	}

	private fun onPickedSeatChanged() {
		if (!mPickedSeatsIds.isEmpty()) {
			snackbar!!.show()
		} else {
			snackbar!!.dismiss()
		}
	}

	private fun animateView(seatView: ImageView) {
		val animation1 = AlphaAnimation(0.5f, 1.0f)
		animation1.duration = 100
		animation1.startOffset = 300
		animation1.fillAfter = true
		seatView.startAnimation(animation1)
	}

	private fun rotateSeat(seatBitmap: Bitmap, seat: Seat, ivSeat: ImageView) {
		seatParams = RelativeLayout.LayoutParams(RLW, RLW)
		seatParams!!.leftMargin = ((seat.dx - MapViewUtils.getSeatOffsetXArena1(seat)) * mXScaleFactor!!).toInt()
		seatParams!!.topMargin = ((seat.dy + MapViewUtils.getSeatOffsetYArena1(seat)) * mYScaleFactor!!).toInt()

		val angle = radianToDegrees(seat)

		val pivotX = seatBitmap.width / 2f
		val pivotY = seatBitmap.height / 2f
		ivSeat.pivotX = pivotX
		ivSeat.pivotY = pivotY
		ivSeat.rotation = angle
	}

	private fun radianToDegrees(seat: Seat): Float {
		return Math.toDegrees(seat.angle).toFloat()
	}


	private fun setImage(seatImg: ImageView, seat: Seat) {
		when (seat.type) {
			SeatType.FREE -> {
				seatImg.setBackgroundResource(R.drawable.ic_seat_gray)
			}
			SeatType.HIDDEN -> {
				seatImg.setBackgroundResource(R.drawable.ic_seat_darkgray)
			}
			SeatType.SELF_BOOKING -> {
				seatImg.setBackgroundResource(R.drawable.ic_seat_blue)
			}
			SeatType.BOOKING -> {
				seatImg.setBackgroundResource(R.drawable.ic_seat_red)
			}
			SeatType.VIP -> {
				seatImg.setBackgroundResource(R.drawable.ic_seat_yellow)
			}
		}
	}


	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		this.presenter = MapPresenter(service, this)
		presenter!!.initScreen()
		presenter!!.readMap()
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		dlgMapLegend()
	}


	private inner class BookingBtnListener : View.OnClickListener {

		override fun onClick(view: View) {
			var timeFrom = ""
			var timeTo = ""
			timeFrom = TimeDataModel.start
			timeTo = TimeDataModel.end
			if (Utils.valideateDate(timeFrom, timeTo)) {
				presenter!!.onBookingClick()
			} else {
				toast(activity!!.resources.getString(R.string.toast_wrong_range))
			}
		}
	}

	fun onSnackBarShow() {
		snackbar!!.show()
	}

	fun onSnackBarHide() {
		snackbar!!.dismiss()
	}

	override fun onStop() {
		super.onStop()
		snackbar!!.dismiss()

	}


	fun showWait() {}

	fun removeWait() {}

	fun onGetPaymentResponseSuccess(payResponse: PaymentResponse) {
		Timber.tag("common").d("Pay successfully: %s", payResponse)

		val url = payResponse.redirectUrl
		//    Intent intent = new Intent(this.getContext(), YandexWebView.class);
		//    intent.putExtra("url", url);

		var intent = Intent()
		intent.putExtra("payments", true)
		startActivity(Intent(activity, MainActivity::class.java))

		intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
		startActivity(intent)
	}

	/**
	 * Something goes wrong, and we can't bye seat from Winstrike PC club. show user toast with description this fucking situation.
	 */
	fun onGetPaymentFailure(appErrorMessage: String) {
		val timeFrom = TimeDataModel.start
		val timeTo = TimeDataModel.end
		Timber.d("timeFrom: %s", timeFrom)
		Timber.d("timeTo: %s", timeTo)

		Timber.tag("common").w("Failure on pay: %s", appErrorMessage)
		if (appErrorMessage.contains(getString(R.string.msg_server_500))) {
			toast(getString(R.string.msg_server_internal_err))
		}
		if (appErrorMessage.contains(getString(R.string.msg_server_400))) {
			toast(getString(R.string.msg_no_data))
		}
		if (appErrorMessage.contains(getString(R.string.msg_server_401))) {
			toast(getString(R.string.msg_no_auth))
		}
		if (appErrorMessage.contains(getString(R.string.msg_serve_403))) {
			toast(getString(R.string.msg_auth_err))
		}
		if (appErrorMessage.contains(getString(R.string.msg_server_404))) {
			toast(getString(R.string.msg_no_seat_with_id))
		}
		if (appErrorMessage.contains(getString(R.string.msg_server_405))) {
			toast(getString(R.string.msg_auth_error))
		}
		if (appErrorMessage.contains(getString(R.string.msg_server_424))) {
			toast(getString(R.string.msg_date_error))
		}
		if (appErrorMessage.contains(getString(R.string.msg_server_416))) {
			toast(getString(R.string.msg_booking_error))
		} else {
			toast(getString(R.string.msg_bookin_err))
		}
	}

	protected fun toast(message: String) {
		if (message.contains("авторизации")) {
			Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
		} else {
			Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
		}
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

	private fun getBitmap(context: Context?, drawableId: Int?): Bitmap {
		Timber.d("getBitmap: 2")
		val drawable = ContextCompat.getDrawable(context!!, drawableId!!)
		val bitmap: Bitmap

		if (drawable is BitmapDrawable) {
			return BitmapFactory.decodeResource(context.resources, drawableId)
		} else if (drawable is VectorDrawable) {
			bitmap = getBitmap((drawable as VectorDrawable?)!!)
		} else {
			throw IllegalArgumentException("unsupported drawable type")
		}
		return bitmap
	}


	private inner class mSeatViewOnClickListener(private val seatName: TextView, private val seat: Seat,
	                                             private val ivSeat: ImageView, private val seatBitmap: Bitmap,
	                                             private val mPickedSeatsIds: LinkedHashMap<*, *>) : View.OnClickListener {

		override fun onClick(v: View) {
			if (seat.type === SeatType.FREE || seat.type === SeatType.VIP) {
				if (!mPickedSeatsIds.containsKey(Integer.parseInt(seat.id))) {
					ivSeat.setBackgroundResource(R.drawable.ic_seat_picked)
					onSelectSeat(seat.id, false, seat.publicPid)
					Timber.d("Seat id: %s,type: %s, name: %s, pid: %s", seat.id, seat.type, seat.pcname, seat.publicPid)
					seatName.setTextColor(Color.WHITE)
					seatName.setTypeface(null, Typeface.BOLD)
				} else {
					rootLayout!!.removeView(ivSeat)
					setImage(ivSeat, seat)
					rotateSeat(seatBitmap, seat, ivSeat)
					rootLayout!!.addView(ivSeat)
					onSelectSeat(seat.id, true, seat.publicPid)
					seatName.setTextColor(ContextCompat.getColor(activity!!, R.color.label_gray))
				}
			} else {
				Timber.d("Seat id: %s,type: %s, name: %s, pid: %s", seat.id, seat.type, seat.pcname, seat.publicPid)
				animateView(ivSeat)
				seatName.setTypeface(null, Typeface.NORMAL)
			}
		}
	}

	// TODO: remove this Map actions block:
	private fun dlgMapLegend() {
		mDlgMapLegend = Dialog(activity, android.R.style.Theme_Dialog)
		mDlgMapLegend!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
		mDlgMapLegend!!.setContentView(R.layout.dlg_legend)
		val tvSee = mDlgMapLegend!!.findViewById<TextView>(R.id.tv_see)

		tvSee.setOnClickListener { mDlgMapLegend!!.dismiss() }

		mDlgMapLegend!!.setCanceledOnTouchOutside(true)
		mDlgMapLegend!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
		mDlgMapLegend!!.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
		val window = mDlgMapLegend!!.window
		val wlp = window!!.attributes

		wlp.gravity = Gravity.TOP
		wlp.flags = wlp.flags and WindowManager.LayoutParams.FLAG_DIM_BEHIND.inv()
		wlp.y = Constants.LEGEND_MAP_TOP_MARGIN
		window.attributes = wlp

		mDlgMapLegend!!.setCanceledOnTouchOutside(false)
		mDlgMapLegend!!.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
		mDlgMapLegend!!.dismiss()

	}


}
