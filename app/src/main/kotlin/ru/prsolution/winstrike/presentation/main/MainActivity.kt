package ru.prsolution.winstrike.presentation.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import kotlinx.android.synthetic.main.ac_mainscreen.*
import org.jetbrains.anko.toast
import ru.prsolution.winstrike.R
import ru.prsolution.winstrike.domain.models.SeatCarousel
import ru.prsolution.winstrike.domain.payment.PaymentResponse
import ru.prsolution.winstrike.presentation.main.carousel.CarouselFragment
import ru.prsolution.winstrike.presentation.map.MapFragmentDirections
import ru.prsolution.winstrike.presentation.utils.date.TimeDataModel
import ru.prsolution.winstrike.presentation.setup.SetupFragment
import ru.prsolution.winstrike.presentation.setup.SetupFragmentDirections
import ru.prsolution.winstrike.presentation.utils.pref.PrefUtils
import ru.prsolution.winstrike.presentation.utils.resouces.ResourceState
import timber.log.Timber

class MainActivity : AppCompatActivity(),
    SetupFragment.MapShowListener,
    CarouselFragment.OnSeatClickListener {

    private lateinit var appBarConfiguration: AppBarConfiguration

    private lateinit var mVm: MainViewModel


    // Open Yandex WebView on payment response from MapFragment
    private fun onPaymentShow(payResponse: PaymentResponse) {
//        Timber.tag("common").d("Pay successfully: %s", payResponse)
        // TODO: Show progress bar when load web view.
        val url = payResponse.redirectUrl
        val testUrl = "https://yandex.ru"
        val action = MapFragmentDirections.nextAction(testUrl)
        findNavController(R.id.nav_host_fragment).navigate(action)

//        mVm.redirectUrl.value = testUrl
    }


    // Show map fragment after user select date and time in SetupFragment
    override fun onMapShow() {
        val action = SetupFragmentDirections.nextAction()
        findNavController(R.id.nav_host_fragment).navigate(action)
    }

    // Show SetUpFragment when user click on carousel view selected seat item
    override fun onCarouselClick(seat: SeatCarousel?) {
        val action = HomeFragmentDirections.nextAction()
        action.seat = seat
        findNavController(R.id.nav_host_fragment).navigate(action)
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

        // payment response from map fragment:
        mVm.paymentResponse.observe(this, Observer {
            // load
            it.state.let { state ->
                if (state == ResourceState.LOADING) {
                }
            }
            // data
            it.data?.let { response ->
                onPaymentShow(response)
            }
            // error
            it.message?.let { error ->
                Timber.tag("$$$").d("message: $error")
                onPaymentError(error)
            }
        })

        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)

        appBarConfiguration = AppBarConfiguration(navController.graph)

        setSupportActionBar(toolbar)
        setupActionBar(navController, appBarConfiguration)
        setupBottomNavMenu(navController)

//        initFCM() // FCM push notifications
    }

    private fun setupActionBar(
        navController: NavController,
        appBarConfig: AppBarConfiguration
    ) {
        setupActionBarWithNavController(navController, appBarConfig)
    }

    private fun setupBottomNavMenu(navController: NavController) {
        bottomNavigation?.let {
            NavigationUI.setupWithNavController(it, navController)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(Navigation.findNavController(this, R.id.nav_host_fragment), appBarConfiguration)
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

    private fun onPaymentError(error: String) {
        TimeDataModel.pids.clear()
        if (error.contains("different time")) {
            toast("Не удается забронировать место на указанный интервал времени.")
        } else {
            toast("Не удается забронировать место.")
        }
    }

    private fun initFCM() {
        val fcmToken = PrefUtils.fcmtoken
        val userToken = PrefUtils.token
        if (!fcmToken?.isEmpty()!!) {
            // TODO fix it: server send "message": "Token is missing" (see logs). Try install chuck.
// 			userToken?.let { vm.sendFCMToken(it, FCMModel(PrefUtils.fcmtoken)) }
        }
    }

    private fun clearData() {
        TimeDataModel.clearPids()
        TimeDataModel.clearDateTime()
    }
}
