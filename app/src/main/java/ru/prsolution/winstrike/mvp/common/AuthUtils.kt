package ru.prsolution.winstrike.mvp.common

object AuthUtils {
    private val TOKEN = "token"
    private val PUBLICID = "publicid"
    private val ISFIRSTENTER = "firstLogin"
    private val ISREGISTERED = "isconfirmed"
    private val ISLOGOUT = "islogout"
    private val PHONE = "phone"
    private val NAME = "name"
    private val PASSWORD = "password"

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
        set(isLogout) {
            PrefUtils.editor.putBoolean(ISLOGOUT, isLogout).commit()
        }

    var phone: String
        get() = PrefUtils.prefs.getString(PHONE, "")
        set(phone) {
            PrefUtils.editor.putString(PHONE, phone).commit()
        }

    var name: String
        get() = PrefUtils.prefs.getString(NAME, "")
        set(name) {
            PrefUtils.editor.putString(NAME, name).commit()
        }

    var password: String
        get() = PrefUtils.prefs.getString(PASSWORD, "")
        set(password) {
            PrefUtils.editor.putString(PASSWORD, password).commit()
        }


}
