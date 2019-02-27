package ru.prsolution.winstrike.presentation.main

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.*
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
import ru.prsolution.winstrike.presentation.injectFeature
import ru.prsolution.winstrike.presentation.login.LoginFragmentDirections
import ru.prsolution.winstrike.presentation.main.carousel.CarouselFragment
import ru.prsolution.winstrike.presentation.model.fcm.FCMPid
import ru.prsolution.winstrike.presentation.setup.SetupFragmentDirections
import ru.prsolution.winstrike.presentation.splash.SplashFragmentDirections
import ru.prsolution.winstrike.presentation.utils.Constants
import ru.prsolution.winstrike.presentation.utils.Constants.URL_CONDITION
import ru.prsolution.winstrike.presentation.utils.Constants.URL_POLITIKA
import ru.prsolution.winstrike.presentation.utils.date.TimeDataModel
import ru.prsolution.winstrike.presentation.utils.hide
import ru.prsolution.winstrike.presentation.utils.pref.PrefUtils
import ru.prsolution.winstrike.presentation.utils.show
import ru.prsolution.winstrike.viewmodel.FCMViewModel
import timber.log.Timber
import java.util.concurrent.atomic.AtomicBoolean

interface ToolbarTitleListener {
    fun updateTitle(title: String)
}

interface FooterProvider {
    fun setLoginPolicyFooter(textView: TextView)
    fun setNamePolicyFooter(textView: TextView)
    fun setCodePolicyFooter(textView: TextView)
    fun setPhoneHint(textView: TextView, phone: String?)
    fun setRegisterLoginFooter(textView: TextView, action: NavDirections)
}

class MainActivity : AppCompatActivity(), ToolbarTitleListener,
    CarouselFragment.OnSeatClickListener, NavigationListener, FooterProvider {

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

        // Init Koin modules
        injectFeature()

        dlgMapLegend()

        clearData()

        setContentView(R.layout.ac_mainscreen)

         val pending = AtomicBoolean(false)
//        Navigation
        navController = Navigation.findNavController(this@MainActivity, R.id.main_host_fragment)

        navController.addOnDestinationChangedListener { nav, destination, _ ->
            when (destination.id) {
                R.id.navigation_splash -> {
                    if (pending.compareAndSet(false, true)) {
                        Timber.d("show splash")
                    } else {
                        pending.set(true)
                        destination.label = "Выберите город"
                        val action = SplashFragmentDirections.actionToCityList()
                        navigate(action)
                    }
                }
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
        mMenuCity = menu?.findItem(R.id.navigation_city_list)!!
        mMenuProfile = menu.findItem(R.id.action_to_login)
        mMenuMap = menu.findItem(R.id.map_legend)
        mMenuCity.isVisible = mCityMenuVisible
        mMenuProfile.isVisible = mProfileMenuVisible
        mMenuMap.isVisible = mMapMenuVisible
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {
            R.id.action_to_login ->
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
            mVmFCM.sendFCMCode(FCMPid(it))
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


    //    Login policy footer
    override fun setLoginPolicyFooter(textView: TextView) {

        val textCondAndPolicy = SpannableString(getString(R.string.fmt_login_politika_footer))
        val conditionClick = object : ClickableSpan() {
            override fun onClick(v: View) {
                val action = LoginFragmentDirections.nextActionPolitika(URL_CONDITION)
                action.title = getString(ru.prsolution.winstrike.R.string.fmt_title_condition)
                Navigation.findNavController(this@MainActivity, ru.prsolution.winstrike.R.id.main_host_fragment)
                    .navigate(action)
            }
        }
        val policyClick = object : ClickableSpan() {
            override fun onClick(v: View) {
                val action = LoginFragmentDirections.nextActionPolitika(URL_POLITIKA)
                action.title = getString(ru.prsolution.winstrike.R.string.fmt_login_title_politika)
                Navigation.findNavController(this@MainActivity, ru.prsolution.winstrike.R.id.main_host_fragment)
                    .navigate(action)
            }
        }
        textCondAndPolicy.setSpan(conditionClick, 0, 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        textCondAndPolicy.setSpan(policyClick, 12, textCondAndPolicy.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView.movementMethod = LinkMovementMethod.getInstance()
        textView.text = textCondAndPolicy
    }

    //    Code policy footer
    override fun setCodePolicyFooter(textView: TextView) {

        val textCondAndPolicy = SpannableString(getString(ru.prsolution.winstrike.R.string.fmt_login_politika_footer))
        val conditionClick = object : ClickableSpan() {
            override fun onClick(v: View) {
                //TODO: fix it
            }
        }
        val policyClick = object : ClickableSpan() {
            override fun onClick(v: View) {
                //TODO: fix it
            }
        }
        textCondAndPolicy.setSpan(conditionClick, 0, 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        textCondAndPolicy.setSpan(policyClick, 12, textCondAndPolicy.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView.movementMethod = LinkMovementMethod.getInstance()
        textView.text = textCondAndPolicy
    }

    //    Name policy footer
    override fun setNamePolicyFooter(textView: TextView) {

        val textCondAndPolicy = SpannableString(getString(ru.prsolution.winstrike.R.string.fmt_login_politika_footer))
        val conditionClick = object : ClickableSpan() {
            override fun onClick(v: View) {
                //TODO: fix it
            }
        }
        val policyClick = object : ClickableSpan() {
            override fun onClick(v: View) {
                //TODO: fix it
            }
        }
        textCondAndPolicy.setSpan(conditionClick, 0, 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        textCondAndPolicy.setSpan(policyClick, 12, textCondAndPolicy.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView.movementMethod = LinkMovementMethod.getInstance()
        textView.text = textCondAndPolicy
    }

    // Phone hint and phone
    override fun setPhoneHint(textView: TextView, phone: String?) {
        val phoneHint = SpannableString(
            "Введите 6-значный код, который был\n" +
                    "отправлен на номер $phone"
        )
        phoneHint.setSpan(
            ForegroundColorSpan(Color.BLACK), phoneHint.length - 12, phoneHint.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        textView.text = phoneHint
    }

    //    Login Footer
    override fun setRegisterLoginFooter(textView: TextView, action: NavDirections) {
//       Уже есть аккуунт? Войдите
        val register = SpannableString(getString(ru.prsolution.winstrike.R.string.fmt_register_message_enter))
        val registerClick = object : ClickableSpan() {
            override fun onClick(v: View) {
//                val action = RegisterFragmentDirections.actionToNavigationLogin()
                navigate(action)
            }
        }
        register.setSpan(registerClick, 18, register.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView.movementMethod = LinkMovementMethod.getInstance()
        textView.text = register
    }


}
