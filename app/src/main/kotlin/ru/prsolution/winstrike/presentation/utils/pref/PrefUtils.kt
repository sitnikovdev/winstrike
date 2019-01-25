package ru.prsolution.winstrike.presentation.utils.pref

object PrefUtils {
	private const val TOKEN = "token"
	private const val FCMTOKEN = "fcmtoken"
	private const val PUBLICID = "publicid"
	private const val ISFIRSTENTER = "firstLogin"
	private const val ISCONFIRMED = "isconfirmed"
	private const val ISLOGOUT = "islogout"
	private const val PHONE = "phone"
	private const val NAME = "name"
	private const val PASSWORD = "password"
	private const val SELECTEDARENA = "selectedArena"
	private const val DISPLAYWIDHTPX = "displayWidhtPx"
	private const val DISPLAYHEIGHTPX = "displayHeightPx"
	private const val DISPLAYHEIGHTDP = "displayHeightDp"
	private const val DISPLAYWIDHTDP = "displayWidhtDp"

	private const val STARTTIME = "startTime"
	private const val ENDTIME = "endTime"

	private const val ARENAPID = "arenaPid"

	var arenaPid: String?
		get() = SharedPrefFactory.prefs.getString(ARENAPID, "")
		set(arenaPid) {
			SharedPrefFactory.editor.putString(ARENAPID, arenaPid).commit()
		}

	var startTime: String?
		get() = SharedPrefFactory.prefs.getString(STARTTIME, "")
		set(startTime) {
			SharedPrefFactory.editor.putString(STARTTIME, startTime).commit()
		}


	var endTime: String?
		get() = SharedPrefFactory.prefs.getString(ENDTIME, "")
		set(endTime) {
			SharedPrefFactory.editor.putString(ENDTIME, endTime).commit()
		}

	var displayWidhtDp: Float
		get() = SharedPrefFactory.prefs.getFloat(DISPLAYWIDHTDP, 0f)
		set(displayWidhtDp) {
			SharedPrefFactory.editor.putFloat(DISPLAYWIDHTDP, displayWidhtDp).commit()
		}
	var displayHeightDp: Float
		get() = SharedPrefFactory.prefs.getFloat(DISPLAYHEIGHTDP, 0f)
		set(displayHeightDp) {
			SharedPrefFactory.editor.putFloat(DISPLAYHEIGHTDP, displayHeightDp).commit()
		}

	var displayHeightPx: Float
		get() = SharedPrefFactory.prefs.getFloat(DISPLAYHEIGHTPX, 0f)
		set(displayHeightPx) {
			SharedPrefFactory.editor.putFloat(DISPLAYHEIGHTPX, displayHeightPx).commit()
		}

	var displayWidhtPx: Float
		get() = SharedPrefFactory.prefs.getFloat(DISPLAYWIDHTPX, 0f)
		set(displayWidhtPx) {
			SharedPrefFactory.editor.putFloat(DISPLAYWIDHTPX, displayWidhtPx).commit()
		}

	var token: String?
		get() = SharedPrefFactory.prefs.getString(TOKEN, "")
		set(token) {
			SharedPrefFactory.editor.putString(TOKEN, token).commit()
		}

	var fcmtoken: String?
		get() = SharedPrefFactory.prefs.getString(FCMTOKEN, "")
		set(fcmtoken) {
			SharedPrefFactory.editor.putString(FCMTOKEN, fcmtoken).commit()
		}

	var publicid: String?
		get() = SharedPrefFactory.prefs.getString(PUBLICID, "")
		set(publicid) {
			SharedPrefFactory.editor.putString(PUBLICID, publicid).commit()
		}

	var selectedArena: Int
		get() = SharedPrefFactory.prefs.getInt(SELECTEDARENA, 0)
		set(selectedArena) {
			SharedPrefFactory.editor.putInt(SELECTEDARENA, selectedArena).commit()
		}

	var isFirstLogin: Boolean
		get() = SharedPrefFactory.prefs.getBoolean(ISFIRSTENTER, true)
		set(isFirstLogin) {
			SharedPrefFactory.editor.putBoolean(ISFIRSTENTER, isFirstLogin).commit()
		}

	var isConfirmed: Boolean
		get() = SharedPrefFactory.prefs.getBoolean(ISCONFIRMED, false)
		set(isConfirmed) {
			SharedPrefFactory.editor.putBoolean(ISCONFIRMED, isConfirmed).commit()
		}

	var isLogout: Boolean
		get() = SharedPrefFactory.prefs.getBoolean(ISLOGOUT, false)
		set(isLogout) {
			SharedPrefFactory.editor.putBoolean(ISLOGOUT, isLogout).commit()
		}

	var phone: String?
		get() = SharedPrefFactory.prefs.getString(PHONE, "")
		set(phone) {
			SharedPrefFactory.editor.putString(PHONE, phone).commit()
		}

	var name: String?
		get() = SharedPrefFactory.prefs.getString(NAME, "")
		set(name) {
			SharedPrefFactory.editor.putString(NAME, name).commit()
		}

	var password: String?
		get() = SharedPrefFactory.prefs.getString(PASSWORD, "")
		set(password) {
			SharedPrefFactory.editor.putString(PASSWORD, password).commit()
		}


}
