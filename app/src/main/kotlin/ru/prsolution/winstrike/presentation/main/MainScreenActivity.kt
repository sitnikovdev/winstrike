package ru.prsolution.winstrike.presentation.main

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.ac_mainscreen.toolbar
import ru.prsolution.winstrike.R
import ru.prsolution.winstrike.domain.models.FCMModel
import ru.prsolution.winstrike.domain.models.TimeDataModel
import ru.prsolution.winstrike.presentation.utils.pref.PrefUtils
import timber.log.Timber

class MainScreenActivity : FragmentActivity() {

	private lateinit var mViewModel: MainScreenViewModel


	override fun onStart() {
		super.onStart()
		// TODO use listener
		PrefUtils.isLogout = false
	}


	private lateinit var vm: MainScreenViewModel

	val homeScreenFragment = HomeScreenFragment()
	val fragmentManager = supportFragmentManager
	val activeFragment = homeScreenFragment

	public override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		clearData()
		setContentView(R.layout.ac_mainscreen)
		vm = ViewModelProviders.of(this).get(MainScreenViewModel::class.java)

		vm.fcmResponse.observe(this, Observer {
			it.let {
				it.data?.let {
					Timber.i("FCM token send to server. \n ${it.message} ")
				}
			}
		})

		// Toolbar
		toolbar.navigationIcon = getDrawable(R.drawable.ic_back_arrow)
		toolbar.setContentInsetsAbsolute(0, toolbar.contentInsetStartWithNavigation)
		fragmentManager.beginTransaction().add(R.id.main_container, homeScreenFragment, "home").commit()
		initFCM() // FCM push notifications
	}


	private fun clearData() {
		TimeDataModel.clearPids()
		TimeDataModel.clearDateTime()
	}


	private fun initFCM() {
		val fcmToken = PrefUtils.fcmtoken
		val userToken = PrefUtils.token
		if (!fcmToken?.isEmpty()!!) {
			// TODO fix it: server send "message": "Token is missing" (see logs). Try install chuck.
			userToken?.let { vm.sendFCMToken(it, FCMModel(PrefUtils.fcmtoken)) }
		}
	}

}
