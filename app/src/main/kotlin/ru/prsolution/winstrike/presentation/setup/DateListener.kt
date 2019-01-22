package ru.prsolution.winstrike.presentation.setup

import android.text.TextUtils


import java.util.Calendar

import ru.prsolution.winstrike.domain.models.TimeDataModel
import timber.log.Timber

// TODO: Replace with another component
/*internal class DateListener : OnSelectDateListener {

	override fun onSelect(calendar: List<Calendar>) {
		TimeDataModel.setSelectDate(calendar[0].time)
		val date = TimeDataModel.selectDate
		TimeDataModel.date.set(date)
		//Update time for selected date:
		if (!TextUtils.isEmpty(TimeDataModel.start)) {
			var timeFrom = TimeDataModel.timeFrom
			var timeTo = TimeDataModel.timeTo
			TimeDataModel.setStartAt(timeFrom)
			TimeDataModel.setEndAt(timeTo)
			timeFrom = TimeDataModel.start
			timeTo = TimeDataModel.end
			Timber.d("New date is selected: timeFrom: %s, timeTo: %s", timeFrom, timeTo)
		}
	}
}*/

