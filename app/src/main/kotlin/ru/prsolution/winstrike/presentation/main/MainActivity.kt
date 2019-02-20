package ru.prsolution.winstrike.presentation.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.invalidateOptionsMenu
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.setupActionBarWithNavController
import kotlinx.android.synthetic.main.ac_mainscreen.*
import kotlinx.android.synthetic.main.fmt_profile_app.*
import kotlinx.android.synthetic.main.toolbar.*
import ru.prsolution.winstrike.R
import ru.prsolution.winstrike.domain.models.arena.SeatCarousel
import ru.prsolution.winstrike.presentation.main.carousel.CarouselFragment
import ru.prsolution.winstrike.presentation.profile.ProfileFragmentArgs
import ru.prsolution.winstrike.presentation.utils.date.TimeDataModel
import ru.prsolution.winstrike.presentation.utils.hide
import ru.prsolution.winstrike.presentation.utils.pref.PrefUtils
import ru.prsolution.winstrike.presentation.utils.show

interface TempToolbarTitleListener {
    fun updateTitle(title: String)
}

class MainActivity : AppCompatActivity(), TempToolbarTitleListener,
    CarouselFragment.OnSeatClickListener {


    private lateinit var appBarConfiguration: AppBarConfiguration

    var mArenaActiveLayoutPid: String? = ""

    var mCityMenuVisible: Boolean = false
    lateinit var mMenuCity: MenuItem
    var mProfileMenuVisible: Boolean = false
    lateinit var mMenuProfile: MenuItem

    // Show SetUpFragment when user click on carousel view selected seat item
    override fun onCarouselClick(seat: SeatCarousel?) {
        val action = HomeFragmentDirections.nextAction()
        action.seat = seat
        action.activeLayoutPid = this.mArenaActiveLayoutPid.toString()
        findNavController(R.id.main_host_fragment).navigate(action)
    }

    override fun onStart() {
        super.onStart()
        // TODO use mListener (Fix logout!!!)
        PrefUtils.isLogout = false
    }

    private lateinit var navController: NavController

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        clearData()

        setContentView(R.layout.ac_mainscreen)

//        Navigation
        navController = Navigation.findNavController(this@MainActivity, R.id.main_host_fragment)

        navController.addOnDestinationChangedListener { nav, destination, _ ->
            when (destination.id) {
                R.id.navigation_home -> {
                    bottomNavigation.show()
                    destination.label = PrefUtils.arenaName
                    mCityMenuVisible = true
                    mProfileMenuVisible = false
                    invalidateOptionsMenu()
                }
                R.id.navigation_order -> {
                    bottomNavigation.show()
                    mCityMenuVisible = false
                    mProfileMenuVisible = false
                    invalidateOptionsMenu()
                }
                R.id.navigation_profile -> {
                    bottomNavigation.show()
                    mCityMenuVisible = false
                    mProfileMenuVisible = true
                    invalidateOptionsMenu()
                }
                else -> bottomNavigation.hide()
            }
        }


        appBarConfiguration = AppBarConfiguration(navController.graph)

        setSupportActionBar(toolbar)
        setupActionBar(navController, appBarConfiguration)
        setupBottomNavMenu(navController)


//        initFCM() // FCM push notifications
    }

    // Set title in Profile
    override fun updateTitle(title: String) {
            toolbar.title = title
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
        return NavigationUI.navigateUp(navController, appBarConfiguration)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        mMenuCity = menu?.findItem(R.id.city_activity)!!
        mMenuProfile = menu.findItem(R.id.navigation_login_activity)
        mMenuCity.isVisible = mCityMenuVisible
        mMenuProfile.isVisible = mProfileMenuVisible
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        PrefUtils.token = ""

        return NavigationUI.onNavDestinationSelected(item!!, navController) ||
                super.onOptionsItemSelected(item)
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


    // Fragment navigation
    fun navigate(action: NavDirections) {
        Navigation.findNavController(this, ru.prsolution.winstrike.R.id.main_host_fragment).navigate(action)
    }
}
