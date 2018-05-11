package ru.prsolution.winstrike.mvp.transform

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