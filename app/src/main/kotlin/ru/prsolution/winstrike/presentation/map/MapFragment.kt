package ru.prsolution.winstrike.presentation.map

import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Point
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.VectorDrawable
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
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.frm_map.rootMap
import org.jetbrains.anko.support.v4.toast
import ru.prsolution.winstrike.R
import ru.prsolution.winstrike.presentation.utils.MapViewUtils
import ru.prsolution.winstrike.presentation.utils.Utils
import ru.prsolution.winstrike.domain.payment.PaymentResponse
import ru.prsolution.winstrike.domain.models.ArenaMap
import ru.prsolution.winstrike.domain.models.ArenaSchema
import ru.prsolution.winstrike.domain.models.Room
import ru.prsolution.winstrike.domain.models.SeatMap
import ru.prsolution.winstrike.domain.models.SeatType
import ru.prsolution.winstrike.presentation.main.MainViewModel
import ru.prsolution.winstrike.presentation.utils.Constants
import ru.prsolution.winstrike.presentation.utils.date.TimeDataModel
import ru.prsolution.winstrike.presentation.utils.pref.PrefUtils
import timber.log.Timber
import java.util.LinkedHashMap


/**
 * Created by oleg 24.01.2019
 */
class MapFragment : Fragment() {

	private var mDlgMapLegend: Dialog? = null
	var mapLayout: RelativeLayout? = null
	private var snackbar: Snackbar? = null
	private var snackLayout: Snackbar.SnackbarLayout? = null

	private val RLW = RelativeLayout.LayoutParams.WRAP_CONTENT
	private var numberParams: RelativeLayout.LayoutParams? = null
	private var seatParams: RelativeLayout.LayoutParams? = null
	private var rootLayoutParams: RelativeLayout.LayoutParams? = null
	private val mPickedSeatsIds = LinkedHashMap<Int, String>()

	val height: Float = PrefUtils.displayHeightPx
	val width: Float = PrefUtils.displayWidhtPx
	var mXScaleFactor: Float? = null
	var mYScaleFactor: Float? = null


	var mVm: MainViewModel? = null
	private var mRoom: ArenaMap? = null
	private var tvDivParam: RelativeLayout.LayoutParams? = null
	private var room: Room? = null


	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		dlgMapLegend()

