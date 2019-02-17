package ru.prsolution.winstrike.presentation.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import kotlinx.android.synthetic.main.ac_mainscreen.*
import kotlinx.android.synthetic.main.toolbar.*
import ru.prsolution.winstrike.R
import ru.prsolution.winstrike.domain.models.arena.SeatCarousel
import ru.prsolution.winstrike.presentation.main.carousel.CarouselFragment
import ru.prsolution.winstrike.presentation.utils.date.TimeDataModel
import ru.prsolution.winstrike.presentation.utils.hide
import ru.prsolution.winstrike.presentation.utils.pref.PrefUtils
import ru.prsolution.winstrike.presentation.utils.show

class MainActivity : AppCompatActivity(),
    CarouselFragment.OnSeatClickListener {

    private lateinit var appBarConfiguration: AppBarConfiguration

    var mArenaActiveLayoutPid: String? = ""

    // Show SetUpFragment when user click on carousel view selected seat item
    override fun onCarouselClick(seat: SeatCarousel?) {
        val action = HomeFragmentDirections.nextAction()
        action.seat = seat
        action.activeLayoutPid = this.mArenaActiveLayoutPid.toString()
        findNavController(R.id.login_host_fragment).navigate(action)
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
        navController = Navigation.findNavController(this@MainActivity, R.id.login_host_fragment)

        navController.addOnDestinationChangedListener { nav, destination, _ ->
            when (destination.id) {
                R.id.navigation_home -> {bottomNavigation.show()
                   destination.label = PrefUtils.arenaName
                }
                R.id.navigation_order,
                R.id.navigation_profile -> bottomNavigation.show()
                else -> bottomNavigation.hide()
            }
        }


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
        return NavigationUI.navigateUp(navController, appBarConfiguration)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

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
}
