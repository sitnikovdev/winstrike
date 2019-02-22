package ru.prsolution.winstrike.presentation.setup

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.icu.util.Calendar
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.DatePicker
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.facebook.drawee.view.SimpleDraweeView
import com.ositnikov.datepicker.TimePickerPopWin
import kotlinx.android.synthetic.main.frm_setup.next_button
import kotlinx.android.synthetic.main.frm_setup.progressBar
import kotlinx.android.synthetic.main.frm_setup.tv_date
import kotlinx.android.synthetic.main.frm_setup.tv_time
import kotlinx.android.synthetic.main.frm_setup.v_date_tap
import kotlinx.android.synthetic.main.frm_setup.v_time_tap
import org.jetbrains.anko.support.v4.toast
import org.koin.androidx.viewmodel.ext.viewModel
import ru.prsolution.winstrike.R
import ru.prsolution.winstrike.domain.models.arena.SeatCarousel
import ru.prsolution.winstrike.presentation.model.arena.SchemaItem
import ru.prsolution.winstrike.presentation.utils.date.TimeDataModel
import ru.prsolution.winstrike.presentation.utils.pref.PrefUtils
import ru.prsolution.winstrike.viewmodel.SetUpViewModel
import timber.log.Timber
import java.time.Month
import java.time.format.TextStyle
import java.util.Locale

class SetupFragment : Fragment(),
    DatePickerDialog.OnDateSetListener {

    private val mVm: SetUpViewModel by viewModel()

    private var mArenaActivePid: String? = null
    private var mSeatName: TextView? = null
    private var mCpuDescription: TextView? = null
    private var mSeatImage: SimpleDraweeView? = null
    private var mArenaSchema: SchemaItem? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(ru.prsolution.winstrike.R.layout.frm_setup, container, false)
    }

    private var pickerDialog: TimePickerPopWin? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mSeatName = view.findViewById(R.id.seat_name_tv)
        mCpuDescription = view.findViewById(R.id.cpu)
        mSeatImage = view.findViewById(R.id.head_image)

        if (!TimeDataModel.date.isEmpty()) {
            tv_date.text = TimeDataModel.date.toString()
        } else {
            tv_date.text = getString(R.string.seatdetail_date)
        }

        if (!TimeDataModel.time.isEmpty()) {
            tv_time.text = TimeDataModel.time.toString()
        } else {
            tv_time.text = getString(R.string.seatdetail_time)
        }

        progressBar.visibility = View.INVISIBLE

        arguments?.let {
            val safeArgs = SetupFragmentArgs.fromBundle(it)
            this.mArenaActivePid = safeArgs.activeLayoutPid

            updateSeatInfo(safeArgs.seat)
        }


        // mArenaPid
        mVm.arenaSchema.observe(this@SetupFragment, Observer { schema ->
            // data
            schema?.let {
                this.mArenaSchema = schema
                mArenaSchema?.let {
                    require(isAdded) { "+++ SetupFragment not attached to an activity. +++" }
                    val action = SetupFragmentDirections.nextAction()
                    action.acitveLayoutPID = mArenaActivePid
                    Navigation.findNavController(requireActivity(), R.id.main_host_fragment).navigate(action)
                }
            }

        })

        initListeners()

        pickerDialog = TimePickerPopWin.Builder(
            requireContext(), (TimePickerPopWin.OnTimePickedListener { hour, min, timeDesc, timeFromData, timeToData ->
                //                textView.text = "$timeFromData - $timeToData"
                Timber.tag("$$$").d("$timeFromData - $timeToData")
                onTimeSet(
                    hourFrom = timeFromData.hour.toInt(),
                    minuteFrom = timeFromData.min.toInt(),
                    hourTo = timeToData.hour.toInt(),
                    minTo = timeToData.min.toInt()
                )
            })
        )
            .textConfirm("Продолжить") //text of confirm button
            .textCancel("CANCEL") //text of cancel button
            .btnTextSize(20) // button text size
            .viewTextSize(25) // pick view text size
            .colorCancel(Color.parseColor("#999999")) //color of cancel button
            .colorConfirm(Color.parseColor("#A9A9A9"))//color of confirm button
            .build()

    }

    private fun getArenaByTime(activePid: String?) {
        require(!TextUtils.isEmpty(activePid)) { "+++ Arena active pid must be set. +++" }

        if (TextUtils.isEmpty(TimeDataModel.start) || TextUtils.isEmpty(TimeDataModel.end)) {
            toast("Не указана дата")
            return
        }

        val time = mutableMapOf<String, String>()
        time["start_at"] = TimeDataModel.start
        time["end_at"] = TimeDataModel.end
// 		time["start_at"] = "2019-01-26T20:00:00" //		time["end_at"] = "2019-01-26T21:00:00"
        mVm.fetchSchema(activePid, time)
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
    // TODO: Provide backport for API < 24.
    fun onTimeSet(hourFrom: Int, minuteFrom: Int, hourTo: Int, minTo: Int) {

        val calendar = Calendar.getInstance()

        // from
        calendar.set(Calendar.HOUR_OF_DAY, hourFrom)
        calendar.set(Calendar.MINUTE, minuteFrom)
        val sdf = android.text.format.DateFormat.getTimeFormat(activity)

        val timeFrom = sdf.format(calendar.time)

        // to
        calendar.add(Calendar.HOUR_OF_DAY, hourTo - hourFrom)
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
            getArenaByTime(mArenaActivePid)
            activity?.supportFragmentManager?.executePendingTransactions()
        }
    }

    private fun updateSeatInfo(seat: SeatCarousel?) {
        PrefUtils.hallName = seat?.title
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
        pickerDialog?.showPopWin(requireActivity())
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

        return DatePickerDialog(
            context, R.style.Theme_Winstrike_DateDialog, listener, year,
            month, day
        )
    }
}