		mVm = activity?.let { ViewModelProviders.of(it)[MainViewModel::class.java] }

	}


	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

		return inflater.inflate(R.layout.frm_map, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
/*		this.presenter = MapPresenter(service, this)
		presenter!!.initScreen()
		presenter!!.readMap()*/

//		initSnackBar()

		activity?.let {
			mVm?.arena?.observe(it, Observer {
				this.room = it.data
				mapLayout = view.findViewById(R.id.rootMap)
				readMap()
			})
		}

	}

	private fun readMap() {
		requireNotNull(room)
		{ "++++ RoomLayoutFactory must be init. ++++" }

		val room = ArenaMap(room)
		rootLayoutParams = RelativeLayout.LayoutParams(RLW, RLW)
		requireNotNull(mapLayout)
		{ "++++ Map Fragment root layout must not be null. ++++" }

		showSeat(room)
	}


	private fun showSeat(room: ArenaMap) {
		this.mRoom = room
		drawSeat(mRoom)
	}

	// TODO: Move this code in Interactor
	private fun drawSeat(room: ArenaMap?) {

		val schema = room?.arenaSchema

		mXScaleFactor = width / 358
		mYScaleFactor = height / 421

		if (schema == ArenaSchema.WINSTRIKE) {
			mXScaleFactor = (width / 358) * 1
			mYScaleFactor = (height / 421) * 1.5f
		} else {
			mXScaleFactor = width / 358
			mYScaleFactor = height / 421
		}

		val seatSize = Point()

		val seatBitmap = getBitmap(context, R.drawable.ic_seat_gray)

		seatSize.set(seatBitmap.width, seatBitmap.height)
		val mScreenSize = MapViewUtils.calculateScreenSize(seatSize, room!!.seats, mXScaleFactor!! + 0.2f,
		                                                   mYScaleFactor!! - 1.5f)

		val mapLP = mapLayout!!.layoutParams as FrameLayout.LayoutParams
		mapLP.setMargins(-65, -80, 100, 80)

		// Calculate  width and height for different Android screen sizes

		if (height <= Constants.SCREEN_HEIGHT_PX_1280) {
			mapLP.width = mScreenSize.x
			mapLP.height = mScreenSize.y + 250
			mYScaleFactor = mYScaleFactor!! - 1.5f
		} else if (height <= Constants.SCREEN_HEIGHT_PX_1920) {
			mapLP.setMargins(-35, -80, 100, 80)
			mapLP.width = mScreenSize.x
			mapLP.height = mScreenSize.y + 380
			mYScaleFactor = mYScaleFactor!! - 2.0f
		} else if (height <= Constants.SCREEN_HEIGHT_PX_2560) { // Samsung GX-7
			mapLP.width = mScreenSize.x
			mapLP.height = mScreenSize.y + 150
			mYScaleFactor = mYScaleFactor!! - 3f
			if (schema == ArenaSchema.WINSTRIKE) { // Winstrike Arena
				mapLP.height = mScreenSize.y + 850
				mapLP.width = mScreenSize.x + 500
				mYScaleFactor = mYScaleFactor!! - 0f
				mXScaleFactor = mXScaleFactor!! - 0.2f
				mapLP.setMargins(0, -250, 0, 80)
			}
		} else {
			mapLP.width = mScreenSize.x
			mapLP.height = mScreenSize.y + 250
			mYScaleFactor = mYScaleFactor!! - 1.5f
		}
		mapLayout!!.layoutParams = mapLP


		// Draw seats and numbers

		for (seat in room.seats) {

			val numberTextView = TextView(context)
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
				numberTextView.setTextAppearance(R.style.StemRegular10Gray)
			} else {
				numberTextView.setTextAppearance(context, R.style.StemRegular10Gray)
			}

			setNumber(seat, numberTextView)

			// Visualize seat
			ImageView(context).apply {
				setImage(this, seat)
				setSeat(seatBitmap, seat, this)
				this.layoutParams = seatParams

				// On seat click listener
				this.setOnClickListener(SeatViewOnClickListener(numberTextView, seat, this, seatBitmap,
				                                                mPickedSeatsIds))
				mapLayout!!.addView(this)
			}

		}

		// Draw labels for halls

		for (label in room.labels) {
			val text = label.text
			var offsetY = -5

			if (schema == ArenaSchema.WINSTRIKE) {
				offsetY = 5
			}

			val dx: Int? = ((label.dx?.minus(0))?.times(mXScaleFactor!!))?.toInt()
			val dy: Int? = ((label.dy?.plus(offsetY))?.times(mYScaleFactor!!))?.toInt()

			numberParams = RelativeLayout.LayoutParams(RLW, RLW)
			numberParams!!.leftMargin = dx!!
			numberParams!!.topMargin = dy!!

			if (height <= Constants.SCREEN_HEIGHT_PX_1280) {
				numberParams!!.topMargin = dy - 5
			}

			val textView = TextView(context)
			textView.text = text
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
				textView.setTextAppearance(R.style.StemMedium15Primary)
			} else {
				textView.setTextAppearance(context, R.style.StemMedium15Primary)
			}

			textView.layoutParams = numberParams
			mapLayout!!.addView(textView)
		}

	}


	override fun onDestroy() {
		super.onDestroy()
		snackLayout!!.setOnClickListener(null)
		for (i in 0 until mapLayout!!.childCount) {
			val v = mapLayout!!.getChildAt(i)
			if (v is ImageView) {
				v.setOnClickListener(null)
			}

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
//			snackbar!!.show()
		} else {
//			snackbar!!.dismiss()
		}
	}

	private fun animateView(seatView: ImageView) {
		val animation1 = AlphaAnimation(0.5f, 1.0f)
		animation1.duration = 100
		animation1.startOffset = 300
		animation1.fillAfter = true
		seatView.startAnimation(animation1)
	}

	private fun setNumber(seat: SeatMap, numberTextView: TextView) {
		numberParams = RelativeLayout.LayoutParams(RLW, RLW)
		val seatNumber = Utils.parseNumber(seat.name)
		numberTextView.text = seatNumber


		val angle = radianToDegrees(seat)
		val angleInt = Math.round(angle)
		val angleAbs = Math.abs(angleInt)

		var offsetX: Int

		if (angleAbs != 90) { // horizontal seats
			offsetX = 0
		} else { // vertical seats
			if (seatNumber.length < 2) {
				offsetX = 5
			} else {
				offsetX = 4
			}
		}

		numberParams?.leftMargin = ((seat.numberDeltaX?.plus(offsetX))?.times(
				mXScaleFactor!!))?.toInt()
		numberParams?.topMargin = ((seat.numberDeltaY?.plus(0))?.times(
				mYScaleFactor!!))?.toInt()

		numberTextView.layoutParams = numberParams
		mapLayout?.addView(numberTextView)

	}


	private fun setSeat(seatBitmap: Bitmap, seat: SeatMap, ivSeat: ImageView) {
		seatParams = RelativeLayout.LayoutParams(RLW, RLW)
		seatParams!!.leftMargin = ((seat.dx.minus(0)) * mXScaleFactor!!).toInt()
		seatParams!!.topMargin = ((seat.dy.plus(0)) * mYScaleFactor!!).toInt()

		val angle = radianToDegrees(seat)
//		Timber.tag("@@@").d("name: ${seat.name} -  angle: $angle")

		val pivotX = seatBitmap.width / 2f
		val pivotY = seatBitmap.height / 2f
		ivSeat.pivotX = pivotX
		ivSeat.pivotY = pivotY
		ivSeat.rotation = angle
	}

	private fun radianToDegrees(seat: SeatMap): Float {
		return Math.toDegrees(seat.angle).toFloat()
	}


	private fun setImage(seatImg: ImageView, seat: SeatMap) {
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


	private inner class BookingBtnListener : View.OnClickListener {

		override fun onClick(view: View) {
			val timeFrom: String = TimeDataModel.start
			val timeTo: String = TimeDataModel.end
			if (Utils.valideateDate(timeFrom, timeTo)) {
//				presenter!!.onBookingClick()
			} else {
				toast(activity!!.resources.getString(R.string.toast_wrong_range))
			}
		}
	}


	fun onGetPaymentResponseSuccess(payResponse: PaymentResponse) {
		Timber.tag("common").d("Pay successfully: %s", payResponse)

		val url = payResponse.redirectUrl

		// TODO Fix it!!!
//		Intent intent = new Intent(this.getContext(), YandexWebView.class);
//		intent.putExtra("url", url);

/*		var intent = Intent()
		intent.putExtra("payments", true)
		startActivity(Intent(activity, MainActivity::class.java))

		intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
		startActivity(intent)*/
	}

	/**
	 * Something goes wrong, and we can't bye seat from Winstrike PC club. show user toast with description this fucking situation.
	 */
	fun onGetPaymentFailure(appErrorMessage: String) {
		val timeFrom = TimeDataModel.start
		val timeTo = TimeDataModel.end
		Timber.d("timeFrom: %s", timeFrom)
		Timber.d("timeTo: %s", timeTo)

		// TODO Replace with "when" construct
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


	private inner class SeatViewOnClickListener(private val seatNumber: TextView, private val seat: SeatMap,
	                                            private val ivSeat: ImageView, private val seatBitmap: Bitmap,
	                                            private val mPickedSeatsIds: LinkedHashMap<*, *>) : View.OnClickListener {

		override fun onClick(v: View) {
			if (seat.type === SeatType.FREE || seat.type === SeatType.VIP) {
				if (!mPickedSeatsIds.containsKey(Integer.parseInt(seat.id))) {
					ivSeat.setBackgroundResource(R.drawable.ic_seat_picked)
					seat.pid?.let { onSelectSeat(seat.id, false, it) }
					Timber.d("Seat id: %s,type: %s, name: %s, pid: %s", seat.id, seat.type, seat.name, seat.pid)
					seatNumber.setTextColor(Color.WHITE)
					seatNumber.setTypeface(null, Typeface.BOLD)
				} else {
					mapLayout!!.removeView(ivSeat)
					setImage(ivSeat, seat)
					setSeat(seatBitmap, seat, ivSeat)
					mapLayout!!.addView(ivSeat)
					seat.pid?.let { onSelectSeat(seat.id, true, it) }
					seatNumber.setTextColor(ContextCompat.getColor(activity!!, R.color.label_gray))
				}
			} else {
				Timber.d("Seat id: %s,type: %s, name: %s, pid: %s", seat.id, seat.type, seat.name, seat.pid)
				animateView(ivSeat)
				seatNumber.setTypeface(null, Typeface.NORMAL)
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

	private fun initSnackBar() {
/*		snackbar = Snackbar.make(mapLayout!!, "", Snackbar.LENGTH_INDEFINITE)
		snackbar!!.view.setBackgroundColor(Color.TRANSPARENT)
		snackbar!!.view.setBackgroundResource(R.drawable.btn_bukking)
		val layoutInflater = this.layoutInflater
		val snackView = layoutInflater.inflate(R.layout.my_snackbar, null)
		snackLayout = snackbar!!.view as Snackbar.SnackbarLayout
		snackLayout!!.addView(snackView)
		snackLayout!!.setOnClickListener(BookingBtnListener())
		snackbar!!.dismiss()*/
	}

	fun onSnackBarShow() {
//		snackbar!!.show()
	}

	fun onSnackBarHide() {
//		snackbar!!.dismiss()
	}

	override fun onStop() {
		super.onStop()
//		snackbar!!.dismiss()
	}


	override fun onStart() {
		super.onStart()
//		snackbar!!.dismiss()
	}


}
