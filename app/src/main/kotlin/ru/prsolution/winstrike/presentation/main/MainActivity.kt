package ru.prsolution.winstrike.presentation.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.ac_mainscreen.toolbar
import ru.prsolution.winstrike.R
import ru.prsolution.winstrike.domain.models.SeatModel
import ru.prsolution.winstrike.presentation.utils.date.TimeDataModel
import ru.prsolution.winstrike.presentation.setup.SetupFragment
import ru.prsolution.winstrike.presentation.utils.pref.PrefUtils
import timber.log.Timber

class MainActivity : FragmentActivity(),
                     SetupFragment.MapShowListener,
                     CarouselFragment.OnSeatClickListener {
	override fun onMapShow() {
		Timber.d("On map show listener")
	}

	override fun onSeatClick(seat: SeatModel?) {
		mVm.currentSeat.postValue(seat)
		showToolbar(isVisible = true)
		fm.beginTransaction()
				.hide(active)
				.addToBackStack(null)
				.show(setupFragment)
				.commit()
		active = setupFragment
	}


	fun setActive() {
		this.active = homeFragment
	}

	override fun onBackPressed() {
		if (fm.backStackEntryCount == 1) {
			showToolbar(isVisible = false)
			active = homeFragment
			super.onBackPressed()
		} else {
//			super.onBackPressed()
		}
	}


	override fun onResume() {
		super.onResume()
		active = homeFragment
	}

	override fun onStart() {
		super.onStart()
		// TODO use listener
		PrefUtils.isLogout = false
	}


	private lateinit var mVm: MainViewModel

	private val homeFragment = HomeFragment()
	private val setupFragment = SetupFragment()
	private val fm: FragmentManager = supportFragmentManager
	var active: Fragment = homeFragment

	public override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		clearData()
		setContentView(R.layout.ac_mainscreen)
		mVm = ViewModelProviders.of(this).get(MainViewModel::class.java)

		mVm.fcmResponse.observe(this, Observer {
			it.let {
				it.data?.let {
					Timber.i("FCM token send to server. \n ${it.message} ")
				}
			}
		})

		// Toolbar
		showToolbar(isVisible = false)

		with(fm) {
			beginTransaction()
					.add(R.id.main_container, setupFragment, setupFragment.javaClass.name)
					.hide(setupFragment)
					.commit()
			beginTransaction()
					.add(R.id.main_container, homeFragment, homeFragment.javaClass.name)
					.commit()

		}
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
//			userToken?.let { vm.sendFCMToken(it, FCMModel(PrefUtils.fcmtoken)) }
		}
	}

	private fun showToolbar(isVisible: Boolean) {
		if (isVisible) {
			toolbar.navigationIcon = getDrawable(R.drawable.ic_back_arrow)
			toolbar.setContentInsetsAbsolute(0, toolbar.contentInsetStartWithNavigation)
		} else {
			toolbar.navigationIcon = null
			toolbar.setContentInsetsAbsolute(0, toolbar.contentInsetStart)
		}
	}

}
