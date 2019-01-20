package ru.prsolution.winstrike.ui.main

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.support.constraint.ConstraintSet
import android.support.transition.TransitionManager
import android.support.v4.app.ActivityCompat.invalidateOptionsMenu
import android.support.v4.app.ShareCompat
import android.support.v4.content.ContextCompat.startActivity
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.TextView
import android.widget.Toast
import butterknife.ButterKnife
import com.arellomobile.mvp.MvpAppCompatActivity
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem
import ru.prsolution.winstrike.R
import ru.prsolution.winstrike.WinstrikeApp
import ru.prsolution.winstrike.common.BackButtonListener
import ru.prsolution.winstrike.common.CarouselAdapter
import ru.prsolution.winstrike.common.ScreenType
import ru.prsolution.winstrike.common.utils.AuthUtils
import ru.prsolution.winstrike.common.vpadapter.BaseViewPagerAdapter
import ru.prsolution.winstrike.databinding.AcMainscreenBinding
import ru.prsolution.winstrike.mvp.apimodels.OrderModel
import ru.prsolution.winstrike.mvp.apimodels.Room
import ru.prsolution.winstrike.mvp.models.*
import ru.prsolution.winstrike.mvp.views.MainScreenView
import ru.prsolution.winstrike.networking.RetrofitFactory
import ru.prsolution.winstrike.ui.Screens
import ru.prsolution.winstrike.ui.login.SignInActivity
import ru.prsolution.winstrike.ui.main.AppFragment.OnAppButtonsClickListener
import ru.prsolution.winstrike.ui.main.ArenaSelectAdapter.OnItemArenaClickListener
import ru.prsolution.winstrike.ui.main.CarouselSeatFragment.OnChoosePlaceButtonsClickListener
import ru.prsolution.winstrike.ui.main.ChooseScreenFragment.onMapShowProcess
import ru.prsolution.winstrike.ui.main.ProfileFragment.OnProfileButtonsClickListener
import ru.prsolution.winstrike.ui.splash.SplashActivity
import ru.prsolution.winstrike.utils.Constants
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.commands.Back
import ru.terrakok.cicerone.commands.Command
import ru.terrakok.cicerone.commands.Replace
import ru.terrakok.cicerone.commands.SystemMessage
import timber.log.Timber
import java.util.*
import javax.inject.Inject

/*
  A Big God Activity.
  // TODO: 18/10/2018 Need some refactoring for SOLID principle.
 */
