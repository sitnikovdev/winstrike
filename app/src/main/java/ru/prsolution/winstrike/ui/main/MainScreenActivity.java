package ru.prsolution.winstrike.ui.main;

import static android.view.View.VISIBLE;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.design.widget.TabLayout;
import android.support.transition.TransitionManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ShareCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.Space;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
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
import butterknife.BindView;
import butterknife.ButterKnife;
import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationViewPager;
import java.util.ArrayList;
import javax.inject.Inject;
import ru.prsolution.winstrike.R;
import ru.prsolution.winstrike.WinstrikeApp;
import ru.prsolution.winstrike.common.BackButtonListener;
import ru.prsolution.winstrike.common.CarouselAdapter;
import ru.prsolution.winstrike.common.RouterProvider;
import ru.prsolution.winstrike.common.ScreenType;
import ru.prsolution.winstrike.common.utils.AuthUtils;
import ru.prsolution.winstrike.common.vpadapter.BaseViewPagerAdapter;
import ru.prsolution.winstrike.databinding.AcMainscreenBinding;
import ru.prsolution.winstrike.mvp.apimodels.OrderModel;
import ru.prsolution.winstrike.mvp.models.FCMModel;
import ru.prsolution.winstrike.mvp.models.MessageResponse;
import ru.prsolution.winstrike.mvp.models.ProfileModel;
import ru.prsolution.winstrike.mvp.models.SeatModel;
import ru.prsolution.winstrike.mvp.models.TimeDataModel;
import ru.prsolution.winstrike.mvp.models.UserProfileObservable;
import ru.prsolution.winstrike.mvp.presenters.MainScreenPresenter;
import ru.prsolution.winstrike.mvp.views.MainScreenView;
import ru.prsolution.winstrike.networking.Service;
import ru.prsolution.winstrike.ui.Screens;
import ru.prsolution.winstrike.ui.main.AppFragment.OnAppButtonsClickListener;
import ru.prsolution.winstrike.ui.main.ArenaSelectAdapter.OnItemLangClickListener;
import ru.prsolution.winstrike.ui.main.CarouselSeatFragment.OnChoosePlaceButtonsClickListener;
import ru.prsolution.winstrike.ui.main.ChooseScreenFragment.onMapShowProcess;
import ru.prsolution.winstrike.ui.main.ProfileFragment.OnProfileButtonsClickListener;
import ru.prsolution.winstrike.ui.start.SplashActivity;
import ru.prsolution.winstrike.utils.Constants;
import ru.terrakok.cicerone.Navigator;
import ru.terrakok.cicerone.NavigatorHolder;
import ru.terrakok.cicerone.Router;
import ru.terrakok.cicerone.commands.Back;
import ru.terrakok.cicerone.commands.Command;
import ru.terrakok.cicerone.commands.Replace;
import ru.terrakok.cicerone.commands.SystemMessage;
import timber.log.Timber;


