package ru.prsolution.winstrike.mvp.transform

import ru.prsolution.winstrike.mvp.models.TimeDataModel.date
import java.text.SimpleDateFormat
import java.util.*

class DateTransform {

    companion object {

        init {
        }

        fun getSimpleDateFromCalendar(date: Date): String {
            val simpleDateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
            return simpleDateFormat.format(date)
        }


        fun getFormattedDateWithTime(time: String): String {
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


        fun getDateInUTC(time: String): Date {
            var fmtDate = Date()
            val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            try {
                fmtDate = formatter.parse(time)

            } catch (e: java.text.ParseException) {
                e.printStackTrace()
            }

            return fmtDate
        }

    }
}