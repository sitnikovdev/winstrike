package ru.prsolution.winstrike.mvp.models

import ru.prsolution.winstrike.mvp.transform.DateTransform
import timber.log.Timber
import java.util.*
import kotlin.collections.LinkedHashMap
import kotlin.properties.ObservableProperty
import kotlin.reflect.KProperty

object TimeDataModel {

    var pid: String = ""
    var pids: LinkedHashMap<Int,String> = LinkedHashMap()
    //var date: String  by observing("", didSet = { valideateDate() })
    var date: String = ""
    var selectDate: String = ""
    var start: String = ""
    var end: String = ""

    fun setStartAt(startAt: String) {
        start = DateTransform.getFormattedDateWithTime(startAt)
        startDate = DateTransform.getDateInUTC(start)
    }

    fun setEndAt(endAt: String) {
        end = DateTransform.getFormattedDateWithTime(endAt)
        endDate = DateTransform.getDateInUTC(end)
    }

    fun setSelectDate(datetime: Date) {
        selectDate = DateTransform.getSimpleDateFromCalendar(datetime)
        date = selectDate
    }

    fun setShortFormatDate(datetime: String) {
        date = datetime;
    }


    var startDate: Date by observing(Date(), didSet = {
        valideateDate()
    })

    var endDate: Date by observing(Date(), didSet = {
        valideateDate()
    })


    fun valideateDate(): Boolean {
        if (startDate != null && endDate != null) {
            val current = Date()
            isDateValid = startDate < endDate && startDate >= current
            return true
        } else
            return false
    }

    var isDateValid: Boolean by observing(false, {
        Timber.d("dates: valid ${isDateValid}, startAt: ${start}, endAt: ${end}")
    })

    fun clear() {
        pids = LinkedHashMap()
        date = ""
        start = ""
        end = ""
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

