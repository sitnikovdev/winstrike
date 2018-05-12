package ru.prsolution.winstrike.mvp.transform

import ru.prsolution.winstrike.mvp.models.TimeDataModel.date
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class DateTransform {

    companion object {
        val dateFormatter: DateFormat by lazy {
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ")
        }

        init {
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


        fun transformFromJSON(value: String): Date? {
            return dateFormatter.parse(value).takeIf { value is String }
                    ?: return null

        }

        fun transformFromJSONwithoutMS(value: String): Date? {
            val formater = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ", Locale.getDefault())
            return formater.parse(value).takeIf { value is String }
                    ?: return null
        }

        fun transformToJSON(value: Date):String{
            val date = value?.let { dateFormatter.format(it) }
            return date
        }

        fun transformFromHourSec(value: String?):Date{
            val formater = SimpleDateFormat("HH:mm", Locale.getDefault())
            return formater.parse(value)
        }

        fun transformFromHourSecString(value: Date):String{
            val formater = SimpleDateFormat("HH:mm", Locale.getDefault())
            return formater.format(value)
        }

    }
}