package ru.prsolution.winstrike.presentation.setup

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import kotlinx.android.synthetic.main.ac_mainscreen.toolbar
import ru.prsolution.winstrike.common.utils.Utils.valideateDate
import ru.prsolution.winstrike.datasource.model.Arenas
import ru.prsolution.winstrike.datasource.model.RoomLayoutFactory
import ru.prsolution.winstrike.domain.models.TimeDataModel
import ru.prsolution.winstrike.presentation.main.HomeFragment
import ru.prsolution.winstrike.presentation.main.MainScreenActivity
import timber.log.Timber
import java.util.HashMap


class SetupFragment : Fragment() {

	var mProgressDialog: ProgressDialog? = null
	private var mListener: onMapShowListener? = null

	//	private var dataPicker: com.ositnikov.datepicker.DataPicker? = null
//	private var dateListener: DateListener? = null
	private var timePickerDialog: TimePickerDialog? = null
	private var selectedArena = 0

	var presenter: ChooseScreenPresenter? = null


	/**
	 * route show map to main presenter in MainScreenActivity
	 */
	interface onMapShowListener {

		fun onMapShow()
	}

	override fun onAttach(context: Context?) {
		super.onAttach(context)
		if (context is onMapShowListener) {
			mListener = context
		} else {
			throw ClassCastException(context!!.toString() + " must implements onMapShowListener. ")
		}
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
/*		this.selectedArena = arguments!!.getInt(
				ACTIVE_ARENA)
		this.presenter = ChooseScreenPresenter(service, this)*/
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//		activity?.toolbar?.navigationIcon  = activity?.getDrawable(ru.prsolution.winstrike.R.drawable.ic_back_arrow)
//		val seat = WinstrikeApp.instance.seat

//		toolbar.setNavigationOnClickListener(View.OnClickListener { activity!!.onBackPressed() })
		activity?.toolbar?.setNavigationOnClickListener{
			(activity as FragmentActivity).supportFragmentManager.popBackStack()
			activity?.toolbar?.navigationIcon  = null
			activity?.toolbar?.setContentInsetsAbsolute(0, (activity as FragmentActivity).toolbar.contentInsetStart)
			(activity as MainScreenActivity).setActive()
		}


		return inflater.inflate(ru.prsolution.winstrike.R.layout.frm_choose, container, false)
	}


	override fun onResume() {
		super.onResume()
/*		if (this.presenter == null) {
			this.presenter = ChooseScreenPresenter(service, this)*/
	}

/*		if (this.dataPicker == null) {
			dateListener = DateListener()
			dataPicker = com.ositnikov.datepicker.DataPicker(activity, dateListener)
		}*/

}

fun onDateClickListener() {
	TimeDataModel.setIsDateSelect(true)
//		dataPicker!!.build().show()
}

fun onTimeClickListener() {
	if (TimeDataModel.getIsDateSelect()) {
//			timePickerDialog = TimePickerDialog(activity)
	} else {
//			toast(activity, getString(R.string.first_select_date))
	}
}


fun onNextButtonClickListener() {
	if (valideateDate(TimeDataModel.start, TimeDataModel.end)) {
//			presenter!!.getActiveArena(this.selectedArena)
	} else {
//			toast(activity, getString(R.string.toast_wrong_range))
	}
}

fun onGetArenasResponseSuccess(authResponse: Arenas, selectedArena: Int) {
	Timber.d("Success get map data from server: %s", authResponse)
	/**
	 * data for active room pid successfully get from server.
	 * save pid and get map for selected time period
	 */
	val rooms = authResponse.rooms

	// TODO: 17/10/2018 Add parameter for arena select:
	val activePid = rooms[selectedArena].roomLayoutPid

	val time = HashMap<String, String>()
	time["start_at"] = TimeDataModel.start
	time["end_at"] = TimeDataModel.end
//		presenter!!.getArenaByTimeRange(activePid, time)
}

fun onGetArenaByTimeResponseSuccess(roomLayoutFactory: RoomLayoutFactory) {
	Timber.d("Success get layout data from server: %s", roomLayoutFactory)
	/**
	 * data for seat mapping successfully get from sever.
	 * save map data in singleton and call MapScreenFragment from main presenter
	 */
//		WinstrikeApp.instance.roomLayout = roomLayoutFactory.roomLayout
//		if (WinstrikeApp.instance.roomLayout != null) {
//			mListener!!.onMapShow()
//		}
}

/**
 * Something go wrong with map request, show user message in toast
 */
fun onGetAcitivePidFailure(appErrorMessage: String) {
	Timber.d("Failure get map from server: %s", appErrorMessage)
	if (appErrorMessage.contains("502")) {
//			toast(activity, getString(R.string.server_error_502))
	} else {
//			toast(activity, appErrorMessage)
	}
}

/**
 * Something go wrong with map request
 */
fun onGetArenaByTimeFailure(appErrorMessage: String) {
	Timber.d("Failure get layout from server: %s", appErrorMessage)
	if (appErrorMessage.contains("416")) {
//			toast(activity, getString(R.string.not_working_range))
	}
}


/**
 * show progress on seats loading
 */
fun showProgressDialog() {
/*		if (mProgressDialog == null) {
			mProgressDialog = ProgressDialog(this.activity)
			mProgressDialog!!.setMessage(getString(R.string.loading_seats))
			mProgressDialog!!.isIndeterminate = true
		}*/

//		mProgressDialog!!.show()
}

private fun hideProgressDialog() {
/*		if (mProgressDialog != null && mProgressDialog!!.isShowing) {
			mProgressDialog!!.dismiss()
		}*/
}

fun showWait() {
	showProgressDialog()
}

fun removeWait() {
	hideProgressDialog()
}


/*	companion object {

		private val EXTRA_NAME = "extra_name"
		private val ACTIVE_ARENA = "extra_number"


		fun getNewInstance(name: String, selectedArena: Int): SetupFragment {
			val fragment = SetupFragment()
			val arguments = Bundle()
			arguments.putString(EXTRA_NAME, name)
			arguments.putInt(ACTIVE_ARENA, selectedArena)
			fragment.arguments = arguments
			return fragment
		}
	}*/
