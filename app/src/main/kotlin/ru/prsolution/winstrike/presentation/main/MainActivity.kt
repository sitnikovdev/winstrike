package ru.prsolution.winstrike.presentation.main

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import kotlinx.android.synthetic.main.ac_mainscreen.*
import kotlinx.android.synthetic.main.toolbar.*
import org.koin.androidx.viewmodel.ext.viewModel
import ru.prsolution.winstrike.R
import ru.prsolution.winstrike.domain.models.arena.SeatCarousel
import ru.prsolution.winstrike.domain.models.common.FCMModel
import ru.prsolution.winstrike.presentation.NavigationListener
import ru.prsolution.winstrike.presentation.main.carousel.CarouselFragment
import ru.prsolution.winstrike.presentation.utils.Constants
import ru.prsolution.winstrike.presentation.utils.date.TimeDataModel
import ru.prsolution.winstrike.presentation.utils.hide
import ru.prsolution.winstrike.presentation.utils.pref.PrefUtils
import ru.prsolution.winstrike.presentation.utils.show
import ru.prsolution.winstrike.viewmodel.FCMViewModel
import timber.log.Timber

interface ToolbarTitleListener {
    fun updateTitle(title: String)
}


class MainActivity : AppCompatActivity(), ToolbarTitleListener,
    CarouselFragment.OnSeatClickListener, NavigationListener {

    private val mVmFCM: FCMViewModel by viewModel()

    private lateinit var appBarConfiguration: AppBarConfiguration
    private var mDlgMapLegend: Dialog? = null
    var mArenaActiveLayoutPid: String? = ""
    private var mCityMenuVisible: Boolean = false
    private lateinit var mMenuCity: MenuItem
    private var mProfileMenuVisible: Boolean = false
    private lateinit var mMenuProfile: MenuItem
    private var mMapMenuVisible: Boolean = false
    private lateinit var mMenuMap: MenuItem

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

        dlgMapLegend()

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
                    mMapMenuVisible = false
                    invalidateOptionsMenu()
                }
                R.id.navigation_order -> {
                    bottomNavigation.show()
                    mCityMenuVisible = false
                    mProfileMenuVisible = false
                    mMapMenuVisible = false
                    invalidateOptionsMenu()
                }
                R.id.navigation_profile -> {
                    bottomNavigation.show()
                    mCityMenuVisible = false
                    mProfileMenuVisible = true
                    mMapMenuVisible = false
                    invalidateOptionsMenu()
                }
                R.id.navigation_map -> {
                    bottomNavigation.hide()
                    mCityMenuVisible = false
                    mProfileMenuVisible = false
                    mMapMenuVisible = true
                    invalidateOptionsMenu()
                }
                else -> {
                    bottomNavigation.hide()
                    mCityMenuVisible = false
                    mProfileMenuVisible = false
                    mMapMenuVisible = false
                    invalidateOptionsMenu()
                }
            }
        }


        appBarConfiguration = AppBarConfiguration(navController.graph)

        setSupportActionBar(toolbar)
        setupActionBar(navController, appBarConfiguration)
        setupBottomNavMenu(navController)


        initFCM() // FCM push notifications

        mVmFCM.messageResponse.observe(this@MainActivity, Observer {
            it?.let { resource ->
                // TODO: process error!
                when (resource.state) {
//                    ResourceState.LOADING -> swipeRefreshLayout.startRefreshing()
//                    ResourceState.SUCCESS -> swipeRefreshLayout.stopRefreshing()
//                    ResourceState.ERROR -> swipeRefreshLayout.stopRefreshing()
                }
                resource.data?.let {
                    Timber.tag("$$$").d("FCM token sent!")
                }
                resource.message?.let {
                    Timber.tag("$$$").e("FCM token DIDN'T sent!")
                }
            }
        })

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
        mMenuMap = menu.findItem(R.id.map_legend)
        mMenuCity.isVisible = mCityMenuVisible
        mMenuProfile.isVisible = mProfileMenuVisible
        mMenuMap.isVisible = mMapMenuVisible
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {
            R.id.navigation_login_activity ->
                PrefUtils.token = ""
            R.id.map_legend ->
                mDlgMapLegend?.show()
        }

        return NavigationUI.onNavDestinationSelected(item!!, navController) ||
                super.onOptionsItemSelected(item)
    }

    // Send FCM code to server
    private fun initFCM() {
        val fcmToken = PrefUtils.fcmtoken
        fcmToken?.let {
            mVmFCM.sendFCMCode(FCMModel(it))
        }
    }


    private fun clearData() {
        TimeDataModel.clearPids()
        TimeDataModel.clearDateTime()
    }


    // Fragment navigation
    override fun navigate(action: NavDirections) {
        Navigation.findNavController(this, ru.prsolution.winstrike.R.id.main_host_fragment).navigate(action)
    }


    // TODO: remove this Map actions block:
    private fun dlgMapLegend() {
        mDlgMapLegend = Dialog(this, android.R.style.Theme_Dialog)
        mDlgMapLegend?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        mDlgMapLegend?.setContentView(R.layout.dlg_legend)
        val tvSee = mDlgMapLegend?.findViewById<TextView>(R.id.tv_see)

        tvSee?.setOnClickListener { mDlgMapLegend?.dismiss() }

        mDlgMapLegend?.setCanceledOnTouchOutside(true)
        mDlgMapLegend?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        mDlgMapLegend?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val window = mDlgMapLegend?.window
        val wlp = window?.attributes

        wlp?.gravity = Gravity.TOP
        wlp?.flags = wlp?.flags?.and(WindowManager.LayoutParams.FLAG_DIM_BEHIND.inv())
        wlp?.y = Constants.LEGEND_MAP_TOP_MARGIN
        window?.attributes = wlp

        mDlgMapLegend?.setCanceledOnTouchOutside(false)
        mDlgMapLegend?.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        mDlgMapLegend?.dismiss()
    }


}
