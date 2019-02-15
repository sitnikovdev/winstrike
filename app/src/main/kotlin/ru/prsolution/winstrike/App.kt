package ru.prsolution.winstrike

import android.app.Application
import com.facebook.drawee.backends.pipeline.Fresco
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

import ru.prsolution.winstrike.datasource.model.ArenaEntity
import ru.prsolution.winstrike.datasource.model.ArenaSchemaEntity
import ru.prsolution.winstrike.domain.models.SeatCarousel
import ru.prsolution.winstrike.domain.models.login.UserModel
import ru.prsolution.winstrike.presentation.utils.cache.CacheLibrary
import ru.prsolution.winstrike.presentation.utils.pref.PrefUtils

class App : Application() {
    val user: UserModel? = null
    var seat: SeatCarousel? = null
    var roomLayout: ArenaSchemaEntity? = null
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
/*        when {
            LeakCanary.isInAnalyzerProcess(this) -> return
            // Report Leaks to Firebase Crashlytics? :thinking:
            // https://github.com/square/leakcanary/wiki/Customizing-LeakCanary#uploading-to-a-server
            else -> LeakCanary.install(this)
        }*/
        //        Fabric.with(this, new Crashlytics());
        instance = this

        // Unique initialization of Cache library to allow saving into device
        CacheLibrary.init(this)

        // Unique initialization of Dependency Injection library to allow the use of application context
        startKoin { androidContext(this@App) }

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
        lateinit var instance: App
    }
}
