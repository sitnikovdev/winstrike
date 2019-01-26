package ru.prsolution.winstrike

import android.app.Application
import com.facebook.drawee.backends.pipeline.Fresco

import ru.prsolution.winstrike.datasource.model.ArenaEntity
import ru.prsolution.winstrike.datasource.model.RoomEntity
import ru.prsolution.winstrike.domain.models.SeatCarousel
import ru.prsolution.winstrike.domain.models.login.UserEntity
import ru.prsolution.winstrike.presentation.utils.pref.PrefUtils


class WinstrikeApp : Application() {
	val user: UserEntity? = null
	var seat: SeatCarousel? = null
	var roomLayout: RoomEntity? = null
	var rooms: List<ArenaEntity>? = null




	val displayWidhtDp: Float
		get() {
			val displayMetrics = this.resources.displayMetrics
			val dpWidth = displayMetrics.widthPixels / displayMetrics.density
			return dpWidth
		}

	val displayHeightDp: Float
		get() {
			val displayMetrics = this.resources.displayMetrics
			val dpHeight = displayMetrics.heightPixels / displayMetrics.density
			return dpHeight
		}

	val displayHeightPx: Float
		get() {
			val displayMetrics = this.resources.displayMetrics
			val dpHeight = displayMetrics.heightPixels.toFloat()
			return dpHeight
		}

	val displayWidhtPx: Float
		get() {
			val displayMetrics = this.resources.displayMetrics
			val dpWidth = displayMetrics.widthPixels.toFloat()
			return dpWidth
		}

	override fun onCreate() {
		super.onCreate()
		//        Fabric.with(this, new Crashlytics());
		instance = this

		Fresco.initialize(this)

		initScreenPref()

	}

	private fun initScreenPref() {
		PrefUtils.displayHeightPx = displayHeightPx
		PrefUtils.displayWidhtPx = displayWidhtPx
		PrefUtils.displayHeightDp = displayHeightDp
		PrefUtils.displayWidhtDp = displayWidhtDp
	}

	companion object {
		lateinit var instance: WinstrikeApp
	}
}
