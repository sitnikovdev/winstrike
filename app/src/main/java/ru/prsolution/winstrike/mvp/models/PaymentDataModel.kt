package ru.prsolution.winstrike.mvp.models

import ru.prsolution.winstrike.mvp.transform.DateTransform
import timber.log.Timber
import java.util.*
import kotlin.collections.HashSet
import kotlin.properties.ObservableProperty
import kotlin.reflect.KProperty

object PaymentDataModel {

    var pid: String = ""
    var pids: Set<String> = HashSet()
    var date: String  by observing("",didSet = {isDateValid()})

    var startAt: String  by observing("",didSet = {
          DateTransform.transformFromJSON(startAt)
    })

    var endAt:String  by observing("",didSet = {
        DateTransform.transformFromJSON(endAt)
    })

    fun setDate(datetime: Date){
         date =  DateTransform.transformToJSON(datetime)
    }

    var startDate: Date by observing(Date(),didSet= {
        isDateValid()
    })

    var endDate: Date by observing(Date(),didSet= {
        isDateValid()
    })


    private fun isDateValid():Boolean {
        if (startDate != null && endDate != null){
            val current = Date()
             dateValid = startDate < endDate && startDate >= current
            return true
        } else
           return false
    }

    var dateValid:Boolean by observing(false,{
        Timber.d("dates: valid ${dateValid}, startAt: ${startAt}, endAt: ${endAt}")
    })

    fun clear() {
        pids = HashSet()
        date = ""
        startAt = ""
        endAt = ""
        endDate = Date()
        startDate = Date()
        dateValid = false
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

