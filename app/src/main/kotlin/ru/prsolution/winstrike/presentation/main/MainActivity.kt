package ru.prsolution.winstrike.presentation.main

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.ac_mainscreen.navigation
import kotlinx.android.synthetic.main.ac_mainscreen.toolbar
import org.jetbrains.anko.toast
import ru.prsolution.winstrike.R
import ru.prsolution.winstrike.domain.models.SeatCarousel
import ru.prsolution.winstrike.domain.payment.PaymentResponse
import ru.prsolution.winstrike.presentation.map.MapFragment
import ru.prsolution.winstrike.presentation.orders.OrderFragment
import ru.prsolution.winstrike.presentation.payment.YandexWebViewFragment
import ru.prsolution.winstrike.presentation.profile.ProfileFragment
import ru.prsolution.winstrike.presentation.utils.date.TimeDataModel
import ru.prsolution.winstrike.presentation.setup.SetupFragment
import ru.prsolution.winstrike.presentation.utils.pref.PrefUtils
import ru.prsolution.winstrike.presentation.utils.resouces.ResourceState
import timber.log.Timber

class MainActivity : AppCompatActivity(),
                     SetupFragment.MapShowListener,
                     CarouselFragment.OnSeatClickListener {

    private lateinit var mVm: MainViewModel

    private val mapFragment = MapFragment()
    private val homeFragment = HomeFragment()
    private val setupFragment = SetupFragment()
    private val orderFragment = OrderFragment()
    private val profileFragment = ProfileFragment()

    private val yandexFragment = YandexWebViewFragment()
    private val fm: FragmentManager = supportFragmentManager
    var active: Fragment = homeFragment

    override fun onMapShow() {
        Timber.d("On map show mListener")
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
        when (active) {
            is SetupFragment -> {
                showHome(isVisible = false)
                navigation.visibility = View.VISIBLE
                mVm.active.value = homeFragment
                super.onBackPressed()
            }
// 			is OrderFragment -> super.onBackPressed()
            is MapFragment -> {
                mVm.active.value = setupFragment
                super.onBackPressed()
            }
            is YandexWebViewFragment -> {
                mVm.active.value = mapFragment
                 super.onBackPressed()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        active = homeFragment
    }

    override fun onStart() {
        super.onStart()
        // TODO use mListener (Fix logout!!!)
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

        mVm.paymentResponse.observe(this, Observer {
            it.state.let { state ->
                if (state == ResourceState.LOADING) {
// 					progressBar.visibility = View.VISIBLE
                }
            }
            it.data?.let { response ->
                onPaymentShow(response)
// 				progressBar.visibility = View.GONE
            }
            it.message?.let { error ->
                Timber.tag("$$$").d("message: $error")
// 				progressBar.visibility = View.GONE
                onPaymentError(error)
            }
        })

        mVm.active.observe(this, Observer { activeFragment ->
            Timber.d("active is ${activeFragment.javaClass.simpleName}")
            active = activeFragment
            if (active is HomeFragment) {
                showHome(isVisible = false)
                navigation.visibility = View.VISIBLE
                navigation.menu.getItem(0).isChecked = true
            } else {
                showHome(isVisible = true)
                navigation.visibility = View.GONE
            }
        })

// 		progressBar?.visibility = View.VISIBLE

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        showHome(isVisible = false)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

//        initToolbar()
//        initFragments()
//        initFCM() // FCM push notifications
    }

    private fun onPaymentError(error: String) {
        TimeDataModel.pids.clear()
        if (error.contains("different time")) {
            toast("Не удается забронировать место на указанный интервал времени.")
        } else {
            toast("Не удается забронировать место.")
        }
    }

    private fun initToolbar() {
        toolbar?.setNavigationOnClickListener {
            when (active) {
                is HomeFragment -> {
                    showHome(isVisible = false)
                    navigation.visibility = View.VISIBLE
                }
                is SetupFragment -> {
                    navigation.visibility = View.VISIBLE
                    mVm.active.value = homeFragment
                }
                is OrderFragment -> {
                    navigation.menu.getItem(1).isChecked = false
                    fm.beginTransaction().detach(orderFragment).show(homeFragment).commit()
                    mVm.active.value = homeFragment
                }
                is MapFragment -> {
                    navigation.visibility = View.GONE
                    mVm.active.value = setupFragment
                }

                is ProfileFragment -> {
                    navigation.menu.getItem(2).isChecked = false
                    fm.beginTransaction().detach(profileFragment).show(homeFragment).commit()
                    mVm.active.value = homeFragment
                }
            }
            supportFragmentManager.popBackStack()
        }
    }

    private fun initFragments() {
        with(fm) {
            // yandex
            beginTransaction().add(R.id.main_container, yandexFragment, yandexFragment.javaClass.name)
                    .detach(yandexFragment)
                    .commit()
            // setup
            beginTransaction()
                    .add(R.id.main_container, setupFragment, setupFragment.javaClass.name)
                    .hide(setupFragment)
                    .commit()
            // map
            beginTransaction()
                    .add(R.id.main_container, mapFragment, mapFragment.javaClass.name)
                    .detach(mapFragment)
                    .commit()
            // orders
            beginTransaction().add(R.id.main_container, orderFragment, orderFragment.javaClass.name)
                    .detach(orderFragment)
                    .commit()
            // profile
            beginTransaction().add(R.id.main_container, profileFragment, profileFragment.javaClass.name)
                    .detach(profileFragment)
                    .commit()
            beginTransaction()
                    .add(R.id.main_container, homeFragment, homeFragment.javaClass.name)
                    .commit()
        }
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
// 			userToken?.let { vm.sendFCMToken(it, FCMModel(PrefUtils.fcmtoken)) }
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

    private fun onPaymentShow(payResponse: PaymentResponse) {
        Timber.tag("common").d("Pay successfully: %s", payResponse)

        // TODO: Show progress bar when load web view.
        val url = payResponse.redirectUrl
        val testUrl = "https://yandex.ru"
        mVm.redirectUrl.value = testUrl

        Timber.d("On yandex web view show mListener")
        showHome(isVisible = true)
        navigation.visibility = View.GONE
        fm.beginTransaction()
                .detach(mapFragment)
                .addToBackStack(null)
                .attach(yandexFragment)
                .commit()
        mVm.active.value = yandexFragment
    }

    private val mOnNavigationItemSelectedListener = object : BottomNavigationView.OnNavigationItemSelectedListener {

        override fun onNavigationItemSelected(item: MenuItem): Boolean {
            when (item.itemId) {
                R.id.navigation_home -> {
                    if (active is HomeFragment) {
                        return false
                    }
                    fm.beginTransaction().detach(active).show(homeFragment).commit()
                    active = homeFragment
                    return true
                }

                R.id.navigation_dashboard -> {
                    if (active is OrderFragment) {
                        return false
                    }
                    showHome(isVisible = true)
                    if (active is HomeFragment) {
                        fm.beginTransaction().hide(active).attach(orderFragment).commit()
                    } else if (active is ProfileFragment) {
                        fm.beginTransaction().detach(active).attach(orderFragment).commit()
                    }
                    active = orderFragment
                    return true
                }

                R.id.navigation_notifications -> {
                    if (active is ProfileFragment) {
                        return false
                    }
                    showHome(isVisible = true)
                    if (active is HomeFragment) {
                        fm.beginTransaction().hide(active).attach(profileFragment).commit()
                    } else if (active is OrderFragment) {
                        fm.beginTransaction().detach(active).attach(profileFragment).commit()
                    }
                    active = profileFragment
                    return true
                }
            }
            return false
        }
    }
}
