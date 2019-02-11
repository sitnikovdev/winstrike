package ru.prsolution.winstrike.presentation.main

import android.os.Bundle
import android.view.View
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
import ru.prsolution.winstrike.R
import ru.prsolution.winstrike.domain.models.SeatCarousel
import ru.prsolution.winstrike.presentation.main.carousel.CarouselFragment
import ru.prsolution.winstrike.presentation.utils.date.TimeDataModel
import ru.prsolution.winstrike.presentation.utils.pref.PrefUtils
import ru.prsolution.winstrike.presentation.utils.pref.PrefUtils.selectedArena

class MainActivity : AppCompatActivity(),
    CarouselFragment.OnSeatClickListener {

    private lateinit var appBarConfiguration: AppBarConfiguration

    private lateinit var mVm: MainViewModel

    private var mArenaPid: String = ""
    val EXIT_DURATION = 1L
    val ENTER_DURATION = 1L

    // Show SetUpFragment when user click on carousel view selected seat item
    override fun onCarouselClick(seat: SeatCarousel?) {
        val action = HomeFragmentDirections.nextAction()
        action.seat = seat
        action.arenaPid = this.mArenaPid
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


        findNavController(R.id.nav_host_fragment).addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.navigation_home,R.id.navigation_order, R.id.navigation_profile -> showBottomNavigation()
                else -> hideBottomNavigation()
            }
        }

/*    findNavController(R.id.nav_host_fragment).addOnNavigatedListener { _, destination ->
        when (destination.id) {
            R.id.dashboardFragment -> showBottomNavigation()
            else -> hideBottomNavigation()
        }
    }*/

        if (savedInstanceState == null) {
            mVm.getArenaList()
        }

        mVm.arenaList.observe(this, Observer { resource ->
            resource.let {
                val arena = resource?.data?.get(selectedArena)
                if (arena != null) {
                    this.mArenaPid = arena.activeLayoutPid!!
                }
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




    private fun hideBottomNavigation() {
        // bottom_navigation is BottomNavigationView
        with(bottomNavigation) {
            if (visibility == View.VISIBLE && alpha == 1f) {
                animate()
                    .alpha(0f)
                    .withEndAction { visibility = View.GONE }
                    .duration = EXIT_DURATION
            }
        }
    }

    private fun showBottomNavigation() {
        // bottom_navigation is BottomNavigationView
        with(bottomNavigation) {
            visibility = View.VISIBLE
            animate()
                .alpha(1f)
                .duration =  ENTER_DURATION
        }
    }
}
