package ru.prsolution.winstrike

import android.app.Application
import android.util.DisplayMetrics
import com.facebook.drawee.backends.pipeline.Fresco
import java.io.File

import retrofit2.Retrofit
import ru.prsolution.winstrike.datasource.model.Room
import ru.prsolution.winstrike.datasource.model.RoomLayout
import ru.prsolution.winstrike.di.AppComponent
import ru.prsolution.winstrike.di.module.ContextModule
import ru.prsolution.winstrike.di.module.NetworkModule
import ru.prsolution.winstrike.domain.models.SeatModel
import ru.prsolution.winstrike.domain.models.UserEntity
import ru.prsolution.winstrike.networking.Service


class WinstrikeApp : Application() {
	private var sAppComponent: AppComponent? = null
	val user: UserEntity? = null
	var seat: SeatModel? = null
	var roomLayout: RoomLayout? = null
	var rooms: List<Room>? = null


	val appComponent: AppComponent?
		get() {
			if (sAppComponent == null) {
				val cacheFile = File(cacheDir, "responses")
/*				sAppComponent = DaggerAppComponent.builder()
						.contextModule(ContextModule(this))
						.networkModule(NetworkModule(cacheFile, applicationContext))
						.build()*/
			}
			return sAppComponent
		}

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

	}

	companion object {
		val DEBUG = true
		lateinit var instance: WinstrikeApp
	}
}
