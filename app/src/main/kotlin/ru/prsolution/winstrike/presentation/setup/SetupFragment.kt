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
import android.text.TextUtils
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.frm_choose.cpu
import kotlinx.android.synthetic.main.frm_choose.head_image
import kotlinx.android.synthetic.main.frm_choose.next_button
import kotlinx.android.synthetic.main.frm_choose.progressBar
import kotlinx.android.synthetic.main.frm_choose.seat_title
import kotlinx.android.synthetic.main.frm_choose.tv_date
import kotlinx.android.synthetic.main.frm_choose.tv_time
import kotlinx.android.synthetic.main.frm_choose.v_date_tap
import kotlinx.android.synthetic.main.frm_choose.v_time_tap
import org.jetbrains.anko.support.v4.toast
import ru.prsolution.winstrike.R
import ru.prsolution.winstrike.domain.models.Arena
import ru.prsolution.winstrike.domain.models.SeatCarousel
import ru.prsolution.winstrike.presentation.main.MainViewModel
import ru.prsolution.winstrike.presentation.utils.pref.PrefUtils.selectedArena
import timber.log.Timber
import ru.prsolution.winstrike.presentation.utils.date.TimeDataModel
import ru.prsolution.winstrike.presentation.utils.date.TimeDataModel.time
import ru.prsolution.winstrike.presentation.utils.date.TimeDataModel.timeFrom
import ru.prsolution.winstrike.presentation.utils.date.TimeDataModel.timeTo
import ru.prsolution.winstrike.presentation.utils.resouces.ResourceState
import java.time.Month
import java.time.format.TextStyle
import java.util.Locale

class SetupFragment : Fragment(),
                      DatePickerDialog.OnDateSetListener,
                      TimePickerDialog.OnTimeSetListener {

    private var rooms: List<Arena>? = null

    /**
	 * route show map to main presenter in MainScreenActivity
	 */
    interface MapShowListener {
        fun onMapShow()
    }

    var mListener: MapShowListener? = null
    var mVm: MainViewModel? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        require(context is MapShowListener) { "++++ Must implements SetupFragment.MapShowListener. +++" }
        mListener = context
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mVm = activity?.let { ViewModelProviders.of(it)[MainViewModel::class.java] }

        if (savedInstanceState == null) {
            mVm?.getArenaList()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(ru.prsolution.winstrike.R.layout.frm_choose, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressBar.visibility = View.INVISIBLE

        activity?.let {
            // arenaList
            mVm?.arenaList?.observe(it, Observer { response ->
                this.rooms = response.data
            })
            // seat
            mVm?.currentSeat?.observe(it, Observer { seat ->
                updateSeatInfo(seat)
            })
            // date
            mVm?.currentDate?.observe(it, Observer { date ->
                tv_date.text = date
            })
            // time
            mVm?.currentTime?.observe(it, Observer { time ->
                tv_time.text = time
            })
            mVm?.arena?.observe(it, Observer { arena ->
                arena.state.let { state ->
                    if (state == ResourceState.LOADING) {
                        progressBar.visibility = View.VISIBLE
                        Timber.tag("$$$").d("status is: $it")
                    }
                }

                arena.data?.let {
                    progressBar.visibility = View.INVISIBLE
                    Timber.tag("$$$").d("arena name: ${arena.data.name}")
                    mListener?.onMapShow()
                }

                arena.message?.let {
                    progressBar.visibility = View.INVISIBLE
                    Timber.tag("$$$").d("error message: $it")
                }
            })
        }

        initListeners()
    }

    private fun getArenaByTime() {
        requireNotNull(rooms) { "+++ Rooms must be initialized +++" }
        if (TextUtils.isEmpty(TimeDataModel.start) || TextUtils.isEmpty(TimeDataModel.end)) {
            toast("Не указана дата")
            return
        }
        // get active arena pid
        val activePid = rooms?.get(selectedArena)?.roomLayoutPid
        val time = mutableMapOf<String, String>()
        time["start_at"] = TimeDataModel.start
        time["end_at"] = TimeDataModel.end
// 		time["start_at"] = "2019-01-26T20:00:00" //		time["end_at"] = "2019-01-26T21:00:00"
        mVm?.getArenaSchema(activePid, time)
    }

    @SuppressLint("NewApi")
    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, day: Int) {

        val monthLoc = Month.of(month + 1).getDisplayName(TextStyle.FULL, Locale("RU"))
        val selectedDate = "$day $monthLoc $year"

        TimeDataModel.setDateFromCalendar(selectedDate)
        val date = TimeDataModel.selectDate

        // update view model
        mVm?.currentDate?.value = date
    }

    @SuppressLint("NewApi")
    override fun onTimeSet(p0: TimePicker?, hourOfDay: Int, minute: Int) {

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)
        val sdf = android.text.format.DateFormat.getTimeFormat(activity)
        val timeFrom = sdf.format(calendar.time)
        calendar.add(Calendar.HOUR_OF_DAY, 1)
        val timeTo = sdf.format(calendar.time)
        val time = "$timeFrom - $timeTo"

        TimeDataModel.timeFrom = timeFrom
        TimeDataModel.timeTo = timeTo

        TimeDataModel.setStartAt(timeFrom)
        TimeDataModel.setEndAt(timeTo)

        mVm?.currentTime?.value = time
    }

    private fun initListeners() {
        // date
        v_date_tap.setOnClickListener {
            showDatePickerDialog(it)
        }
        tv_date.setOnClickListener {
            showDatePickerDialog(it)
        }

        // time
        v_time_tap.setOnClickListener {
            showTimePickerDialog(it)
        }
        tv_time.setOnClickListener {
            showTimePickerDialog(it)
        }

        // next button
        next_button.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            getArenaByTime()
        }
    }

    private fun updateSeatInfo(seat: SeatCarousel?) {
        seat_title.text = seat?.title
        head_image.setImageURI(Uri.parse(seat?.imageUrl))
        cpu.text = seat?.description.let { it?.replace(oldValue = "\\", newValue = "") }
    }

    private fun showDatePickerDialog(v: View) {
        val newFragment = DatePickerFragment(this)
        activity?.supportFragmentManager?.let { newFragment.show(it, "datePicker") }
    }

    private fun showTimePickerDialog(v: View) {
        if (TextUtils.isEmpty(TimeDataModel.date)) {
            toast("Сначала выберите дату")
            return
        }
        val timePicker = TimePickerFragment(this)
        activity?.supportFragmentManager?.let { timePicker.show(it, "timePicker") }
    }
}

class DatePickerFragment(
    private val listener: DatePickerDialog.OnDateSetListener
) : DialogFragment() {

    init {
        requireNotNull(listener) { "+++++ Must implement DatePickerDialog.OnDateSetListener. +++++" }
    }

    @SuppressLint("NewApi")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

// 		return DatePickerDialog(activity, android.R.style.Theme_DeviceDefault_Dialog_Alert, listener, year,
// 		                        month, day)
        val dialog = DatePickerDialog(activity, R.style.DatePickerDialogTheme, listener, year,
                                      month, day).apply { requestWindowFeature(Window.FEATURE_NO_TITLE) }
        return dialog
    }
}

class TimePickerFragment(private val listener: TimePickerDialog.OnTimeSetListener) : DialogFragment() {

    init {
        requireNotNull(listener) { "+++++ Must implement TimePickerDialog.OnTimeSetListener. +++++" }
    }

    @SuppressLint("NewApi", "InlinedApi")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        return TimePickerDialog(activity, AlertDialog.THEME_HOLO_DARK, listener, hour, minute,
                                DateFormat.is24HourFormat(activity))
    }
}