class MainScreenActivity : AppCompatActivity(),
        OnProfileButtonsClickListener,
        OnAppButtonsClickListener,
        OnChoosePlaceButtonsClickListener,
        onMapShowProcess,
        OnItemArenaClickListener {

    private var progressDialog: ProgressDialog? = null
    private var bottomNavigationBar: AHBottomNavigation? = null
    private var homeTabFragment: MainContainerFragment? = null
    private var placesTabFragment: MainContainerFragment? = null
    private var userTabFragment: MainContainerFragment? = null
    private var chooseTabFragment: MainContainerFragment? = null
    private var mapTabFragment: MainContainerFragment? = null
    private var payTabFragment: MainContainerFragment? = null
    private var viewPagerSeat: ViewPager? = null
    private var adapter: CarouselAdapter? = null
    var orders = ArrayList<OrderModel>()
        private set
    private val HIDE_ICON = true
    private val SHOW_ICON = false
    private var mDlgSingOut: Dialog? = null
    private var mMainOnClickListener: MainOnClickListener? = null
    private var mMapOnClickListener: MapOnClickListener? = null
    private var mScreenType: ScreenType? = null
    private var mDlgMapLegend: Dialog? = null
    private val user = UserProfileObservable()
    private var binding: AcMainscreenBinding? = null
    private val arenaItems = ArrayList<ArenaItem>()
    private val arenaUpConstraintSet = ConstraintSet()
    private val arenaDownConstraintSet = ConstraintSet()
    private lateinit var sharedPref: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    var selectedArena = 0
    private var isArenaShow: Boolean = false
    var rooms: List<Room>? = null


//    @InjectPresenter

//    @Inject

//    @Inject
//    var router: Router? = null

/*    @Inject
    var navigatorHolder: NavigatorHolder? = null*/

//    private val navigator = object : Navigator {
////        override fun applyCommands(commands: Array<Command>) {
////            for (command in commands) {
////                applyCommand(command)
////            }
////        }
//
////        private fun applyCommand(command: Command) {
////            if (command is Back) {
////                finish()
////            } else if (command is SystemMessage) {
////                Toast.makeText(this@MainScreenActivity, command.message, Toast.LENGTH_SHORT).show()
////            } else if (command is Replace) {
////                val fm = supportFragmentManager
////
////                when (command.screenKey) {
////                    Screens.START_SCREEN -> {
////                        setHomeScreenStateVisibility(true)
////                        fm.beginTransaction()
////                                .detach(userTabFragment!!)
////                                .detach(placesTabFragment!!)
////                                .detach(chooseTabFragment!!)
////                                .detach(mapTabFragment!!)
////                                .detach(payTabFragment!!)
////                                .attach(homeTabFragment!!)
////                                .commitNow()
////                    }
////                    Screens.PLACE_SCREEN -> {
////                        setHomeScreenStateVisibility(false)
////                        fm.beginTransaction()
////                                .detach(userTabFragment!!)
////                                .detach(homeTabFragment!!)
////                                .detach(chooseTabFragment!!)
////                                .detach(mapTabFragment!!)
////                                .detach(payTabFragment!!)
////                                .attach(placesTabFragment!!)
////                                .commitNow()
////                    }
////                    Screens.USER_SCREEN -> {
////                        setHomeScreenStateVisibility(false)
////                        fm.beginTransaction()
////                                .detach(homeTabFragment!!)
////                                .detach(placesTabFragment!!)
////                                .detach(chooseTabFragment!!)
////                                .detach(mapTabFragment!!)
////                                .detach(payTabFragment!!)
////                                .attach(userTabFragment!!)
////                                .commitNow()
////                    }
////                    Screens.CHOOSE_SCREEN -> {
////                        user.name = rooms!![selectedArena].name
////                        initMainToolbar(SHOW_ICON, ScreenType.MAIN, mMainOnClickListener)
////                        setHomeScreenStateVisibility(false)
////                        fm.beginTransaction()
////                                .detach(homeTabFragment!!)
////                                .detach(placesTabFragment!!)
////                                .detach(userTabFragment!!)
////                                .detach(mapTabFragment!!)
////                                .detach(payTabFragment!!)
////                                .attach(chooseTabFragment!!)
////                                .commit()
////                        fm.executePendingTransactions()
////                    }
////                    Screens.MAP_SCREEN -> {
////                        user.name = rooms!![selectedArena].name
////                        initMainToolbar(SHOW_ICON, ScreenType.MAIN, mMapOnClickListener)
////                        setHomeScreenStateVisibility(false)
////                        fm.beginTransaction()
////                                .detach(homeTabFragment!!)
////                                .detach(placesTabFragment!!)
////                                .detach(userTabFragment!!)
////                                .detach(chooseTabFragment!!)
////                                .detach(payTabFragment!!)
////                                .attach(mapTabFragment!!)
////                                .commit()
////                        fm.executePendingTransactions()
////                    }
////                    Screens.PAY_SCREEN -> {
////                        user.name = getString(R.string.title_payment)
////                        initMainToolbar(SHOW_ICON, ScreenType.MAP, mMainOnClickListener)
////                        setHomeScreenStateVisibility(false)
////                        fm.beginTransaction()
////                                .detach(homeTabFragment!!)
////                                .detach(placesTabFragment!!)
////                                .detach(userTabFragment!!)
////                                .detach(chooseTabFragment!!)
////                                .detach(mapTabFragment!!)
////                                .attach(payTabFragment!!)
////                                .commitNow()
////                    }
////                }
////            }
////        }
//    }


    override fun onArenaSelectItem(v: View, layoutPosition: Int) {
        TransitionManager.beginDelayedTransition(binding!!.root)
        this.selectedArena = layoutPosition
        this.isArenaShow = false
        arenaUpConstraintSet.applyTo(binding!!.root)
        editor.putInt(getString(R.string.saved_arena), layoutPosition)
        editor.commit()

        ArenaSelectAdapter.SELECTED_ITEM = layoutPosition
        binding!!.tvArenaTitle.text = rooms!![selectedArena].name

        initArena()

        binding!!.rvArena.adapter!!.notifyDataSetChanged()
    }

    private fun initCarouselArenaSeat() {

        val widthPx = WinstrikeApp.getInstance().displayWidhtPx

        if (!TextUtils.isEmpty(this.rooms!![selectedArena].usualDescription) && !TextUtils
                        .isEmpty(this.rooms!![selectedArena].vipDescription)) {
            adapter!!.setPagesCount(2)
            adapter!!.addFragment(CarouselSeatFragment.newInstance(this, 0), 0, "Общий зал")
            adapter!!.addFragment(CarouselSeatFragment.newInstance(this, 1), 1, "Vip зал")
        } else {
            adapter!!.setPagesCount(1)
            adapter!!.addFragment(CarouselSeatFragment.newInstance(this, 0), 0, "Общий зал")
        }
        adapter!!.notifyDataSetChanged()
        viewPagerSeat!!.adapter = adapter
        viewPagerSeat!!.setPageTransformer(false, adapter)
        viewPagerSeat!!.currentItem = 0
        viewPagerSeat!!.offscreenPageLimit = 2

        if (widthPx <= Constants.SCREEN_WIDTH_PX_720) {
            viewPagerSeat!!.pageMargin = Constants.SCREEN_MARGIN_350
        } else if (widthPx <= Constants.SCREEN_WIDTH_PX_1080) {
            viewPagerSeat!!.pageMargin = Constants.SCREEN_MARGIN_450
        } else if (widthPx <= Constants.SCREEN_WIDTH_PX_1440) {
            viewPagerSeat!!.pageMargin = Constants.SCREEN_MARGIN_600
        } else {
            viewPagerSeat!!.pageMargin = Constants.SCREEN_MARGIN_450
        }
    }

    override fun onChooseArenaSeatClick(seat: SeatModel) {
        TimeDataModel.clearPids()
        showFragmentHolderContainer(true)
        WinstrikeApp.getInstance().seat = seat
        presenter!!.onChooseScreenClick()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (this.mScreenType === ScreenType.MAIN) {
            menuInflater.inflate(R.menu.main_toolbar_menu, menu)
        }
        if (this.mScreenType === ScreenType.PROFILE) {
            menuInflater.inflate(R.menu.prof_toolbar_menu, menu)
        }
        if (this.mScreenType === ScreenType.MAP) {
            menuInflater.inflate(R.menu.map_toolbar_menu, menu)
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (this.mScreenType === ScreenType.PROFILE) {
            mDlgSingOut!!.show()
        }
        if (this.mScreenType === ScreenType.MAP) {
            mDlgMapLegend!!.show()
        }
        return super.onOptionsItemSelected(item)
    }

    fun showWait() {}

    fun removeWait() {}

    override fun onStart() {
        super.onStart()
        AuthUtils.isLogout = false
    }

    override fun onPause() {
//        navigatorHolder!!.removeNavigator()
        super.onPause()
    }


    override fun onDestroy() {
        super.onDestroy()
        if (mDlgSingOut != null) {
            mDlgSingOut!!.dismiss()
        }
        if (progressDialog != null) {
            progressDialog!!.dismiss()
        }
        this.mMainOnClickListener = null
        this.mMapOnClickListener = null
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (fragment != null
                && fragment is BackButtonListener
                && (fragment as BackButtonListener).onBackPressed()) {
            return
        } else {
            presenter!!.onBackPressed()
        }
    }


    private lateinit var presenter: MainScreenPresenter

    public override fun onCreate(savedInstanceState: Bundle?) {
        var service = RetrofitFactory.makeRetrofitService()
        presenter = MainScreenPresenter(service)
//        WinstrikeApp.INSTANCE.appComponent.inject(this)
        super.onCreate(savedInstanceState)
        clearData()
        binding = DataBindingUtil.setContentView(this, R.layout.ac_mainscreen)

        this.rooms = WinstrikeApp.getInstance().rooms


        if (this.rooms == null) {
            val intent = Intent(this, SignInActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        } else {

            val arenaTitle = ArrayList<String>()
            val arenaAdress = ArrayList<String>()
            for (room in rooms!!) {
                arenaTitle.add(room.name ?: "No title")
                arenaAdress.add(room.metro ?: "No metro")
            }


            for (i in arenaTitle.indices) {
                arenaItems.add(ArenaItem(arenaTitle[i], arenaAdress[i], false))
            }

            sharedPref = getPreferences(Context.MODE_PRIVATE)
            editor = sharedPref.edit()
            selectedArena = sharedPref.getInt(getString(R.string.saved_arena), Constants.SAVED_ARENA_DEFAULT)

            binding!!.user = user
            binding!!.tvArenaTitle.text = rooms!![selectedArena].name
            ArenaSelectAdapter.SELECTED_ITEM = selectedArena

            //Transitions animations:
            arenaDownConstraintSet.clone(this, R.layout.part_arena_down)
            arenaUpConstraintSet.clone(this, R.layout.part_arena_up)

            binding!!.rvArena.adapter = ArenaSelectAdapter(this, this, arenaItems)
            binding!!.rvArena.layoutManager = LinearLayoutManager(this)
            binding!!.rvArena.addItemDecoration(RecyclerViewMargin(24, 1))
            binding!!.rvArena.bringToFront()
            binding!!.rvArena.adapter!!.notifyDataSetChanged()

            ButterKnife.bind(this)

            viewPagerSeat = findViewById(R.id.view_pager_seat)
            adapter = CarouselAdapter(this)
            initArena()
            Timber.w(adapter!!.count.toString())

            progressDialog = ProgressDialog(this)

            mMainOnClickListener = MainOnClickListener()

            mMapOnClickListener = MapOnClickListener()

            initViews()
            initFragmentsContainers()

            if (savedInstanceState == null) {
                presenter!!.onCreate()
            }

            val token = Constants.TOKEN_TYPE_BEARER + AuthUtils.token

            val fcmToken = AuthUtils.fcmtoken
            if (!fcmToken.isEmpty()) {
                sendRegistrationToServer(token, fcmToken)
            }

            // Arena select:
            binding!!.arrowArenaDown.setOnClickListener { v ->
                TransitionManager.beginDelayedTransition(binding!!.root)
                if (!isArenaShow) {
                    isArenaShow = true
                    arenaDownConstraintSet.applyTo(binding!!.root)
                } else {
                    arenaUpConstraintSet.applyTo(binding!!.root)
                    isArenaShow = false
                }
            }

            binding!!.arrowArenaUp.setOnClickListener { v ->
                TransitionManager.beginDelayedTransition(binding!!.root)
                arenaUpConstraintSet.applyTo(binding!!.root)
            }
        }
    }

    private fun initArena() {
        // TODO: 20/10/2018 Get arena name from Api.
        val uri: Uri
        if (rooms!![selectedArena].imageUrl != null) {
            uri = Uri.parse(rooms!![selectedArena].imageUrl)
            binding!!.headImage.setImageURI(uri)
        }
        if (rooms!![selectedArena].name != null) {
            binding!!.user?.name = rooms!![selectedArena].name
        }
        if (rooms!![selectedArena].description != null) {

            binding!!.arenaDescription.text = rooms!![selectedArena].description
        }
        initCarouselArenaSeat()
    }

    private fun initViews() {
        initBottomNavigationBar()

        // Hide profile interface element
        setProfileScreenVisibility(false)

        dlgMapLegend()
        dlgProfileSingOut()
    }

    fun initMainToolbar(hideNavIcon: Boolean?, screenType: ScreenType, listener: View.OnClickListener?) {
        setSupportActionBar(binding!!.toolbar)
        binding!!.toolbar.setNavigationOnClickListener(listener)

        mScreenType = screenType
        invalidateOptionsMenu() // now onCreateOptionsMenu(...) is called again
        binding!!.toolbar.setNavigationIcon(R.drawable.ic_back_arrow)
        if (hideNavIcon!!) {
            binding!!.toolbar.navigationIcon = null
            binding!!.toolbar.setContentInsetsAbsolute(0, binding!!.toolbar.contentInsetStart)
        } else {
            binding!!.toolbar.setNavigationIcon(R.drawable.ic_back_arrow)
            binding!!.toolbar.setContentInsetsAbsolute(0, binding!!.toolbar.contentInsetStartWithNavigation)
        }
        supportActionBar!!.setDisplayShowTitleEnabled(false)

    }


    private fun setHomeScreenStateVisibility(isVisible: Boolean) {
        if (isVisible) {
            setArenaVisibility(true)
            viewPagerSeat!!.visibility = VISIBLE
            binding!!.seatCat.visibility = VISIBLE
        } else {
            setArenaVisibility(false)
            binding!!.seatCat.visibility = GONE
            viewPagerSeat!!.visibility = GONE
        }
    }

    private fun initFragmentsContainers() {
        val fm = supportFragmentManager
        homeTabFragment = fm.findFragmentByTag(getString(R.string.tag_main)) as MainContainerFragment?
        if (homeTabFragment == null) {
            homeTabFragment = MainContainerFragment.getNewInstance(getString(R.string.tag_main))
            fm.beginTransaction()
                    .add(R.id.fragment_container, homeTabFragment!!, getString(R.string.tag_main))
                    .detach(homeTabFragment!!).commitNow()
            fm.executePendingTransactions()
        }

        placesTabFragment = fm.findFragmentByTag(getString(R.string.tag_places)) as MainContainerFragment?
        if (placesTabFragment == null) {
            placesTabFragment = MainContainerFragment.getNewInstance(getString(R.string.tag_places))
            fm.beginTransaction()
                    .add(R.id.fragment_container, placesTabFragment!!, getString(R.string.tag_places))
                    .detach(placesTabFragment!!).commitNow()
            fm.executePendingTransactions()
        }

        userTabFragment = fm.findFragmentByTag(getString(R.string.tag_user)) as MainContainerFragment?
        if (userTabFragment == null) {
            userTabFragment = MainContainerFragment.getNewInstance(getString(R.string.tag_user))
            fm.beginTransaction()
                    .add(R.id.fragment_container, userTabFragment!!, getString(R.string.tag_user))
                    .detach(userTabFragment!!).commitNow()
            fm.executePendingTransactions()
        }

        chooseTabFragment = fm.findFragmentByTag(getString(R.string.tag_choose)) as MainContainerFragment?
        if (chooseTabFragment == null) {
            chooseTabFragment = MainContainerFragment.getNewInstance(getString(R.string.tag_choose))
            fm.beginTransaction()
                    .add(R.id.fragment_container, chooseTabFragment!!, getString(R.string.tag_choose))
                    .detach(chooseTabFragment!!).commitNow()
            fm.executePendingTransactions()
        }

        mapTabFragment = fm.findFragmentByTag(getString(R.string.tag_map)) as MainContainerFragment?
        if (mapTabFragment == null) {
            mapTabFragment = MainContainerFragment.getNewInstance(getString(R.string.tag_map))
            fm.beginTransaction()
                    .add(R.id.fragment_container, mapTabFragment!!, getString(R.string.tag_map))
                    .detach(mapTabFragment!!).commitNow()
            fm.executePendingTransactions()
        }

        payTabFragment = fm.findFragmentByTag(getString(R.string.tag_pay)) as MainContainerFragment?
        if (payTabFragment == null) {
            payTabFragment = MainContainerFragment.getNewInstance(getString(R.string.tag_pay))
            fm.beginTransaction()
                    .add(R.id.fragment_container, payTabFragment!!, getString(R.string.tag_pay))
                    .detach(payTabFragment!!).commitNow()
            fm.executePendingTransactions()
        }
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
//        navigatorHolder!!.setNavigator(navigator)
    }

    fun goHome() {
        startActivity(Intent(applicationContext, MainScreenActivity::class.java))
    }

    // User profile actions:
    fun setProfileScreenVisibility(isVisible: Boolean?) {
        if (isVisible!!) {
            setArenaVisibility(false)
            binding!!.tabLayoutProfile.visibility = VISIBLE
            binding!!.viewPagerProfile.visibility = VISIBLE
            setupProfileViewPager(binding!!.viewPagerProfile)
            binding!!.tabLayoutProfile.setupWithViewPager(binding!!.viewPagerProfile)
        } else {
            setArenaVisibility(true)
            binding!!.tabLayoutProfile.visibility = GONE
            binding!!.viewPagerProfile.visibility = GONE
        }
    }

    private fun setArenaVisibility(isVisible: Boolean?) {
        if ((!isVisible!!)) {
            binding!!.vSpinner.visibility = GONE
            binding!!.rvArena.visibility = GONE
            binding!!.arrowArenaUp.visibility = GONE
            binding!!.arrowArenaDown.visibility = GONE
            binding!!.arenaDescription.visibility = GONE
            binding!!.headImage.visibility = GONE
        } else {
            binding!!.headImage.visibility = VISIBLE
            binding!!.arenaDescription.visibility = VISIBLE
            binding!!.vSpinner.visibility = VISIBLE
            binding!!.arrowArenaUp.visibility = GONE
            binding!!.arrowArenaDown.visibility = VISIBLE
        }
    }

    private fun setupProfileViewPager(viewPager: ViewPager) {
        val pagerAdapter = BaseViewPagerAdapter(supportFragmentManager)
        pagerAdapter.addFragments(ProfileFragment.newInstance(), getString(R.string.title_profile))
        pagerAdapter.addFragments(AppFragment.newInstance(), getString(R.string.title_app))
        viewPager.adapter = pagerAdapter
    }

    override fun onProfileUpdate(name: String, password: String) {
        if (password.isEmpty()) {
            Toast.makeText(this, R.string.message_password, Toast.LENGTH_LONG).show()
        }
        if (name.isEmpty()) {
            Toast.makeText(this, R.string.message_username, Toast.LENGTH_LONG).show()
        }
        if (!TextUtils.isEmpty(password) && !TextUtils.isEmpty(name)) {
            // call api for update profile here ...
            val profile = ProfileModel()
            profile.name = name
            profile.password = password
            val publicId = AuthUtils.publicid
            val token = Constants.TOKEN_TYPE_BEARER + AuthUtils.token
            presenter!!.updateProfile(token, profile, publicId)
            AuthUtils.name = name
            var title = getString(R.string.title_settings)
            if (AuthUtils.name != null) {
                if (!TextUtils.isEmpty(AuthUtils.name)) {
                    title = AuthUtils.name
                }
            }
            user.name = name
            initMainToolbar(SHOW_ICON, ScreenType.PROFILE, mMainOnClickListener)
        }
    }

    fun onFailureUpdateProfile(appErrorMessage: String) {
        Toast.makeText(this, R.string.message_on_failure_profile_update, Toast.LENGTH_LONG).show()
    }

    fun onProfileUpdateSuccessfully(authResponse: MessageResponse) {
        Toast.makeText(this, R.string.message_on_success_profile_update, Toast.LENGTH_LONG).show()
    }

    private fun dlgProfileSingOut() {
        mDlgSingOut = Dialog(this, android.R.style.Theme_Dialog)
        mDlgSingOut!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        mDlgSingOut!!.setContentView(R.layout.dlg_logout)

        val cvBtnOk = mDlgSingOut!!.findViewById<TextView>(R.id.btn_ok)
        val cvCancel = mDlgSingOut!!.findViewById<TextView>(R.id.btn_cancel)

        cvCancel.setOnClickListener { it -> mDlgSingOut!!.dismiss() }

        cvBtnOk.setOnClickListener { it ->
            AuthUtils.isLogout = true
            AuthUtils.token = ""
            startActivity(Intent(this@MainScreenActivity, SplashActivity::class.java))
        }

        mDlgSingOut!!.setCanceledOnTouchOutside(true)
        mDlgSingOut!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        mDlgSingOut!!.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        val window = mDlgSingOut!!.window
        val wlp = window!!.attributes

        wlp.gravity = Gravity.CENTER
        wlp.flags = wlp.flags and WindowManager.LayoutParams.FLAG_DIM_BEHIND.inv()
        window.attributes = wlp

        mDlgSingOut!!.window!!.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        mDlgSingOut!!.window!!.setDimAmount(0.5f)

        mDlgSingOut!!.setCanceledOnTouchOutside(false)
        mDlgSingOut!!.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        mDlgSingOut!!.dismiss()

    }


    // Social networks links block:
    override fun onGooglePlayButtonClick() {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(Constants.URL_GOOGLE_PLAY)))
    }

    override fun onVkClick() {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(Constants.URL_VK)))
    }

    override fun onInstagramClick() {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(Constants.URL_INSTAGRAM)))
    }

    override fun onTweeterClick() {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(Constants.URL_TWEETER)))
    }

    override fun onFacebookClick() {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(Constants.URL_FACEBOOK)))
    }

    override fun onTwitchClick() {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(Constants.URL_TWITCH)))
    }

    override fun onRecommendButtonClick() {
        shareImgOnRecommendClick()
    }

    private fun shareImgOnRecommendClick() {
        val attached_Uri = Uri.parse(Constants.ANDROID_RESOURCES_PATH + packageName
                + Constants.SHARE_DRAWABLE + Constants.SHARE_IMG)
        val shareIntent = ShareCompat.IntentBuilder.from(this)
                .setType(Constants.IMAGE_TYPE)
                .setStream(attached_Uri)
                .intent
        shareIntent.putExtra(Intent.EXTRA_TEXT, R.string.message_share_images)
        startActivity(Intent.createChooser(shareIntent, Constants.SHARE_IMG_TITLE))
    }

    // Map actions block:
    private fun dlgMapLegend() {
        mDlgMapLegend = Dialog(this, android.R.style.Theme_Dialog)
        mDlgMapLegend!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        mDlgMapLegend!!.setContentView(R.layout.dlg_legend)
        val tvSee = mDlgMapLegend!!.findViewById<TextView>(R.id.tv_see)

        tvSee.setOnClickListener { it -> mDlgMapLegend!!.dismiss() }

        mDlgMapLegend!!.setCanceledOnTouchOutside(true)
        mDlgMapLegend!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        mDlgMapLegend!!.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val window = mDlgMapLegend!!.window
        val wlp = window!!.attributes

        wlp.gravity = Gravity.TOP
        wlp.flags = wlp.flags and WindowManager.LayoutParams.FLAG_DIM_BEHIND.inv()
        wlp.y = Constants.LEGEND_MAP_TOP_MARGIN
        window.attributes = wlp

        mDlgMapLegend!!.setCanceledOnTouchOutside(false)
        mDlgMapLegend!!.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        mDlgMapLegend!!.dismiss()

    }

    override fun onMapShow() {
        presenter!!.onMapShowClick()
    }

    private fun clearData() {
        TimeDataModel.clearPids()
        TimeDataModel.clearDateTime()
    }

    private fun showFragmentHolderContainer(isVisible: Boolean) {
        if (isVisible) {
            binding!!.fragmentContainer.visibility = VISIBLE
        } else {
            binding!!.fragmentContainer.visibility = GONE
        }
    }

     fun onGetOrdersSuccess(orders: ArrayList<OrderModel>) {
        this.orders = orders
        presenter!!.onTabPlaceClick(this.orders)
        Timber.d("UserEntity order list size: %s", this.orders.size)
    }

     fun onGetOrdersFailure(appErrorMessage: String) {
        orders = ArrayList()
        presenter!!.onTabPlaceClick(orders)
        Timber.d("Failure get layout from server: %s", appErrorMessage)
    }

    // Bottom navigation menu:
    private fun initBottomNavigationBar() {
        val bottomNavigationListener = BottomNavigationListener()
        // Create items
        val itHome = AHBottomNavigationItem(null, R.drawable.ic_home)
        val itPlaces = AHBottomNavigationItem(null, R.drawable.ic_money)
        val itProfile = AHBottomNavigationItem(null, R.drawable.ic_user)

        // Add items
        bottomNavigationBar = findViewById(R.id.ab_bottom_navigation_bar)
        bottomNavigationBar!!.addItem(itHome)
        bottomNavigationBar!!.addItem(itPlaces)
        bottomNavigationBar!!.addItem(itProfile)

        bottomNavigationBar!!.titleState = AHBottomNavigation.TitleState.ALWAYS_HIDE
        bottomNavigationBar!!.accentColor = R.color.color_accent
        bottomNavigationBar!!.setOnTabSelectedListener(bottomNavigationListener)
    }

    inner class BottomNavigationListener : AHBottomNavigation.OnTabSelectedListener {

        override fun onTabSelected(position: Int, wasSelected: Boolean): Boolean {
            when (position) {
                MainScreenView.HOME_TAB_POSITION -> {
                    showFragmentHolderContainer(false)
                    setHomeScreenStateVisibility(true)
                    setProfileScreenVisibility(false)
                    user.name = rooms!![selectedArena].name
                    initMainToolbar(HIDE_ICON, ScreenType.MAIN, mMainOnClickListener)
                    presenter!!.onTabHomeClick()
                }
                MainScreenView.PLACE_TAB_POSITION -> {
                    showFragmentHolderContainer(true)
                    setProfileScreenVisibility(false)
                    //          setHomeScreenStateVisibility(false);
                    user.name = getString(R.string.title_payment_places)
                    initMainToolbar(SHOW_ICON, ScreenType.MAIN, mMainOnClickListener)
                    val token = Constants.TOKEN_TYPE_BEARER + AuthUtils.token
                    presenter!!.getOrders(token)
                }
                MainScreenView.USER_TAB_POSITION -> {
                    showFragmentHolderContainer(false)
                    setProfileScreenVisibility(true)
                    binding!!.toolbar.navigationIcon = null
                    user.name = AuthUtils.name
                    initMainToolbar(SHOW_ICON, ScreenType.PROFILE, mMainOnClickListener)
                    presenter!!.onTabUserClick()
                }
            }
            return true
        }
    }

     fun highlightTab(position: Int) {
        bottomNavigationBar!!.setCurrentItem(position, false)
    }

     fun hideBottomTab() {
        bottomNavigationBar!!.visibility = GONE
    }

     fun showBottomTab() {
        bottomNavigationBar!!.visibility = VISIBLE
    }

    private inner class MainOnClickListener : View.OnClickListener {

        override fun onClick(v: View) {
            clearData()
            user.name = rooms!![selectedArena].name
            initMainToolbar(HIDE_ICON, ScreenType.MAIN, this)
            presenter!!.onBackPressed()
        }
    }

    private inner class MapOnClickListener : View.OnClickListener {

        // TODO: implement navigation
        override fun onClick(v: View) {
            TimeDataModel.clearPids()
//            router!!.replaceScreen(Screens.CHOOSE_SCREEN, 0)
        }
    }

    //FCM push message services:
    fun sendRegistrationToServer(authToken: String, refreshedToken: String) {
        val fcmModel = FCMModel()
        fcmModel.token = refreshedToken
        presenter!!.sendFCMTokenToServer(authToken, fcmModel)
    }

    override fun onPushClick(isOn: String) {
        Toast.makeText(this, "Push is: $isOn", Toast.LENGTH_LONG).show()
    }

}
