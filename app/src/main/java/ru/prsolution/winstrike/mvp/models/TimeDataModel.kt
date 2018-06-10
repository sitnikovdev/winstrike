package ru.prsolution.winstrike.mvp.models

import android.databinding.ObservableField
import ru.prsolution.winstrike.mvp.transform.DateTransform
import timber.log.Timber
import java.util.*
import kotlin.collections.LinkedHashMap
import kotlin.properties.ObservableProperty
import kotlin.reflect.KProperty

object TimeDataModel {

    var pid: String = ""
    var pids: LinkedHashMap<Int, String> = LinkedHashMap()
    //var date: String  by observing("", didSet = { valideateDate() })
    var date: String = "Выберите дату"
    var time: String = "Укажите диапазон времени"
    var selectDate: String = ""
    var start: String = ""
    var end: String = ""
    var timeFrom: String = ""
    var timeTo: String = ""
    var isDateSelect = false;

    fun setIsDateSelect(isSelect: Boolean) {
        this.isDateSelect = isSelect;
    }

    fun getIsDateSelect():Boolean {
        return this.isDateSelect
    }


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



    var startDate: Date by observing(Date()
            , didSet = {
        valideateDate()
    })

    var endDate: Date by observing(Date(), didSet = {
        valideateDate()
    })


    fun valideateDate(): Boolean {
        val current = Date()
        isDateValid = startDate < endDate && startDate >= current
        return isDateValid
    }

    var isDateValid: Boolean by observing(false, {
        Timber.d("dates: valid ${isDateValid}, startAt: ${start}, endAt: ${end}")
    })

    fun clearPids() {
        pids = LinkedHashMap()
    }

    fun clearDateTime() {
        isDateSelect = false
        date = "Выберите дату"
        time = "Укажите диапазон времени"
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

