package ru.prsolution.winstrike.ui.main;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.Space;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationViewPager;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.prsolution.winstrike.R;
import ru.prsolution.winstrike.WinstrikeApp;
import ru.prsolution.winstrike.common.entity.SeatModel;
import ru.prsolution.winstrike.common.fragment.AppFragment;
import ru.prsolution.winstrike.common.fragment.CarouselSeatFragment;
import ru.prsolution.winstrike.common.fragment.ProfileFragment;
import ru.prsolution.winstrike.common.logging.MessageResponse;
import ru.prsolution.winstrike.common.logging.ProfileModel;
import ru.prsolution.winstrike.common.rvadapter.PayAdapter;
import ru.prsolution.winstrike.common.rvlistener.OnItemPayClickListener;
import ru.prsolution.winstrike.common.vpadapter.BaseViewPagerAdapter;
import ru.prsolution.winstrike.db.UserViewModel;
import ru.prsolution.winstrike.mvp.apimodels.OrderModel;
import ru.prsolution.winstrike.mvp.common.AuthUtils;
import ru.prsolution.winstrike.mvp.presenters.MainScreenPresenter;
import ru.prsolution.winstrike.mvp.views.MainScreenView;
import ru.prsolution.winstrike.networking.NetworkError;
import ru.prsolution.winstrike.networking.Service;
import ru.prsolution.winstrike.ui.Screens;
import ru.prsolution.winstrike.ui.common.BackButtonListener;
import ru.prsolution.winstrike.ui.common.CarouselAdapter;
import ru.prsolution.winstrike.ui.common.MapInfoSingleton;
import ru.prsolution.winstrike.ui.common.RouterProvider;
import ru.prsolution.winstrike.ui.common.ScreenType;
import ru.prsolution.winstrike.ui.start.SplashActivity;
import ru.terrakok.cicerone.Navigator;
import ru.terrakok.cicerone.NavigatorHolder;
import ru.terrakok.cicerone.Router;
import ru.terrakok.cicerone.commands.Back;
import ru.terrakok.cicerone.commands.Command;
import ru.terrakok.cicerone.commands.Replace;
import ru.terrakok.cicerone.commands.SystemMessage;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;


