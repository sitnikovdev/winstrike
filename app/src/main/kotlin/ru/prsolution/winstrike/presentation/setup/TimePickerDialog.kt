package ru.prsolution.winstrike.presentation.setup

import android.graphics.Color
import androidx.fragment.app.FragmentActivity

import com.ositnikov.datepicker.TimePickerPopWin
import ru.prsolution.winstrike.domain.models.TimeDataModel

/**
 * Show time picker dialog.
 */
class TimePickerDialog(context: FragmentActivity?) {

	init {
		val bntTextSize = 20
		val viewTextSize = 25
		val pickerPopWin = com.ositnikov.datepicker.TimePickerPopWin.Builder(context) { hour, min, timeDesc, timeFromData, timeToData ->

			val time = timeFromData.toString() + " - " + timeToData


			TimeDataModel.timeFrom = timeFromData.toString()
			TimeDataModel.timeTo = timeToData.toString()


			TimeDataModel.setStartAt(timeFromData.toString())
			TimeDataModel.setEndAt(timeToData.toString())

			/**
			 * Save date data from timepicker (start and end).
			 */
			TimeDataModel.time.set(time)


		}.textConfirm("Продолжить") //text of confirm button
				.textCancel("CANCEL") //text of cancel button
				.btnTextSize(bntTextSize) // button text size
				.viewTextSize(viewTextSize) // pick view text size
				.colorCancel(Color.parseColor("#999999")) //color of cancel button
				.colorConfirm(Color.parseColor("#A9A9A9"))//color of confirm button
				.build()


		pickerPopWin.showPopWin(context)
	}

}

