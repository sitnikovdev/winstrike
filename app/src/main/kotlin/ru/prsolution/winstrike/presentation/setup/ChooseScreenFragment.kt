package ru.prsolution.winstrike.presentation.setup

import ru.prsolution.winstrike.common.utils.Utils.toast
import ru.prsolution.winstrike.common.utils.Utils.valideateDate

import android.app.ProgressDialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import java.util.HashMap
import ru.prsolution.winstrike.R
import ru.prsolution.winstrike.WinstrikeApp
import ru.prsolution.winstrike.datasource.model.Arenas
import ru.prsolution.winstrike.datasource.model.RoomLayoutFactory
import ru.prsolution.winstrike.domain.models.TimeDataModel
import ru.prsolution.winstrike.networking.Service
import timber.log.Timber


class ChooseScreenFragment : Fragment() {

	var mProgressDialog: ProgressDialog? = null
	private var listener: onMapShowProcess? = null

	//	private var dataPicker: com.ositnikov.datepicker.DataPicker? = null
//	private var dateListener: DateListener? = null
	private var timePickerDialog: TimePickerDialog? = null
	private var selectedArena = 0


	var presenter: ChooseScreenPresenter? = null

	//	@Inject
	var service: Service? = null


	/**
	 * route show map to main presenter in MainScreenActivity
	 */
	interface onMapShowProcess {

		fun onMapShow()
	}

	override fun onAttach(context: Context?) {
		super.onAttach(context)
		if (context is onMapShowProcess) {
			listener = context
		} else {
			throw ClassCastException(context!!.toString() + " must implements onMapShowProcess")
		}
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		this.selectedArena = arguments!!.getInt(
				ACTIVE_ARENA)
		this.presenter = ChooseScreenPresenter(service, this)
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val seat = WinstrikeApp.instance.seat
		val view = inflater.inflate(R.layout.frm_choose, container, false)
		return view
	}


	override fun onResume() {
		super.onResume()
		if (this.presenter == null) {
			this.presenter = ChooseScreenPresenter(service, this)
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
			timePickerDialog = TimePickerDialog(activity)
		} else {
			toast(activity, getString(R.string.first_select_date))
		}
	}


	fun onNextButtonClickListener() {
		if (valideateDate(TimeDataModel.start, TimeDataModel.end)) {
			presenter!!.getActiveArena(this.selectedArena)
		} else {
			toast(activity, getString(R.string.toast_wrong_range))
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
		presenter!!.getArenaByTimeRange(activePid, time)
	}

	fun onGetArenaByTimeResponseSuccess(roomLayoutFactory: RoomLayoutFactory) {
		Timber.d("Success get layout data from server: %s", roomLayoutFactory)
		/**
		 * data for seat mapping successfully get from sever.
		 * save map data in singleton and call MapScreenFragment from main presenter
		 */
		WinstrikeApp.instance.roomLayout = roomLayoutFactory.roomLayout
		if (WinstrikeApp.instance.roomLayout != null) {
			listener!!.onMapShow()
		}
	}

	/**
	 * Something go wrong with map request, show user message in toast
	 */
	fun onGetAcitivePidFailure(appErrorMessage: String) {
		Timber.d("Failure get map from server: %s", appErrorMessage)
		if (appErrorMessage.contains("502")) {
			toast(activity, getString(R.string.server_error_502))
		} else {
			toast(activity, appErrorMessage)
		}
	}

	/**
	 * Something go wrong with map request
	 */
	fun onGetArenaByTimeFailure(appErrorMessage: String) {
		Timber.d("Failure get layout from server: %s", appErrorMessage)
		if (appErrorMessage.contains("416")) {
			toast(activity, getString(R.string.not_working_range))
		}
	}


	/**
	 * show progress on seats loading
	 */
	fun showProgressDialog() {
		if (mProgressDialog == null) {
			mProgressDialog = ProgressDialog(this.activity)
			mProgressDialog!!.setMessage(getString(R.string.loading_seats))
			mProgressDialog!!.isIndeterminate = true
		}

		mProgressDialog!!.show()
	}

	private fun hideProgressDialog() {
		if (mProgressDialog != null && mProgressDialog!!.isShowing) {
			mProgressDialog!!.dismiss()
		}
	}

	fun showWait() {
		showProgressDialog()
	}

	fun removeWait() {
		hideProgressDialog()
	}

	override fun onDestroy() {
		super.onDestroy()
		if (this.listener != null) {
			this.listener = null
		}
		if (this.service != null) {
			this.service = null
		}
		if (this.timePickerDialog != null) {
			this.timePickerDialog = null
		}
	}

	override fun onStop() {
		super.onStop()
		if (presenter != null) {
			presenter!!.onStop()
			presenter = null
		}
/*		if (this.dataPicker != null) {
			this.dataPicker = null
		}
		if (this.dateListener != null) {
			this.dateListener = null
		}*/
	}

	companion object {

		private val EXTRA_NAME = "extra_name"
		private val ACTIVE_ARENA = "extra_number"


		fun getNewInstance(name: String, selectedArena: Int): ChooseScreenFragment {
			val fragment = ChooseScreenFragment()
			val arguments = Bundle()
			arguments.putString(EXTRA_NAME, name)
			arguments.putInt(ACTIVE_ARENA, selectedArena)
			fragment.arguments = arguments
			return fragment
		}
	}

}
