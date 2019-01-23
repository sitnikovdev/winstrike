package ru.prsolution.winstrike

import android.app.Application
import com.facebook.drawee.backends.pipeline.Fresco
import java.io.File

import ru.prsolution.winstrike.datasource.model.Room
import ru.prsolution.winstrike.datasource.model.RoomLayout
import ru.prsolution.winstrike.di.module.NetworkModule
import ru.prsolution.winstrike.domain.models.SeatModel
import ru.prsolution.winstrike.domain.models.UserEntity
import ru.prsolution.winstrike.networking.Service
import ru.prsolution.winstrike.presentation.utils.pref.AuthUtils


class WinstrikeApp : Application() {
	val user: UserEntity? = null
	var seat: SeatModel? = null
	var roomLayout: RoomLayout? = null
	var rooms: List<Room>? = null


	val service: Service
		get() {
			val cacheFile = File(cacheDir, "responses")
			val networkModule = NetworkModule(cacheFile, applicationContext)
			val retrofit = networkModule.provideCall()
			return Service(networkModule.providesNetworkService(retrofit))
		}


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
		AuthUtils.displayHeightPx = displayHeightPx
		AuthUtils.displayWidhtPx = displayWidhtPx
	}

	companion object {
		lateinit var instance: WinstrikeApp
	}
}
