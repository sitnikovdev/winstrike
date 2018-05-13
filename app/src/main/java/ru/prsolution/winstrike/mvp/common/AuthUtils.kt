package ru.prsolution.winstrike.mvp.common

/**
 * Date: 18.01.2016
 * Time: 15:02
 *
 * @author Yuri Shmakov
 */
object AuthUtils {
    private val TOKEN = "token"

    var token: String
        get() = PrefUtils.prefs.getString(TOKEN, "")
        set(token) {
            PrefUtils.editor.putString(TOKEN, token).commit()
        }
}
