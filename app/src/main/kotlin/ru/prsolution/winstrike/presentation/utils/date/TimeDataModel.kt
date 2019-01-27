package ru.prsolution.winstrike.presentation.utils.date

import timber.log.Timber
import java.util.*
import kotlin.collections.LinkedHashMap
import kotlin.properties.ObservableProperty
import kotlin.reflect.KProperty

object TimeDataModel {

	var pids: LinkedHashMap<Int, String> = LinkedHashMap()
	var date: String = "" // by observing("", willSet = { validateDate() })
	var time: String = "Укажите диапазон времени"
	var selectDate: String = ""
	var start: String = ""
	var end: String = ""
	var timeFrom: String = ""
	var timeTo: String = ""
	var isDateSelect = false

	fun setIsDateSelect(isSelect: Boolean) {
		isDateSelect = isSelect
	}

	fun getIsDateSelect(): Boolean {
		return isDateSelect
	}


	fun setStartAt(startAt: String) {
		start = DateTransform.getFormattedDateWithTime(startAt)
		startDate = DateTransform.getDateInUTC(
				start)
	}

	fun setEndAt(endAt: String) {
		end = DateTransform.getFormattedDateWithTime(endAt)
		endDate = DateTransform.getDateInUTC(
				end)
	}

	fun setSelectDate(datetime: Date) {
		selectDate = DateTransform.getSimpleDateFromCalendar(datetime)
        date  = selectDate
	}


	var startDate: Date by observing(Date()
	                                                                                               , didSet = {
		validateDate()
	})

	var endDate: Date by observing(Date(), didSet = {
		validateDate()
	})

	fun validateDate(stDate: String, edDate: String): Boolean {
		if (stDate.isEmpty() || edDate.isEmpty()) {
			return false
		}
		val current = Date()
		val startDate = DateTransform.getDateInUTC(stDate)
		val endDate = DateTransform.getDateInUTC(edDate)
		return startDate.before(endDate) && (startDate.after(current) || startDate.equals(current))
	}


	private fun validateDate(): Boolean {
		val current = Date()
		isDateValid = startDate < endDate && startDate >= current
		return isDateValid
	}

	var isDateValid: Boolean by observing(false, {
		Timber.d(
				"dates: valid $isDateValid, startAt: $start, endAt: $end")
	})

	fun clearPids() {
		pids = LinkedHashMap()
	}

	fun clearDateTime() {
		isDateSelect = false
//        date.set("Выберите дату")
//        time.set("Укажите диапазон времени")
		start = ""
		end = ""
		startDate = Date()
		endDate = Date()
		selectDate = ""
	}


	fun <T> observing(initialValue: T,
	                  willSet: () -> Unit = { },
	                  didSet: () -> Unit = { }
	) = object : ObservableProperty<T>(initialValue) {
		override fun beforeChange(property: KProperty<*>, oldValue: T, newValue: T): Boolean =
				true.apply { willSet() }

		override fun afterChange(property: KProperty<*>, oldValue: T, newValue: T) = didSet()
	}

}

