package ru.prsolution.winstrike.mvp.common

object AuthUtils {
    private val TOKEN = "token"
    private val PUBLICID = "publicid"
    private val ISFIRSTENTER = "firstLogin"
    private val ISREGISTERED = "isconfirmed"
    private val ISLOGOUT = "islogout"

    var token: String
        get() = PrefUtils.prefs.getString(TOKEN, "")
        set(token) {
            PrefUtils.editor.putString(TOKEN, token).commit()
        }

    var publicid: String
        get() = PrefUtils.prefs.getString(PUBLICID, "")
        set(publicid) {
            PrefUtils.editor.putString(PUBLICID, publicid).commit()
        }

    var isFirstLogin: Boolean
        get() = PrefUtils.prefs.getBoolean(ISFIRSTENTER, true)
        set(isFirstLogin) {
            PrefUtils.editor.putBoolean(ISFIRSTENTER, isFirstLogin).commit()
        }

    var isRegistered: Boolean
        get() = PrefUtils.prefs.getBoolean(ISREGISTERED, false)
        set(isConfirmed) {
            PrefUtils.editor.putBoolean(ISREGISTERED, isConfirmed).commit()
        }

    var isLogout: Boolean
        get() = PrefUtils.prefs.getBoolean(ISLOGOUT, false)
        set(isConfirmed) {
            PrefUtils.editor.putBoolean(ISLOGOUT, isConfirmed).commit()
        }

}
