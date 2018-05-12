package ru.prsolution.winstrike.mvp.models

import ru.prsolution.winstrike.mvp.transform.DateTransform
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashSet
import kotlin.properties.ObservableProperty
import kotlin.reflect.KProperty

object TimeDataModel {

    var pid: String = ""
    var pids: Set<String> = HashSet()
    //var date: String  by observing("", didSet = { valideateDate() })
    var date: String = ""
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

    fun setDate(datetime: Date) {
        date = DateTransform.transformToJSON(datetime)
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
        pids = HashSet()
        date = ""
        start = ""
        end = ""
    }

    private fun getFormattedDateShortToUTCString(time: String): String {
        var dateInStr = date
        var fmtDate = Date()
        val date: String

        dateInStr += 'T'.toString() + time + ":00"
        val formatter = SimpleDateFormat("dd MMM yyyy'T'HH:mm:ss")
        try {
            fmtDate = formatter.parse(dateInStr)

        } catch (e: java.text.ParseException) {
            e.printStackTrace()
        }

        date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(fmtDate)
        return date
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

