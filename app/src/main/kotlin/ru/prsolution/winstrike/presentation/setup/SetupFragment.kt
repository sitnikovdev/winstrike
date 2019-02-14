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
import android.view.*
import android.view.Window.FEATURE_NO_TITLE
import android.widget.DatePicker
import android.widget.TextView
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.facebook.drawee.view.SimpleDraweeView
import kotlinx.android.synthetic.main.frm_setup.next_button
import kotlinx.android.synthetic.main.frm_setup.progressBar
import kotlinx.android.synthetic.main.frm_setup.tv_date
import kotlinx.android.synthetic.main.frm_setup.tv_time
import kotlinx.android.synthetic.main.frm_setup.v_date_tap
import kotlinx.android.synthetic.main.frm_setup.v_time_tap
import org.jetbrains.anko.support.v4.toast
import ru.prsolution.winstrike.R
import ru.prsolution.winstrike.domain.models.ArenaSchema
import ru.prsolution.winstrike.domain.models.SeatCarousel
import ru.prsolution.winstrike.viewmodel.MainViewModel
import ru.prsolution.winstrike.presentation.utils.date.TimeDataModel
import ru.prsolution.winstrike.data.repository.resouces.ResourceState
import timber.log.Timber
import java.time.Month
import java.time.format.TextStyle
import java.util.Locale

class SetupFragment : Fragment(),
        DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener {

    private var mArenaPid: String? = null
    private var mSeatName: TextView? = null
    private var mCpuDescription: TextView? = null
    private var mSeatImage: SimpleDraweeView? = null
    private var mArena: ArenaSchema? = null

    /**
     * route show map to main presenter in MainScreenActivity
     */
    var mVm: MainViewModel? = null


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (isAdded) {
            Timber.d("fragment is added")
        } else {
            Timber.d("fragment is not added")
        }
        if (context != null) {
            Timber.d("on Attach context not null")
        } else {
            Timber.d("on Attach context is null")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mVm = activity?.let { ViewModelProviders.of(it)[MainViewModel::class.java] }

        // Get list of arenas
        if (savedInstanceState == null) {
//            mVm?.getArenaList()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(ru.prsolution.winstrike.R.layout.frm_setup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mSeatName = view.findViewById(R.id.seat_name_tv)
        mCpuDescription = view.findViewById(R.id.cpu)
        mSeatImage = view.findViewById(R.id.head_image)

        progressBar.visibility = View.INVISIBLE

        arguments?.let {
            val safeArgs = SetupFragmentArgs.fromBundle(it)
            this.mArenaPid = safeArgs.arenaPid
            updateSeatInfo(safeArgs.seat)
        }


        // mArenaPid
        mVm?.arena?.observe(this@SetupFragment, Observer { arena ->

            // loading
            arena.state.let { state ->
                if (state == ResourceState.LOADING) {
//                        progressBar.visibility = View.VISIBLE
//                        Timber.tag("$$$").d("status is: $it")
                }
            }

            // data
            arena.data?.let {
                //                    progressBar.visibility = View.INVISIBLE
                this.mArena = arena.data
                mArena?.let {
                    require(isAdded) { "+++ SetupFragment not attached to an activity. +++" }
                    val action = SetupFragmentDirections.nextAction(mArena)
                    Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(action)
                }
            }

            // error
            arena.message?.let {
                //                    progressBar.visibility = View.INVISIBLE
                Timber.tag("$$$").d("error message: $it")
            }
        })


        initListeners()
    }

    // Show map fragment after user select date and time in SetupFragment
    private fun onMapShow() {
        mVm?.getArenaList()
/*        mArena?.let {
            val action = SetupFragmentDirections.nextAction()
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(action)
        }*/
    }

    private fun getArenaByTime(activePid: String?) {
        requireNotNull(activePid) { "+++ Rooms must be initialized +++" }

        if (TextUtils.isEmpty(TimeDataModel.start) || TextUtils.isEmpty(TimeDataModel.end)) {
            toast("Не указана дата")
            return
        }
        val time = mutableMapOf<String, String>()
        time["start_at"] = TimeDataModel.start
        time["end_at"] = TimeDataModel.end
// 		time["start_at"] = "2019-01-26T20:00:00" //		time["end_at"] = "2019-01-26T21:00:00"
        mVm?.getArenaSchema(activePid, time)
    }

    @SuppressLint("NewApi")
    override fun onDateSet(datePicker: DatePicker?, year: Int, month: Int, day: Int) {

        val monthLoc = Month.of(month + 1).getDisplayName(TextStyle.FULL, Locale("RU"))
        val selectedDate = "$day $monthLoc $year"

        TimeDataModel.setDateFromCalendar(selectedDate)
        val date = TimeDataModel.selectDate

        // update view model
        tv_date.text = date
    }

    @SuppressLint("NewApi")
    override fun onTimeSet(timePicker: TimePicker?, hourOfDay: Int, minute: Int) {

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

        tv_time.text = time
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
            getArenaByTime(mArenaPid)
            activity?.supportFragmentManager?.executePendingTransactions()
            onMapShow()
        }
    }

    private fun updateSeatInfo(seat: SeatCarousel?) {
        mSeatName?.text = seat?.title
        mSeatImage?.setImageURI(Uri.parse(seat?.imageUrl))
        mCpuDescription?.text = seat?.description.let { it?.replace(oldValue = "\\", newValue = "") }
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

        return DatePickerDialog(context, R.style.DateDialog, listener, year,
                month, day)
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

        return TimePickerDialog(
            activity, AlertDialog.THEME_HOLO_DARK, listener, hour, minute,
            DateFormat.is24HourFormat(activity)
/*        return TimePickerDialog(
                activity, R.style.TimeDialog, listener, hour, minute,
                DateFormat.is24HourFormat(activity)*/
        )
    }
}
