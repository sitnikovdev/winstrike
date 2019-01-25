package ru.prsolution.winstrike.presentation.setup

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.icu.util.Calendar
import android.net.Uri
import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.ac_mainscreen.toolbar
import kotlinx.android.synthetic.main.frm_choose.cpu
import kotlinx.android.synthetic.main.frm_choose.head_image
import kotlinx.android.synthetic.main.frm_choose.seat_title
import kotlinx.android.synthetic.main.frm_choose.tv_date
import kotlinx.android.synthetic.main.frm_choose.tv_time
import kotlinx.android.synthetic.main.frm_choose.v_date_tap
import kotlinx.android.synthetic.main.frm_choose.v_time_tap
import ru.prsolution.winstrike.domain.models.SeatModel
import ru.prsolution.winstrike.presentation.main.MainActivity
import ru.prsolution.winstrike.presentation.main.MainViewModel
import timber.log.Timber
import java.text.DateFormatSymbols


class SetupFragment : Fragment(),
                      DatePickerDialog.OnDateSetListener,
                      TimePickerDialog.OnTimeSetListener {

	override fun onDateSet(p0: DatePicker?, year: Int, month: Int, day: Int) {
		mVm?.currentDate?.value = "$day ${DateFormatSymbols().months[month]} $year"
	}

	override fun onTimeSet(p0: TimePicker?, hourOfDay: Int, minute: Int) {
		mVm?.currentTime?.value = "$hourOfDay:$minute - ${hourOfDay + 1}:$minute"
	}

	var mVm: MainViewModel? = null
	private var mListener: MapShowListener? = null


	/**
	 * route show map to main presenter in MainScreenActivity
	 */
	interface MapShowListener {
		fun onMapShow()
	}

	override fun onAttach(context: Context?) {
		super.onAttach(context)
		require(context is MapShowListener)
		{ "++++ Must implements onMapShowListener. +++" }
		mListener = context
	}


	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		mVm = activity?.let { ViewModelProviders.of(it)[MainViewModel::class.java] }
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		activity?.toolbar?.setNavigationOnClickListener {
			(activity as FragmentActivity).supportFragmentManager.popBackStack()
			activity?.toolbar?.navigationIcon = null
			activity?.toolbar?.setContentInsetsAbsolute(0, (activity as FragmentActivity).toolbar.contentInsetStart)
			(activity as MainActivity).setActive()
		}

		return inflater.inflate(ru.prsolution.winstrike.R.layout.frm_choose, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		activity?.let {
			mVm?.currentSeat?.observe(it, Observer { seat ->
				updateUI(seat)
			})
			mVm?.currentDate?.observe(it, Observer { date ->
				updateDate(date)
			})
			mVm?.currentTime?.observe(it, Observer { time ->
				updateTime(time)
			})
		}

		setupListeners()
	}

	private fun updateTime(time: String?) {
		tv_time.text = time
	}

	private fun updateDate(date: String?) {
		tv_date.text = date
	}

	private fun setupListeners() {
		v_date_tap.setOnClickListener {
			showDatePickerDialog(it)
		}
		tv_date.setOnClickListener {
			showDatePickerDialog(it)
		}

		v_time_tap.setOnClickListener {
			showTimePickerDialog(it)
		}
		tv_time.setOnClickListener {
			showTimePickerDialog(it)
		}
	}

	private fun updateUI(seat: SeatModel?) {
		seat_title.text = seat?.title
		head_image.setImageURI(Uri.parse(seat?.imageUrl))
		cpu.text = seat?.description.let { it?.replace("\\", "") }
	}

	val datePickerDialoListener = DatePickerDialog.OnDateSetListener() { datePiker, year, month, day ->

		Timber.d("This date: $year - $month - $day")

	}


	fun showDatePickerDialog(v: View) {
		val newFragment = DatePickeFragment(this)
		newFragment.show(activity?.supportFragmentManager, "datePicker")
	}

	fun showTimePickerDialog(v: View) {
		val timePicker = TimePickerFragment(this)
		timePicker.show(activity?.supportFragmentManager, "timePicker")
	}


}

class DatePickeFragment(
		listener: DatePickerDialog.OnDateSetListener) : DialogFragment() {
	val listener = listener

	init {
		requireNotNull(listener) { "+++++ Must implement DatePickerDialog.OnDateSetListener. +++++" }
	}


	@SuppressLint("NewApi")
	override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
		val c = Calendar.getInstance()
		val year = c.get(Calendar.YEAR)
		val month = c.get(Calendar.MONTH)
		val day = c.get(Calendar.DAY_OF_MONTH)

		return DatePickerDialog(activity, AlertDialog.THEME_DEVICE_DEFAULT_DARK, listener, year,
		                        month, day)
	}

}

class TimePickerFragment(listener: TimePickerDialog.OnTimeSetListener) : DialogFragment() {
	val listener = listener

	init {
		requireNotNull(listener) { "+++++ Must implement TimePickerDialog.OnTimeSetListener. +++++" }
	}

	@SuppressLint("NewApi", "InlinedApi")
	override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
		// Use the current time as the default values for the picker
		val c = Calendar.getInstance()
		val hour = c.get(Calendar.HOUR_OF_DAY)
		val minute = c.get(Calendar.MINUTE)

		// Create a new instance of TimePickerDialog and return it
		return TimePickerDialog(activity, AlertDialog.THEME_HOLO_DARK, listener, hour, minute,
		                        DateFormat.is24HourFormat(activity))

	}


/*	override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
		// Do something with the time chosen by the user
		Timber.d("On time picker selected")
	}*/
}