public class MainScreenActivity extends MvpAppCompatActivity implements MainScreenView, RouterProvider, OnProfileButtonsClickListener,
    OnAppButtonsClickListener, OnChoosePlaceButtonsClickListener
    , onMapShowProcess, OnItemLangClickListener {

  @BindView(R.id.root) ConstraintLayout layoutRoot;
  @BindView(R.id.toolbar_text) TextView tvToolbarTitle;
  @BindView(R.id.ab_container) RelativeLayout flFragmentContainer;
  @BindView(R.id.head_image) ImageView ivHeadImage;
  @BindView(R.id.description) TextView tvCarouselDescription;
  @BindView(R.id.category) TextView tvCarouselTitleCategory;
  @BindView(R.id.spacer) Space spSpace;
  @BindView(R.id.main_toolbar) Toolbar toolbar;
  @BindView(R.id.viewpager) AHBottomNavigationViewPager viewPager;
  @BindView(R.id.tablayout) TabLayout tabLayout;
  @BindView(R.id.rv_arena) RecyclerView rv_arena;
  @BindView(R.id.v_tap_arrow_down) View viewLangDown;
  @BindView(R.id.v_tap_arrow_up) View viewLangUp;
  @BindView(R.id.tvArenaTitle) TextView tv_lang;
  @Nullable
  @BindView(R.id.tv_title)
  TextView tvToolbarHead;


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
  private final Boolean HIDE_ICON = true;
  private final Boolean SHOW_ICON = false;
  private Dialog mDlgSingOut;
  private MainOnClickListener mMainOnClickListener;
  private MapOnClickListener mMapOnClickListener;
  private ScreenType mScreenType;
  private Dialog mDlgMapLegend;
  private UserProfileObservable user;
  private AcMainscreenBinding binding;
  private ArrayList<RowItem> rowItems;
  private final Integer[] titles = new Integer[]{R.string.spin_arena1, R.string.spin_arena2};
  private final Integer[] address = {R.string.spin_address1, R.string.spin_address2};
  private ConstraintSet arenaUpConstraintSet = new ConstraintSet();
  private ConstraintSet arenaDownConstraintSet = new ConstraintSet();


  @InjectPresenter
  MainScreenPresenter presenter;

  @Inject
  public Service service;

  @Inject
  Router router;

  @Inject
  NavigatorHolder navigatorHolder;

  @ProvidePresenter
  public MainScreenPresenter createBottomNavigationPresenter() {
    return new MainScreenPresenter(service, router);
  }

  public Service getService() {
    return service;
  }

  @Override
  public Router getRouter() {
    return router;
  }

  private Navigator navigator = new Navigator() {
    @Override
    public void applyCommands(Command[] commands) {
      for (Command command : commands) {
        applyCommand(command);
      }
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
            setHomeScreenStateVisibility(true);
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
            setHomeScreenStateVisibility(false);
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
            setHomeScreenStateVisibility(false);
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
            initMainToolbar(getString(R.string.app_club), SHOW_ICON, ScreenType.MAIN, mMainOnClickListener);
            setHomeScreenStateVisibility(false);
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
            initMainToolbar(getString(R.string.app_club), SHOW_ICON, ScreenType.MAP, mMapOnClickListener);
            setHomeScreenStateVisibility(false);
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
            initMainToolbar(getString(R.string.title_payment), SHOW_ICON, ScreenType.MAP, mMainOnClickListener);
            setHomeScreenStateVisibility(false);
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
  public void onArenaSelectItem(View v, int layoutPosition) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      TransitionManager.beginDelayedTransition(binding.root);
    }
    arenaUpConstraintSet.applyTo(binding.root);

    ArenaSelectAdapter.SELECTED_ITEM = layoutPosition;
    RowItem item = rowItems.get(layoutPosition);
    tv_lang.setText(item.getTitle());

    binding.rvArena.getAdapter().notifyDataSetChanged();
  }

  private void initCarouselArenaSeat(int currentItem) {

    dpWidth = WinstrikeApp.getInstance().getDisplayWidhtDp();
    dpHeight = WinstrikeApp.getInstance().getDisplayHeightDp();

    Float widthPx = WinstrikeApp.getInstance().getDisplayWidhtPx();
    Float heightPx = WinstrikeApp.getInstance().getDisplayHeightPx();

    adapter.addFragment(CarouselSeatFragment.newInstance(this, 0), 0);
    adapter.addFragment(CarouselSeatFragment.newInstance(this, 1), 1);
    adapter.notifyDataSetChanged();
    viewPagerSeat.setAdapter(adapter);
    viewPagerSeat.setPageTransformer(false, adapter);
    viewPagerSeat.setCurrentItem(currentItem);
    viewPagerSeat.setOffscreenPageLimit(2);

    Timber.d("PX: pxWidth: %s x pxHeight: %s px", widthPx, heightPx);
    Timber.d("DP: dpWidth: %s dp x dpHeight: %s dp", dpWidth, dpHeight);

    if (widthPx <= 720) {
      viewPagerSeat.setPageMargin(-350);
      Timber.d("widthPx <= 720");
    } else if (widthPx <= 1080) {
      viewPagerSeat.setPageMargin(-450);
      Timber.d("widthPx <= 1080");
    } else if (widthPx <= 1440) {
      Timber.d("widthPx <= 1440");
      viewPagerSeat.setPageMargin(-600);
    } else {
      Timber.d("dbWidth: - 450. Default");
      viewPagerSeat.setPageMargin(-450);
    }

  }

  @Override
  public void onChooseArenaSeatClick(SeatModel seat) {
    TimeDataModel.INSTANCE.clearPids();
    showFragmentHolderContainer(true);
    initMainToolbar(getResources().getString(R.string.app_club), SHOW_ICON, ScreenType.MAIN, mMainOnClickListener);
    WinstrikeApp.getInstance().setSeat(seat);
    presenter.onChooseScreenClick();
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

  @Override
  public void showWait() {
  }

  @Override
  public void removeWait() {
  }

  @Override
  protected void onStart() {
    super.onStart();
    AuthUtils.INSTANCE.setLogout(false);
  }

  @Override
  protected void onPause() {
    navigatorHolder.removeNavigator();
    super.onPause();
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
    this.mMainOnClickListener = null;
    this.mMapOnClickListener = null;
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

  @Override
  public void onCreate(Bundle savedInstanceState) {
    WinstrikeApp.INSTANCE.getAppComponent().inject(this);
    super.onCreate(savedInstanceState);
    clearData();

    user = new UserProfileObservable();

    binding = DataBindingUtil.setContentView(this, R.layout.ac_mainscreen);

    binding.setUser(user);

    rowItems = new ArrayList<RowItem>();
    for (int i = 0; i < titles.length; i++) {
      RowItem item = new RowItem(getString(titles[i]), getString(address[i]), false);
      rowItems.add(item);
    }

    //Transitions animations:
    arenaDownConstraintSet.clone(this, R.layout.part_arena_down);
    arenaUpConstraintSet.clone(this, R.layout.part_arena_up);

    binding.rvArena.setAdapter(new ArenaSelectAdapter(this, this, rowItems));
    binding.rvArena.setLayoutManager(new LinearLayoutManager(this));
    RecyclerViewMargin decoration = new RecyclerViewMargin(24, 1);
    binding.rvArena.addItemDecoration(decoration);
    binding.rvArena.getAdapter().notifyDataSetChanged();

    user.setName(getResources().getString(R.string.app_club));

    ButterKnife.bind(this);

    viewPagerSeat = findViewById(R.id.view_pager_seat);
    adapter = new CarouselAdapter(this);
    Timber.w(String.valueOf(adapter.getCount()));
    initCarouselArenaSeat(1);

    progressDialog = new ProgressDialog(this);

    mMainOnClickListener = new MainOnClickListener();

    mMapOnClickListener = new MapOnClickListener();

    initViews();
    initFragmentsContainers();

    if (savedInstanceState == null) {
      presenter.onCreate();
    }

    String token = Constants.TOKEN_TYPE_BEARER + AuthUtils.INSTANCE.getToken();

    String fcmToken = AuthUtils.INSTANCE.getFcmtoken();
    if (!fcmToken.isEmpty()) {
      sendRegistrationToServer(token, fcmToken);
    }

    // Arena select:
    viewLangDown.setOnClickListener(v ->
        {
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            TransitionManager.beginDelayedTransition(binding.root);
          }
          arenaDownConstraintSet.applyTo(binding.root);
        }
    );

    viewLangUp.setOnClickListener(
        v ->

        {
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            TransitionManager.beginDelayedTransition(binding.root);
          }
          arenaUpConstraintSet.applyTo(binding.root);
        }
    );
  }

  private void initViews() {
    initMainToolbar(getResources().getString(R.string.app_club), HIDE_ICON, ScreenType.MAIN, mMainOnClickListener);
    initBottomNavigationBar();

    // Hide profile interface element
    setProfileScreenInterfaceVisibility(false);

    dlgMapLegend();
    dlgProfileSingOut();
  }

  public void initMainToolbar(String title, Boolean hideNavIcon, ScreenType screenType, View.OnClickListener listener) {
    setSupportActionBar(toolbar);
    toolbar.setNavigationOnClickListener(listener);

    mScreenType = screenType;
    invalidateOptionsMenu(); // now onCreateOptionsMenu(...) is called again
    toolbar.setNavigationIcon(R.drawable.ic_back_arrow);
    tvToolbarTitle.setText(title);
    if (hideNavIcon) {
      toolbar.setNavigationIcon(null);
      toolbar.setContentInsetsAbsolute(0, toolbar.getContentInsetStart());
    } else {
      toolbar.setNavigationIcon(R.drawable.ic_back_arrow);
      toolbar.setContentInsetsAbsolute(0, toolbar.getContentInsetStartWithNavigation());
    }
    getSupportActionBar().setDisplayShowTitleEnabled(false);
  }

  public void initMainToolbar(String title, Boolean hideNavIcon, ScreenType screenType) {
    setSupportActionBar(toolbar);
    toolbar.setNavigationOnClickListener(mMainOnClickListener);

    mScreenType = screenType;
    invalidateOptionsMenu(); // now onCreateOptionsMenu(...) is called again
    toolbar.setNavigationIcon(R.drawable.ic_back_arrow);
    tvToolbarTitle.setText(title);
    if (hideNavIcon) {
      toolbar.setNavigationIcon(null);
      toolbar.setContentInsetsAbsolute(0, toolbar.getContentInsetStart());
    } else {
      toolbar.setNavigationIcon(R.drawable.ic_back_arrow);
      toolbar.setContentInsetsAbsolute(0, toolbar.getContentInsetStartWithNavigation());
    }
    getSupportActionBar().setDisplayShowTitleEnabled(false);
  }

  private void setHomeScreenStateVisibility(Boolean isVisible) {
    if (isVisible) {
      binding.vSpinner.setVisibility(View.VISIBLE);
      ivHeadImage.setVisibility(VISIBLE);
      tvCarouselDescription.setVisibility(VISIBLE);
      tvCarouselTitleCategory.setVisibility(VISIBLE);
      viewPagerSeat.setVisibility(VISIBLE);
    } else {
      binding.vSpinner.setVisibility(View.GONE);
      ivHeadImage.setVisibility(View.GONE);
      tvCarouselDescription.setVisibility(View.GONE);
      tvCarouselTitleCategory.setVisibility(View.GONE);
      viewPagerSeat.setVisibility(View.GONE);
    }
  }

  private void initFragmentsContainers() {
    FragmentManager fm = getSupportFragmentManager();
    homeTabFragment = (MainContainerFragment) fm.findFragmentByTag(getString(R.string.tag_main));
    if (homeTabFragment == null) {
      homeTabFragment = MainContainerFragment.getNewInstance(getString(R.string.tag_main));
      fm.beginTransaction()
          .add(R.id.ab_container, homeTabFragment, getString(R.string.tag_main))
          .detach(homeTabFragment).commitNow();
      fm.executePendingTransactions();
    }

    placesTabFragment = (MainContainerFragment) fm.findFragmentByTag(getString(R.string.tag_places));
    if (placesTabFragment == null) {
      placesTabFragment = MainContainerFragment.getNewInstance(getString(R.string.tag_places));
      fm.beginTransaction()
          .add(R.id.ab_container, placesTabFragment, getString(R.string.tag_places))
          .detach(placesTabFragment).commitNow();
      fm.executePendingTransactions();
    }

    userTabFragment = (MainContainerFragment) fm.findFragmentByTag(getString(R.string.tag_user));
    if (userTabFragment == null) {
      userTabFragment = MainContainerFragment.getNewInstance(getString(R.string.tag_user));
      fm.beginTransaction()
          .add(R.id.ab_container, userTabFragment, getString(R.string.tag_user))
          .detach(userTabFragment).commitNow();
      fm.executePendingTransactions();
    }

    chooseTabFragment = (MainContainerFragment) fm.findFragmentByTag(getString(R.string.tag_choose));
    if (chooseTabFragment == null) {
      chooseTabFragment = MainContainerFragment.getNewInstance(getString(R.string.tag_choose));
      fm.beginTransaction()
          .add(R.id.ab_container, chooseTabFragment, getString(R.string.tag_choose))
          .detach(chooseTabFragment).commitNow();
      fm.executePendingTransactions();
    }

    mapTabFragment = (MainContainerFragment) fm.findFragmentByTag(getString(R.string.tag_map));
    if (mapTabFragment == null) {
      mapTabFragment = MainContainerFragment.getNewInstance(getString(R.string.tag_map));
      fm.beginTransaction()
          .add(R.id.ab_container, mapTabFragment, getString(R.string.tag_map))
          .detach(mapTabFragment).commitNow();
      fm.executePendingTransactions();
    }

    payTabFragment = (MainContainerFragment) fm.findFragmentByTag(getString(R.string.tag_pay));
    if (payTabFragment == null) {
      payTabFragment = MainContainerFragment.getNewInstance(getString(R.string.tag_pay));
      fm.beginTransaction()
          .add(R.id.ab_container, payTabFragment, getString(R.string.tag_pay))
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
  public void goHome() {
    startActivity(new Intent(getApplicationContext(), MainScreenActivity.class));
  }

  // User profile actions:
  @Override
  public void setProfileScreenInterfaceVisibility(Boolean isVisible) {
    if (isVisible) {
      binding.vSpinner.setVisibility(View.GONE);
      tabLayout.setVisibility(VISIBLE);
      viewPager.setVisibility(VISIBLE);
      setupProfileViewPager(viewPager);
      tabLayout.setupWithViewPager(viewPager);
    } else {
      binding.vSpinner.setVisibility(View.VISIBLE);
      tabLayout.setVisibility(View.GONE);
      viewPager.setVisibility(View.GONE);
    }
  }

  private void setupProfileViewPager(ViewPager viewPager) {
    pagerAdapter = new BaseViewPagerAdapter(getSupportFragmentManager());
    pagerAdapter.addFragments(ProfileFragment.newInstance(), getString(R.string.title_profile));
    pagerAdapter.addFragments(AppFragment.newInstance(), getString(R.string.title_app));
    viewPager.setAdapter(pagerAdapter);
  }

  @Override
  public void onProfileUpdate(String name, String passw) {
    if (passw.isEmpty()) {
      Toast.makeText(this, R.string.message_password, Toast.LENGTH_LONG).show();
    }
    if (name.isEmpty()) {
      Toast.makeText(this, R.string.message_username, Toast.LENGTH_LONG).show();
    }
    if (!TextUtils.isEmpty(passw) && !TextUtils.isEmpty(name)) {
      // call api for update profile here ...
      ProfileModel profile = new ProfileModel();
      profile.setName(name);
      profile.setPassword(passw);
      String publicId = AuthUtils.INSTANCE.getPublicid();
      String token = Constants.TOKEN_TYPE_BEARER + AuthUtils.INSTANCE.getToken();
      presenter.updateProfile(token, profile, publicId);
      AuthUtils.INSTANCE.setName(name);
      String title = getString(R.string.title_settings);
      if (AuthUtils.INSTANCE.getName() != null) {
        if (!TextUtils.isEmpty(AuthUtils.INSTANCE.getName())) {
          title = AuthUtils.INSTANCE.getName();
        }
      }
      user.setName(name);
      initMainToolbar(title, SHOW_ICON, ScreenType.PROFILE, mMainOnClickListener);
    }
  }

  public void onFailureUpdateProfile(String appErrorMessage) {
    Timber.d("Wrong update profile");
    Toast.makeText(this, R.string.message_on_failure_profile_update, Toast.LENGTH_LONG).show();
  }

  public void onProfileUpdateSuccessfully(MessageResponse authResponse) {
    Timber.d("Profile is updated");
    Toast.makeText(this, R.string.message_on_success_profile_update, Toast.LENGTH_LONG).show();
  }

  private void dlgProfileSingOut() {
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
          AuthUtils.INSTANCE.setLogout(true);
          AuthUtils.INSTANCE.setToken("");
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


  // Social networks block:
  @Override
  public void onGooglePlayButtonClick() {
    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.URL_GOOGLE_PLAY)));
  }

  @Override
  public void onVkClick() {
    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.URL_VK)));
  }

  @Override
  public void onInstagramClick() {
    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.URL_INSTAGRAM)));
  }

  @Override
  public void onTweeterClick() {
    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.URL_TWEETER)));
  }

  @Override
  public void onFacebookClick() {
    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.URL_FACEBOOK)));
  }

  @Override
  public void onTwitchClick() {
    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.URL_TWITCH)));
  }

  @Override
  public void onRecommendButtonClick() {
    shareImg();
  }

  private void shareImg() {
    Uri attached_Uri = Uri.parse(Constants.ANDROID_RESOURCES_PATH + getPackageName()
        + Constants.SHARE_DRAWABLE + Constants.SHARE_IMG);
    Intent shareIntent = ShareCompat.IntentBuilder.from(this)
        .setType(Constants.IMAGE_TYPE)
        .setStream(attached_Uri)
        .getIntent();
    shareIntent.putExtra(Intent.EXTRA_TEXT, R.string.message_share_images);
    startActivity(Intent.createChooser(shareIntent, Constants.SHARE_IMG_TITLE));
  }

  // Map actions:
  private void dlgMapLegend() {
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
  public void onMapShow() {
    presenter.onMapShowClick();
  }

  private void clearData() {
    TimeDataModel.INSTANCE.clearPids();
    TimeDataModel.INSTANCE.clearDateTime();
  }

  private void showFragmentHolderContainer(Boolean isVisible) {
    if (isVisible) {
      flFragmentContainer.setVisibility(VISIBLE);
    } else {
      flFragmentContainer.setVisibility(View.GONE);
    }
  }

  public ArrayList<OrderModel> getOrders() {
    return this.mPayList;
  }

  @Override
  public void onGetOrdersSuccess(ArrayList<OrderModel> orders) {
    mPayList = orders;
    presenter.onTabPlaceClick(mPayList);
    Timber.d("UserEntity order list size: %s", mPayList.size());
  }

  @Override
  public void onGetOrdersFailure(String appErrorMessage) {
    Timber.d("Failure get layout from server: %s", appErrorMessage);
  }

  // Bottom navigation menu:
  private void initBottomNavigationBar() {
    bottomNavigationListener = new BottomNavigationListener();
    // Create items
    AHBottomNavigationItem item1 = new AHBottomNavigationItem(null, R.drawable.ic_home);
    AHBottomNavigationItem item2 = new AHBottomNavigationItem(null, R.drawable.ic_money);
    AHBottomNavigationItem item3 = new AHBottomNavigationItem(null, R.drawable.ic_user);

    // Add items
    bottomNavigationBar = findViewById(R.id.ab_bottom_navigation_bar);
    bottomNavigationBar.addItem(item1);
    bottomNavigationBar.addItem(item2);
    bottomNavigationBar.addItem(item3);

    bottomNavigationBar.setTitleState(AHBottomNavigation.TitleState.ALWAYS_HIDE);
    bottomNavigationBar.setAccentColor(getColor(R.color.color_accent));
    bottomNavigationBar.setOnTabSelectedListener(bottomNavigationListener);
  }

  public class BottomNavigationListener implements AHBottomNavigation.OnTabSelectedListener {

    @Override
    public boolean onTabSelected(int position, boolean wasSelected) {
      switch (position) {
        case HOME_TAB_POSITION:
          showFragmentHolderContainer(false);
          setHomeScreenStateVisibility(true);
          setProfileScreenInterfaceVisibility(false);
          initMainToolbar(getResources().getString(R.string.app_club), HIDE_ICON, ScreenType.MAIN, mMainOnClickListener);
          presenter.onTabHomeClick();
          break;
        case PLACE_TAB_POSITION:
          showFragmentHolderContainer(true);
          setProfileScreenInterfaceVisibility(false);
          initMainToolbar(getString(R.string.title_payment_places), SHOW_ICON, ScreenType.MAIN, mMainOnClickListener);
          String token = Constants.TOKEN_TYPE_BEARER + AuthUtils.INSTANCE.getToken();
          presenter.getOrders(token);
          break;
        case USER_TAB_POSITION:
          showFragmentHolderContainer(false);
          setProfileScreenInterfaceVisibility(true);
          toolbar.setNavigationIcon(null);
          user.setName(AuthUtils.INSTANCE.getName());
          String title = user.getName();
          if (AuthUtils.INSTANCE.getName() != null) {
            if (!TextUtils.isEmpty(AuthUtils.INSTANCE.getName())) {
              title = AuthUtils.INSTANCE.getName();
            }
          }
          initMainToolbar(title, SHOW_ICON, ScreenType.PROFILE, mMainOnClickListener);

          presenter.onTabUserClick();
          break;
      }
      return true;
    }
  }

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
    bottomNavigationBar.setVisibility(VISIBLE);
  }

  private class MainOnClickListener implements View.OnClickListener {

    @Override
    public void onClick(View v) {
      clearData();
      initMainToolbar(getResources().getString(R.string.app_club), HIDE_ICON, ScreenType.MAIN, this);
      presenter.onBackPressed();
    }
  }

  private class MapOnClickListener implements View.OnClickListener {

    @Override
    public void onClick(View v) {
      TimeDataModel.INSTANCE.clearPids();
      initMainToolbar(getResources().getString(R.string.app_club), SHOW_ICON, ScreenType.MAIN, this);
      router.replaceScreen(Screens.CHOOSE_SCREEN, 0);
    }
  }

  //FCM push message services:
  public void sendRegistrationToServer(String authToken, String refreshedToken) {
    FCMModel fcmModel = new FCMModel();
    fcmModel.setToken(refreshedToken);
    presenter.sendFCMTokenToServer(authToken, fcmModel);
  }

  @Override
  public void onPushClick(String isOn) {
    Toast.makeText(this, "Push is: " + isOn, Toast.LENGTH_LONG).show();
  }

}
