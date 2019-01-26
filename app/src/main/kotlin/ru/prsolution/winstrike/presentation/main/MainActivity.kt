package ru.prsolution.winstrike.presentation.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.ac_mainscreen.navigation
import kotlinx.android.synthetic.main.ac_mainscreen.toolbar
import ru.prsolution.winstrike.R
import ru.prsolution.winstrike.domain.models.SeatCarousel
import ru.prsolution.winstrike.presentation.map.MapFragment
import ru.prsolution.winstrike.presentation.utils.date.TimeDataModel
import ru.prsolution.winstrike.presentation.setup.SetupFragment
import ru.prsolution.winstrike.presentation.utils.pref.PrefUtils
import timber.log.Timber

class MainActivity : FragmentActivity(),
                     SetupFragment.MapShowListener,
                     CarouselFragment.OnSeatClickListener {

	private lateinit var mVm: MainViewModel

	private val mapFragment = MapFragment()
	private val homeFragment = HomeFragment()
	private val setupFragment = SetupFragment()
	private val fm: FragmentManager = supportFragmentManager
	var active: Fragment = homeFragment

	override fun onMapShow() {
		Timber.d("On map show listener")
		showHome(isVisible = true)
		navigation.visibility = View.GONE
		fm.beginTransaction()
				.hide(setupFragment)
				.addToBackStack(null)
				.attach(mapFragment)
				.commit()
		mVm.active.value = mapFragment
	}

	override fun onSeatClick(seat: SeatCarousel?) {
		mVm.currentSeat.postValue(seat)
		showHome(isVisible = true)
		navigation.visibility = View.GONE
		fm.beginTransaction()
				.hide(active)
				.addToBackStack(null)
				.show(setupFragment)
				.commit()
		mVm.active.value = setupFragment
	}


	override fun onBackPressed() {
		// TODO get by instance
		if (fm.backStackEntryCount == 1) {
			showHome(isVisible = false)
			mVm.active.value = homeFragment
			super.onBackPressed()
		} else if (fm.backStackEntryCount == 2) {
			super.onBackPressed()
		}
	}


	override fun onResume() {
		super.onResume()
		active = homeFragment
	}

	override fun onStart() {
		super.onStart()
		// TODO use listener (Fix logout!!!)
		PrefUtils.isLogout = false
	}


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


		mVm.active.observe(this, Observer { activeFragment ->
			Timber.d("active is ${activeFragment.javaClass.simpleName}")
			active = activeFragment
			if (active is HomeFragment) {
				showHome(isVisible = false)
				navigation.visibility = View.VISIBLE
			} else {
				showHome(isVisible = true)
				navigation.visibility = View.GONE
			}
		})

		toolbar?.setNavigationOnClickListener {
			supportFragmentManager.popBackStack()
			when (active) {
				is HomeFragment -> {
					showHome(isVisible = false)
					navigation.visibility = View.VISIBLE
				}
				is SetupFragment -> {
					showHome(isVisible = false)
					navigation.visibility = View.VISIBLE
					mVm.active.value = homeFragment
				}
				is MapFragment -> {
					showHome(isVisible = true)
					navigation.visibility = View.GONE
					mVm.active.value = setupFragment
				}
			}

		}


		with(fm) {
			beginTransaction()
					.add(R.id.main_container, setupFragment, setupFragment.javaClass.name)
					.hide(setupFragment)
					.commit()
			beginTransaction()
					.add(R.id.main_container, mapFragment, mapFragment.javaClass.name)
					.detach(mapFragment)
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

	private fun showHome(isVisible: Boolean) {
		if (isVisible) {
			toolbar.navigationIcon = getDrawable(R.drawable.ic_back_arrow)
			toolbar.setContentInsetsAbsolute(0, toolbar.contentInsetStartWithNavigation)
		} else {
			toolbar.navigationIcon = null
			toolbar.setContentInsetsAbsolute(0, toolbar.contentInsetStart)
		}
	}

}