public class MainScreenActivity extends MvpAppCompatActivity implements MainScreenView, RouterProvider, ProfileFragment.OnProfileButtonsClickListener,
        AppFragment.OnAppButtonsClickListener, CarouselSeatFragment.OnChoosePlaceButtonsClickListener
        , OnItemPayClickListener, ChooseScreenFragment.onMapShowProcess {

    private ProgressDialog progressDialog;
    private AHBottomNavigation bottomNavigationBar;
    private BaseViewPagerAdapter pagerAdapter;
    private MainContainerFragment homeTabFragment;
    private MainContainerFragment placesTabFragment;
    private MainContainerFragment userTabFragment;
    private MainContainerFragment chooseTabFragment;
    private MainContainerFragment mapTabFragment;
    private MainContainerFragment payTabFragment;
    private ViewPager viewPagerSeat;
    private CarouselAdapter adapter;
    private BottomNavigationListener bottomNavigationListener;
    float dpHeight, dpWidth;
    private ArrayList<OrderModel> mPayList = new ArrayList<>();
    private final Boolean HIDE_MENU = true;
    private final Boolean SHOW_MENU = false;
    private final Boolean HIDE_ICON = true;
    private final Boolean SHOW_ICON = false;
    private Dialog mDlgSingOut;
    private MainOnClickListener mMainOnClickListener;
    private MapOnClickListener mMapOnClickListener;
    private ScreenType mScreenType;
    private Dialog mDlgMapLegend;


    @BindView(R.id.toolbar_text)
    TextView tvToolbarTitle;

    @BindView(R.id.ab_container)
    RelativeLayout flFragmentContainer;

    @BindView(R.id.head_image)
    ImageView ivHeadImage;

    @BindView(R.id.seat_title)
    TextView tvCaruselTitle;

    @BindView(R.id.description)
    TextView tvCarouselDescription;

    @BindView(R.id.category)
    TextView tvCarouselTitleCategory;

    @BindView(R.id.spacer)
    Space spSpace;

    @BindView(R.id.main_toolbar)
    Toolbar toolbar;

    @Nullable
    @BindView(R.id.tv_title)
    TextView tvToolbarHead;

    @InjectPresenter
    MainScreenPresenter presenter;

    @BindView(R.id.tablayout)
    TabLayout tabLayout;

    @BindView(R.id.viewpager)
    AHBottomNavigationViewPager viewPager;


    @Inject
    public Service service;

    @Inject
    Router router;

    @Inject
    NavigatorHolder navigatorHolder;
    private UserViewModel mUserViewModel;
    private CompositeSubscription subscriptions;

    public Service getService() {
        return service;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (this.mScreenType == ScreenType.MAIN) {
            getMenuInflater().inflate(R.menu.main_toolbar_menu, menu);
        }
        if (this.mScreenType == ScreenType.PROFILE) {
            getMenuInflater().inflate(R.menu.prof_toolbar_menu, menu);
        }
        if (this.mScreenType == ScreenType.MAP) {
            getMenuInflater().inflate(R.menu.map_toolbar_menu, menu);
        }

        this.subscriptions = new CompositeSubscription();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (this.mScreenType == ScreenType.PROFILE) {
            mDlgSingOut.show();
        }
        if (this.mScreenType == ScreenType.MAP) {
            mDlgMapLegend.show();
        }
        return super.onOptionsItemSelected(item);
    }

    @ProvidePresenter
    public MainScreenPresenter createBottomNavigationPresenter() {
        return new MainScreenPresenter(service, router);
    }


    private void dlgSingOut() {
        mDlgSingOut = new Dialog(this, android.R.style.Theme_Dialog);
        mDlgSingOut.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDlgSingOut.setContentView(R.layout.dlg_logout);

        TextView cvBtnOk = mDlgSingOut.findViewById(R.id.btn_ok);
        TextView cvCancel = mDlgSingOut.findViewById(R.id.btn_cancel);

        cvCancel.setOnClickListener(
                it -> mDlgSingOut.dismiss()
        );

        cvBtnOk.setOnClickListener(
                it -> {
                    mUserViewModel.delete();
                    AuthUtils.INSTANCE.setToken("");
                    AuthUtils.INSTANCE.setLogout(true);
                    startActivity(new Intent(MainScreenActivity.this, SplashActivity.class));
                }
        );

        mDlgSingOut.setCanceledOnTouchOutside(true);
        mDlgSingOut.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDlgSingOut.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        Window window = mDlgSingOut.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);

        mDlgSingOut.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        mDlgSingOut.getWindow().setDimAmount(0.5f);

        mDlgSingOut.setCanceledOnTouchOutside(false);
        mDlgSingOut.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        mDlgSingOut.dismiss();

    }

    private void dlgLegend() {
        mDlgMapLegend = new Dialog(this, android.R.style.Theme_Dialog);
        mDlgMapLegend.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDlgMapLegend.setContentView(R.layout.dlg_legend);
        TextView tvSee = mDlgMapLegend.findViewById(R.id.tv_see);

        tvSee.setOnClickListener(
                it -> mDlgMapLegend.dismiss()
        );


        mDlgMapLegend.setCanceledOnTouchOutside(true);
        mDlgMapLegend.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDlgMapLegend.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Window window = mDlgMapLegend.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.TOP;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        wlp.y = 200;
        window.setAttributes(wlp);

        mDlgMapLegend.setCanceledOnTouchOutside(false);
        mDlgMapLegend.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        mDlgMapLegend.dismiss();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDlgSingOut != null) {
            mDlgSingOut.dismiss();
        }
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        WinstrikeApp.INSTANCE.getAppComponent().inject(this);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ac_mainscreen);
        ButterKnife.bind(this);

        viewPagerSeat = findViewById(R.id.view_pager_seat);
        adapter = new CarouselAdapter(this);
        Timber.w(String.valueOf(adapter.getCount()));
        initCarouselSeat(1);

        progressDialog = new ProgressDialog(this);

        mMainOnClickListener = new MainOnClickListener();

        mMapOnClickListener = new MapOnClickListener();

        initViews();
        initContainers();


        if (savedInstanceState == null) {
            presenter.onCreate();
        }

        mUserViewModel = ViewModelProviders.of(this).get(UserViewModel.class);

        // TODO: 29/04/2018 REMOVE AFTER TEST!!!

        String token = "Bearer " + AuthUtils.INSTANCE.getToken();
        presenter.getOrders(token);


    }

    private void initCarouselSeat(int currentItem) {

        dpWidth = WinstrikeApp.getInstance().getDisplayWidhtDp();
        dpHeight = WinstrikeApp.getInstance().getDisplayHeightDp();

        Timber.d("heigth: %s", dpHeight);
        Timber.d("width: %s", dpWidth);

        adapter.addFragment(CarouselSeatFragment.newInstance(this, 0), 0);
        adapter.addFragment(CarouselSeatFragment.newInstance(this, 1), 1);
        adapter.notifyDataSetChanged();
        viewPagerSeat.setAdapter(adapter);
        viewPagerSeat.setPageTransformer(false, adapter);
        viewPagerSeat.setCurrentItem(currentItem);
        viewPagerSeat.setOffscreenPageLimit(2);

        Timber.tag("display").d("Display dpWidth: %s", dpWidth);

        if (dpWidth <= 360) {
            viewPagerSeat.setPageMargin(-500);
        } else {
            viewPagerSeat.setPageMargin(-600);
        }

    }


    private void setupViewPager(ViewPager viewPager) {
        pagerAdapter = new BaseViewPagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragments(ProfileFragment.newInstance(), "Профиль");
        pagerAdapter.addFragments(AppFragment.newInstance(), "Приложение");
        viewPager.setAdapter(pagerAdapter);
    }


    private void initViews() {
        initMainToolbar(HIDE_MENU, "Winstrike Arena", HIDE_ICON, ScreenType.MAIN);
        initBottomNavigationBar();

        // Hide profile interface element
        setProfileScreenInterfaceVisibility(false);

        // TODO: 08/05/2018 REMOVE AFTER TESTS!!!
        // presenter.onChooseScreenClick();
        dlgLegend();
        dlgSingOut();
    }

    private void initBottomNavigationBar() {
        bottomNavigationListener = new BottomNavigationListener();
        // Create items
        AHBottomNavigationItem item1 = new AHBottomNavigationItem(null, R.drawable.ic_home);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(null, R.drawable.ic_place);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(null, R.drawable.ic_user);

        // Add items
        bottomNavigationBar = findViewById(R.id.ab_bottom_navigation_bar);
        bottomNavigationBar.addItem(item1);
        bottomNavigationBar.addItem(item2);
        bottomNavigationBar.addItem(item3);

        bottomNavigationBar.setTitleState(AHBottomNavigation.TitleState.ALWAYS_HIDE);
        bottomNavigationBar.setAccentColor(Color.parseColor("#ffc9186c"));
        bottomNavigationBar.setOnTabSelectedListener(bottomNavigationListener);
    }


    @Override
    public void setProfileScreenInterfaceVisibility(Boolean isVisible) {
        if (isVisible) {
            tabLayout.setVisibility(View.VISIBLE);
            viewPager.setVisibility(View.VISIBLE);
            setupViewPager(viewPager);
            tabLayout.setupWithViewPager(viewPager);
        } else {
            tabLayout.setVisibility(View.GONE);
            viewPager.setVisibility(View.GONE);
        }
    }


    public void initMainToolbar(Boolean hide_menu, String title, Boolean hideNavIcon, ScreenType screenType) {
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(mMainOnClickListener);

        mScreenType = screenType;
        // getMenuInflater().inflate(R.menu.main_toolbar_menu, menu);
        invalidateOptionsMenu(); // now onCreateOptionsMenu(...) is called again
        toolbar.setNavigationIcon(R.drawable.back_arrow);
        tvToolbarTitle.setText(title);
        if (hideNavIcon) {
            toolbar.setNavigationIcon(null);
            toolbar.setContentInsetsAbsolute(0, toolbar.getContentInsetStart());
        } else {
            toolbar.setNavigationIcon(R.drawable.back_arrow);
            toolbar.setContentInsetsAbsolute(0, toolbar.getContentInsetStartWithNavigation());
        }
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void setHomeScreenStateVisibily(Boolean isVisible) {
        if (isVisible) {
            tvCaruselTitle.setVisibility(View.VISIBLE);
            ivHeadImage.setVisibility(View.VISIBLE);
            tvCarouselDescription.setVisibility(View.VISIBLE);
            tvCarouselTitleCategory.setVisibility(View.VISIBLE);
            viewPagerSeat.setVisibility(View.VISIBLE);
        } else {
            tvCaruselTitle.setVisibility(View.GONE);
            ivHeadImage.setVisibility(View.GONE);
            tvCarouselDescription.setVisibility(View.GONE);
            tvCarouselTitleCategory.setVisibility(View.GONE);
            viewPagerSeat.setVisibility(View.GONE);
        }
    }

    private void initContainers() {
        FragmentManager fm = getSupportFragmentManager();
        homeTabFragment = (MainContainerFragment) fm.findFragmentByTag("MAIN");
        if (homeTabFragment == null) {
            homeTabFragment = MainContainerFragment.getNewInstance("MAIN");
            fm.beginTransaction()
                    .add(R.id.ab_container, homeTabFragment, "MAIN")
                    .detach(homeTabFragment).commitNow();
            fm.executePendingTransactions();
        }

        placesTabFragment = (MainContainerFragment) fm.findFragmentByTag("PLACES");
        if (placesTabFragment == null) {
            placesTabFragment = MainContainerFragment.getNewInstance("PLACES");
            fm.beginTransaction()
                    .add(R.id.ab_container, placesTabFragment, "PLACES")
                    .detach(placesTabFragment).commitNow();
            fm.executePendingTransactions();
        }

        userTabFragment = (MainContainerFragment) fm.findFragmentByTag("USER");
        if (userTabFragment == null) {
            userTabFragment = MainContainerFragment.getNewInstance("USER");
            fm.beginTransaction()
                    .add(R.id.ab_container, userTabFragment, "USER")
                    .detach(userTabFragment).commitNow();
            fm.executePendingTransactions();
        }

        chooseTabFragment = (MainContainerFragment) fm.findFragmentByTag("CHOOSE");
        if (chooseTabFragment == null) {
            chooseTabFragment = MainContainerFragment.getNewInstance("CHOOSE");
            fm.beginTransaction()
                    .add(R.id.ab_container, chooseTabFragment, "CHOOSE")
                    .detach(chooseTabFragment).commitNow();
            fm.executePendingTransactions();
        }

        mapTabFragment = (MainContainerFragment) fm.findFragmentByTag("MAP");
        if (mapTabFragment == null) {
            mapTabFragment = MainContainerFragment.getNewInstance("MAP");
            fm.beginTransaction()
                    .add(R.id.ab_container, mapTabFragment, "MAP")
                    .detach(mapTabFragment).commitNow();
            fm.executePendingTransactions();
        }

        payTabFragment = (MainContainerFragment) fm.findFragmentByTag("PAY");
        if (payTabFragment == null) {
            payTabFragment = MainContainerFragment.getNewInstance("PAY");
            fm.beginTransaction()
                    .add(R.id.ab_container, payTabFragment, "PAY")
                    .detach(payTabFragment).commitNow();
            fm.executePendingTransactions();
        }

    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        navigatorHolder.setNavigator(navigator);
    }

    @Override
    protected void onPause() {
        navigatorHolder.removeNavigator();
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.ab_container);
        if (fragment != null
                && fragment instanceof BackButtonListener
                && ((BackButtonListener) fragment).onBackPressed()) {
            return;
        } else {
            presenter.onBackPressed();
        }
    }

    private Navigator navigator = new Navigator() {
        @Override
        public void applyCommands(Command[] commands) {
            for (Command command : commands) applyCommand(command);
        }

        private void applyCommand(Command command) {
            if (command instanceof Back) {
                finish();
            } else if (command instanceof SystemMessage) {
                Toast.makeText(MainScreenActivity.this, ((SystemMessage) command).getMessage(), Toast.LENGTH_SHORT).show();
            } else if (command instanceof Replace) {
                FragmentManager fm = getSupportFragmentManager();

                switch (((Replace) command).getScreenKey()) {
                    case Screens.START_SCREEN:
                        setHomeScreenStateVisibily(true);
                        fm.beginTransaction()
                                .detach(userTabFragment)
                                .detach(placesTabFragment)
                                .detach(chooseTabFragment)
                                .detach(mapTabFragment)
                                .detach(payTabFragment)
                                .attach(homeTabFragment)
                                .commitNow();
                        break;
                    case Screens.PLACE_SCREEN:
                        setHomeScreenStateVisibily(false);
                        fm.beginTransaction()
                                .detach(userTabFragment)
                                .detach(homeTabFragment)
                                .detach(chooseTabFragment)
                                .detach(mapTabFragment)
                                .detach(payTabFragment)
                                .attach(placesTabFragment)
                                .commitNow();
                        break;
                    case Screens.USER_SCREEN:
                        setHomeScreenStateVisibily(false);
                        fm.beginTransaction()
                                .detach(homeTabFragment)
                                .detach(placesTabFragment)
                                .detach(chooseTabFragment)
                                .detach(mapTabFragment)
                                .detach(payTabFragment)
                                .attach(userTabFragment)
                                .commitNow();
                        break;
                    case Screens.CHOOSE_SCREEN:
                        toolbar.setNavigationOnClickListener(mMainOnClickListener);
                        initMainToolbar(HIDE_MENU, "Winstrike Arena", SHOW_ICON, ScreenType.MAIN);
                        setHomeScreenStateVisibily(false);
                        fm.beginTransaction()
                                .detach(homeTabFragment)
                                .detach(placesTabFragment)
                                .detach(userTabFragment)
                                .detach(mapTabFragment)
                                .detach(payTabFragment)
                                .attach(chooseTabFragment)
                                .commit();
                        fm.executePendingTransactions();
                        break;
                    case Screens.MAP_SCREEN:
                        toolbar.setNavigationOnClickListener(mMapOnClickListener);
                        initMainToolbar(HIDE_MENU, "Winstrike Arena", SHOW_ICON, ScreenType.MAP);
                        setHomeScreenStateVisibily(false);
                        fm.beginTransaction()
                                .detach(homeTabFragment)
                                .detach(placesTabFragment)
                                .detach(userTabFragment)
                                .detach(chooseTabFragment)
                                .detach(payTabFragment)
                                .attach(mapTabFragment)
                                .commit();
                        fm.executePendingTransactions();
                        break;
                    case Screens.PAY_SCREEN:
                        setHomeScreenStateVisibily(false);
                        fm.beginTransaction()
                                .detach(homeTabFragment)
                                .detach(placesTabFragment)
                                .detach(userTabFragment)
                                .detach(chooseTabFragment)
                                .detach(mapTabFragment)
                                .attach(payTabFragment)
                                .commitNow();
                        break;
                }
            }
        }
    };


    @Override
    public void highlightTab(int position) {
        bottomNavigationBar.setCurrentItem(position, false);
    }

    @Override
    public void hideBottomTab() {
        bottomNavigationBar.setVisibility(View.GONE);
    }

    @Override
    public void showBottomTab() {
        bottomNavigationBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void goHome() {
        startActivity(new Intent(getApplicationContext(), MainScreenActivity.class));
    }

    @Override
    public Router getRouter() {
        return router;
    }


    @Override
    public void onSaveButtonClick(String name, String passw) {
        Timber.d("Update user profile..");
        // call api for update profile here ...
        ProfileModel profile = new ProfileModel();
        profile.setName(name);
        profile.setPassword(passw);
        // TODO: 19/05/2018 Pass real public id here
        String publicId = "60cc441c-9def-41fd-8c31-fa937a80858a";
        String token = "Bearer " + AuthUtils.INSTANCE.getToken();
        updateProfile(token, profile,publicId);
    }

    public void updateProfile(String token, ProfileModel profile, String publicId) {

        Subscription subscription = service.updateUser(new Service.ProfileCallback() {
            @Override
            public void onSuccess(MessageResponse authResponse) {
                onProfileUpdateSuccessfully(authResponse);
            }

            @Override
            public void onError(NetworkError networkError) {
                onFailtureUpdateProfile(networkError.getAppErrorMessage());
            }

        },token, profile, publicId);

        subscriptions.add(subscription);
    }

    private void onFailtureUpdateProfile(String appErrorMessage) {
        Timber.d("Wrong update profile");
/*        switch (appErrorMessage) {
            case "409":
                Timber.tag("OkHttp").d("Не верный код");
                toast("Не верный код");
                break;
            case "403":
                Timber.tag("OkHttp").d("Пользователь уже поддвержден");
                toast("Пользователь уже поддвержден");
                setBtnEnable(nextButtonPhone, false);
                break;
            case "400":
                Timber.tag("OkHttp").d("Пользователь не найден");
                toast("Пользователь с таким номером не найден");
                break;
            case "404":
                Timber.tag("OkHttp").d("Пользователь уже поддвержден");
                toast("Пользователь уже поддвержден");
                break;
            default:
                Timber.tag("OkHttp").d("confirm code: %s", appErrorMessage);
                toast("Пользователь не подтвержден");
                break;
        }*/
    }

    private void onProfileUpdateSuccessfully(MessageResponse authResponse) {
        Timber.d("Falure update profile");
/*        toast("Новый пароль успешно сохранен");
        startActivity(new Intent(this,SignInActivity.class));*/
    }





    @Override
    public void onRecommendButtonClick() {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.putExtra(Intent.EXTRA_TEXT, "https://winstrike.gg");
        // указываем тип передаваемых данных
        sharingIntent.setType("text/plain");
        startActivity(sharingIntent);
    }

    @Override
    public void onGooglePlayButtonClick() {
        String url = "https://winstrike.gg/";
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    @Override
    public void onVkClick() {
        String url = "https://vk.com/winstrikearena";
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    @Override
    public void onInstagramClick() {
        String url = "https://www.instagram.com/winstrikearena/";
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    @Override
    public void onTweeterClick() {
        String url = "https://twitter.com/winstrikearena";
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    @Override
    public void onFacebookClick() {
        String url = "https://www.facebook.com/winstrikegg/";
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    @Override
    public void onTwitchClick() {
        String url = "https://www.twitch.tv/winstrikearena";
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    @Override
    public void onChooseSeatClick(SeatModel seat) {
        showFragmentHolderContainer(true);

        initMainToolbar(HIDE_MENU, "Winstrike Arena", SHOW_ICON, ScreenType.MAIN);

        MapInfoSingleton.getInstance().setSeat(seat);
        presenter.onChooseScreenClick();
    }

    private void showFragmentHolderContainer(Boolean isVisible) {
        if (isVisible) {
            flFragmentContainer.setVisibility(View.VISIBLE);
        } else {
            flFragmentContainer.setVisibility(View.GONE);
        }
    }

    @Override
    public void onItemPayClick(PayAdapter.PayViewHolder view, Integer position) {
        Timber.tag("common").d("onItemPayClick: %s", view.itemView.getClass().getSimpleName());
    }

    @Override
    public void showWait() {
        // TODO: 13/05/2018 Show progress for some loading dialogs
    }

    @Override
    public void removeWait() {
    }

    @Override
    public void onMapShow() {
        presenter.onMapShowClick();
    }

    @Override
    public void onGetOrdersSuccess(ArrayList<OrderModel> orderModels) {
        mPayList = orderModels;
    }

    @Override
    public void onGetOrdersFailure(String appErrorMessage) {
        // TODO: 13/05/2018 !!! Write here error catch logic.
        Timber.d("Can't get orders: %s", appErrorMessage);
    }

    public ArrayList<OrderModel> getOrders() {
        return mPayList;
    }

    public class BottomNavigationListener implements AHBottomNavigation.OnTabSelectedListener {
        @Override
        public boolean onTabSelected(int position, boolean wasSelected) {
            switch (position) {
                case HOME_TAB_POSITION:
                    showFragmentHolderContainer(false);
                    setHomeScreenStateVisibily(true);
                    setProfileScreenInterfaceVisibility(false);
                    initMainToolbar(HIDE_MENU, "Winstrike Arena", HIDE_ICON, ScreenType.MAIN);
                    presenter.onTabHomeClick();
                    break;
                case PLACE_TAB_POSITION:
                    showFragmentHolderContainer(true);
                    setProfileScreenInterfaceVisibility(false);
                    initMainToolbar(HIDE_MENU, "Оплаченные места", SHOW_ICON, ScreenType.MAIN);
                    presenter.onTabPlaceClick(mPayList);
                    break;
                case USER_TAB_POSITION:
                    showFragmentHolderContainer(false);
                    setProfileScreenInterfaceVisibility(true);
                    toolbar.setNavigationIcon(null);
                    initMainToolbar(SHOW_MENU, "Настройки", SHOW_ICON, ScreenType.PROFILE);
                    presenter.onTabUserClick();
                    break;
            }
            return true;
        }
    }

    private class MainOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            initMainToolbar(HIDE_MENU, "Winstrike Arena", HIDE_ICON, ScreenType.MAIN);
            presenter.onBackPressed();
        }
    }

    private class MapOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            initMainToolbar(HIDE_MENU, "Winstrike Arena", SHOW_ICON, ScreenType.MAIN);
            router.replaceScreen(Screens.CHOOSE_SCREEN, 0);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.onStop();
        this.mMainOnClickListener = null;
        this.mMapOnClickListener = null;
        this.mUserViewModel.delete();
    }
}
