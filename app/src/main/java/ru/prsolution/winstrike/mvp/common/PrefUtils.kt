package ru.prsolution.winstrike.mvp.common

import android.content.Context
import android.content.SharedPreferences

import ru.prsolution.winstrike.WinstrikeApp


/**
 * Date: 18.01.2016
 * Time: 15:01
 *
 */
object PrefUtils {
    private val PREF_NAME = "token"

    val prefs: SharedPreferences
        get() = WinstrikeApp.getInstance().appComponent.context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    val editor: SharedPreferences.Editor
        get() = prefs.edit()
}
