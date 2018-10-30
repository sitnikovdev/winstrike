package ru.prsolution.winstrike.ui.main;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.constraint.ConstraintSet;
import android.support.transition.TransitionManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ShareCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import java.util.ArrayList;
import java.util.List;
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
import ru.prsolution.winstrike.mvp.apimodels.Room;
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
import ru.prsolution.winstrike.ui.main.ArenaSelectAdapter.OnItemArenaClickListener;
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

/*
  A Big God Activity.
  // TODO: 18/10/2018 Need some refactoring for SOLID principle.
 */
public class MainScreenActivity extends MvpAppCompatActivity implements MainScreenView, RouterProvider, OnProfileButtonsClickListener,
    OnAppButtonsClickListener, OnChoosePlaceButtonsClickListener
    , onMapShowProcess, OnItemArenaClickListener {

  private ProgressDialog progressDialog;
  private AHBottomNavigation bottomNavigationBar;
  private MainContainerFragment homeTabFragment;
  private MainContainerFragment placesTabFragment;
  private MainContainerFragment userTabFragment;
  private MainContainerFragment chooseTabFragment;
  private MainContainerFragment mapTabFragment;
  private MainContainerFragment payTabFragment;
  private ViewPager viewPagerSeat;
  private CarouselAdapter adapter;
  private ArrayList<OrderModel> mPayList = new ArrayList<>();
  private final Boolean HIDE_ICON = true;
  private final Boolean SHOW_ICON = false;
  private final Boolean HIDEMENU = false;
  private final Boolean SHOWMENU = true;
  private Boolean mState = SHOWMENU;
  private Dialog mDlgSingOut;
  private MainOnClickListener mMainOnClickListener;
  private MapOnClickListener mMapOnClickListener;
  private ScreenType mScreenType;
  private Dialog mDlgMapLegend;
  private UserProfileObservable user = new UserProfileObservable();
  private AcMainscreenBinding binding;
  private ArrayList<ArenaItem> arenaItems = new ArrayList<ArenaItem>();
  private String[] titles;
  private String[] address;
  private ConstraintSet arenaUpConstraintSet = new ConstraintSet();
  private ConstraintSet arenaDownConstraintSet = new ConstraintSet();
  SharedPreferences sharedPref;
  SharedPreferences.Editor editor;
  public int selectedArena = 0;
  public List<Room> rooms;

  private MenuItem profileMenu;

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
            user.setName(rooms.get(selectedArena).getName());
            initMainToolbar(SHOW_ICON, ScreenType.MAIN, mMainOnClickListener);
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
            user.setName(rooms.get(selectedArena).getName());
            initMainToolbar(SHOW_ICON, ScreenType.MAIN, mMapOnClickListener);
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
            user.setName(getString(R.string.title_payment));
            initMainToolbar(SHOW_ICON, ScreenType.MAP, mMainOnClickListener);
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
    this.selectedArena = layoutPosition;
    arenaUpConstraintSet.applyTo(binding.root);
    editor.putInt(getString(R.string.saved_arena), layoutPosition);
    editor.commit();

    ArenaSelectAdapter.SELECTED_ITEM = layoutPosition;
    binding.tvArenaTitle.setText(rooms.get(selectedArena).getName());

    initArena();

    binding.rvArena.getAdapter().notifyDataSetChanged();
  }

  private void initCarouselArenaSeat(int pos) {

    float widthPx = WinstrikeApp.getInstance().getDisplayWidhtPx();

    adapter.setPagesCount(1);
    adapter.addFragment(CarouselSeatFragment.newInstance(this, pos), 0);
    adapter.notifyDataSetChanged();
    viewPagerSeat.setAdapter(adapter);
    viewPagerSeat.setPageTransformer(false, adapter);
    viewPagerSeat.setCurrentItem(0);
    viewPagerSeat.setOffscreenPageLimit(2);

    if (widthPx <= Constants.SCREEN_WIDTH_PX_720) {
      viewPagerSeat.setPageMargin(Constants.SCREEN_MARGIN_350);
    } else if (widthPx <= Constants.SCREEN_WIDTH_PX_1080) {
      viewPagerSeat.setPageMargin(Constants.SCREEN_MARGIN_450);
    } else if (widthPx <= Constants.SCREEN_WIDTH_PX_1440) {
      viewPagerSeat.setPageMargin(Constants.SCREEN_MARGIN_600);
    } else {
      viewPagerSeat.setPageMargin(Constants.SCREEN_MARGIN_450);
    }
  }

  @Override
  public void onChooseArenaSeatClick(SeatModel seat) {
    TimeDataModel.INSTANCE.clearPids();
    showFragmentHolderContainer(true);
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
    Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
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

    this.rooms = WinstrikeApp.getInstance().getRooms();

    titles = new String[]{rooms.get(0).getName(), rooms.get(1).getName()};
    address = new String[]{rooms.get(0).getMetro(), rooms.get(1).getMetro()};
    binding = DataBindingUtil.setContentView(this, R.layout.ac_mainscreen);

    for (int i = 0; i < titles.length; i++) {
      arenaItems.add(new ArenaItem((titles[i]), address[i], false));
    }

    sharedPref = getPreferences(Context.MODE_PRIVATE);
    editor = sharedPref.edit();
    selectedArena = sharedPref.getInt(getString(R.string.saved_arena), Constants.SAVED_ARENA_DEFAULT);

    binding.setUser(user);
    binding.tvArenaTitle.setText(rooms.get(selectedArena).getName());
    ArenaSelectAdapter.SELECTED_ITEM = selectedArena;

    //Transitions animations:
    arenaDownConstraintSet.clone(this, R.layout.part_arena_down);
    arenaUpConstraintSet.clone(this, R.layout.part_arena_up);

    binding.rvArena.setAdapter(new ArenaSelectAdapter(this, this, arenaItems));
    binding.rvArena.setLayoutManager(new LinearLayoutManager(this));
    binding.rvArena.addItemDecoration(new RecyclerViewMargin(24, 1));
    binding.rvArena.getAdapter().notifyDataSetChanged();

    ButterKnife.bind(this);

    viewPagerSeat = findViewById(R.id.view_pager_seat);
    adapter = new CarouselAdapter(this);
    initArena();
    Timber.w(String.valueOf(adapter.getCount()));

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
    binding.arrowArenaDown.setOnClickListener(v ->
        {
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            TransitionManager.beginDelayedTransition(binding.root);
          }
          arenaDownConstraintSet.applyTo(binding.root);
        }
    );

    binding.arrowArenaUp.setOnClickListener(
        v ->
        {
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            TransitionManager.beginDelayedTransition(binding.root);
          }
          arenaUpConstraintSet.applyTo(binding.root);
        }
    );
  }

  private void initArena() {
    // TODO: 20/10/2018 Get arena name from Api.
    Uri uri = Uri.parse(rooms.get(selectedArena).getImageUrl());
    binding.headImage.setImageURI(uri);
    binding.getUser().setName(rooms.get(selectedArena).getName());
    binding.arenaDescription.setText(rooms.get(selectedArena).getDescription());
    initCarouselArenaSeat(this.selectedArena);
  }

  private void initViews() {
    initBottomNavigationBar();

    // Hide profile interface element
    setProfileScreenVisibility(false);

    dlgMapLegend();
    dlgProfileSingOut();
  }

  public void initMainToolbar(Boolean hideNavIcon, ScreenType screenType, View.OnClickListener listener) {
    setSupportActionBar(binding.toolbar);
    binding.toolbar.setNavigationOnClickListener(listener);

    mScreenType = screenType;
    invalidateOptionsMenu(); // now onCreateOptionsMenu(...) is called again
    binding.toolbar.setNavigationIcon(R.drawable.ic_back_arrow);
    if (hideNavIcon) {
      binding.toolbar.setNavigationIcon(null);
      binding.toolbar.setContentInsetsAbsolute(0, binding.toolbar.getContentInsetStart());
    } else {
      binding.toolbar.setNavigationIcon(R.drawable.ic_back_arrow);
      binding.toolbar.setContentInsetsAbsolute(0, binding.toolbar.getContentInsetStartWithNavigation());
    }
    getSupportActionBar().setDisplayShowTitleEnabled(false);

  }


  private void setHomeScreenStateVisibility(Boolean isVisible) {
    if (isVisible) {
      binding.vSpinner.setVisibility(View.VISIBLE);
      binding.headImage.setVisibility(VISIBLE);
      binding.arenaDescription.setVisibility(VISIBLE);
      binding.seatTitle.setVisibility(VISIBLE);
      viewPagerSeat.setVisibility(VISIBLE);
    } else {
      binding.vSpinner.setVisibility(GONE);
      binding.rvArena.setVisibility(GONE);
      binding.headImage.setVisibility(GONE);
      binding.arenaDescription.setVisibility(GONE);
      binding.seatTitle.setVisibility(GONE);
      viewPagerSeat.setVisibility(GONE);
    }
  }

  private void initFragmentsContainers() {
    FragmentManager fm = getSupportFragmentManager();
    homeTabFragment = (MainContainerFragment) fm.findFragmentByTag(getString(R.string.tag_main));
    if (homeTabFragment == null) {
      homeTabFragment = MainContainerFragment.getNewInstance(getString(R.string.tag_main));
      fm.beginTransaction()
          .add(R.id.fragment_container, homeTabFragment, getString(R.string.tag_main))
          .detach(homeTabFragment).commitNow();
      fm.executePendingTransactions();
    }

    placesTabFragment = (MainContainerFragment) fm.findFragmentByTag(getString(R.string.tag_places));
    if (placesTabFragment == null) {
      placesTabFragment = MainContainerFragment.getNewInstance(getString(R.string.tag_places));
      fm.beginTransaction()
          .add(R.id.fragment_container, placesTabFragment, getString(R.string.tag_places))
          .detach(placesTabFragment).commitNow();
      fm.executePendingTransactions();
    }

    userTabFragment = (MainContainerFragment) fm.findFragmentByTag(getString(R.string.tag_user));
    if (userTabFragment == null) {
      userTabFragment = MainContainerFragment.getNewInstance(getString(R.string.tag_user));
      fm.beginTransaction()
          .add(R.id.fragment_container, userTabFragment, getString(R.string.tag_user))
          .detach(userTabFragment).commitNow();
      fm.executePendingTransactions();
    }

    chooseTabFragment = (MainContainerFragment) fm.findFragmentByTag(getString(R.string.tag_choose));
    if (chooseTabFragment == null) {
      chooseTabFragment = MainContainerFragment.getNewInstance(getString(R.string.tag_choose));
      fm.beginTransaction()
          .add(R.id.fragment_container, chooseTabFragment, getString(R.string.tag_choose))
          .detach(chooseTabFragment).commitNow();
      fm.executePendingTransactions();
    }

    mapTabFragment = (MainContainerFragment) fm.findFragmentByTag(getString(R.string.tag_map));
    if (mapTabFragment == null) {
      mapTabFragment = MainContainerFragment.getNewInstance(getString(R.string.tag_map));
      fm.beginTransaction()
          .add(R.id.fragment_container, mapTabFragment, getString(R.string.tag_map))
          .detach(mapTabFragment).commitNow();
      fm.executePendingTransactions();
    }

    payTabFragment = (MainContainerFragment) fm.findFragmentByTag(getString(R.string.tag_pay));
    if (payTabFragment == null) {
      payTabFragment = MainContainerFragment.getNewInstance(getString(R.string.tag_pay));
      fm.beginTransaction()
          .add(R.id.fragment_container, payTabFragment, getString(R.string.tag_pay))
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
  public void setProfileScreenVisibility(Boolean isVisible) {
    if (isVisible) {
      setArenaVisibility(false);
      binding.tabLayoutProfile.setVisibility(VISIBLE);
      binding.viewPagerProfile.setVisibility(VISIBLE);
      setupProfileViewPager(binding.viewPagerProfile);
      binding.tabLayoutProfile.setupWithViewPager(binding.viewPagerProfile);
    } else {
      setArenaVisibility(true);
      binding.tabLayoutProfile.setVisibility(GONE);
      binding.viewPagerProfile.setVisibility(GONE);
    }
  }

  public void setArenaVisibility(Boolean isVisible) {
    if (!isVisible) {
      binding.vSpinner.setVisibility(GONE);
      binding.rvArena.setVisibility(GONE);
      binding.arrowArenaUp.setVisibility(GONE);
      binding.arrowArenaDown.setVisibility(GONE);
    } else {
      binding.vSpinner.setVisibility(VISIBLE);
      binding.arrowArenaUp.setVisibility(GONE);
      binding.arrowArenaDown.setVisibility(VISIBLE);
    }
  }

  private void setupProfileViewPager(ViewPager viewPager) {
    BaseViewPagerAdapter pagerAdapter = new BaseViewPagerAdapter(getSupportFragmentManager());
    pagerAdapter.addFragments(ProfileFragment.newInstance(), getString(R.string.title_profile));
    pagerAdapter.addFragments(AppFragment.newInstance(), getString(R.string.title_app));
    viewPager.setAdapter(pagerAdapter);
  }

  @Override
  public void onProfileUpdate(String name, String password) {
    if (password.isEmpty()) {
      Toast.makeText(this, R.string.message_password, Toast.LENGTH_LONG).show();
    }
    if (name.isEmpty()) {
      Toast.makeText(this, R.string.message_username, Toast.LENGTH_LONG).show();
    }
    if (!TextUtils.isEmpty(password) && !TextUtils.isEmpty(name)) {
      // call api for update profile here ...
      ProfileModel profile = new ProfileModel();
      profile.setName(name);
      profile.setPassword(password);
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
      initMainToolbar(SHOW_ICON, ScreenType.PROFILE, mMainOnClickListener);
    }
  }

  public void onFailureUpdateProfile(String appErrorMessage) {
    Toast.makeText(this, R.string.message_on_failure_profile_update, Toast.LENGTH_LONG).show();
  }

  public void onProfileUpdateSuccessfully(MessageResponse authResponse) {
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


  // Social networks links block:
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
    shareImgOnRecommendClick();
  }

  private void shareImgOnRecommendClick() {
    Uri attached_Uri = Uri.parse(Constants.ANDROID_RESOURCES_PATH + getPackageName()
        + Constants.SHARE_DRAWABLE + Constants.SHARE_IMG);
    Intent shareIntent = ShareCompat.IntentBuilder.from(this)
        .setType(Constants.IMAGE_TYPE)
        .setStream(attached_Uri)
        .getIntent();
    shareIntent.putExtra(Intent.EXTRA_TEXT, R.string.message_share_images);
    startActivity(Intent.createChooser(shareIntent, Constants.SHARE_IMG_TITLE));
  }

  // Map actions block:
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
    wlp.y = Constants.LEGEND_MAP_TOP_MARGIN;
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
      binding.fragmentContainer.setVisibility(VISIBLE);
    } else {
      binding.fragmentContainer.setVisibility(GONE);
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
    mPayList = new ArrayList<>();
    presenter.onTabPlaceClick(mPayList);
    Timber.d("Failure get layout from server: %s", appErrorMessage);
  }

  // Bottom navigation menu:
  private void initBottomNavigationBar() {
    BottomNavigationListener bottomNavigationListener = new BottomNavigationListener();
    // Create items
    AHBottomNavigationItem itHome = new AHBottomNavigationItem(null, R.drawable.ic_home);
    AHBottomNavigationItem itPlaces = new AHBottomNavigationItem(null, R.drawable.ic_money);
    AHBottomNavigationItem itProfile = new AHBottomNavigationItem(null, R.drawable.ic_user);

    // Add items
    bottomNavigationBar = findViewById(R.id.ab_bottom_navigation_bar);
    bottomNavigationBar.addItem(itHome);
    bottomNavigationBar.addItem(itPlaces);
    bottomNavigationBar.addItem(itProfile);

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
          setProfileScreenVisibility(false);
          user.setName(rooms.get(selectedArena).getName());
          initMainToolbar(HIDE_ICON, ScreenType.MAIN, mMainOnClickListener);
          presenter.onTabHomeClick();
          break;
        case PLACE_TAB_POSITION:
          showFragmentHolderContainer(true);
          setProfileScreenVisibility(false);
          setHomeScreenStateVisibility(false);
          user.setName(getString(R.string.title_payment_places));
          initMainToolbar(SHOW_ICON, ScreenType.MAIN, mMainOnClickListener);
          String token = Constants.TOKEN_TYPE_BEARER + AuthUtils.INSTANCE.getToken();
          presenter.getOrders(token);
          break;
        case USER_TAB_POSITION:
          showFragmentHolderContainer(false);
          setProfileScreenVisibility(true);
          binding.toolbar.setNavigationIcon(null);
          user.setName(AuthUtils.INSTANCE.getName());
          initMainToolbar(SHOW_ICON, ScreenType.PROFILE, mMainOnClickListener);
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
    bottomNavigationBar.setVisibility(GONE);
  }

  @Override
  public void showBottomTab() {
    bottomNavigationBar.setVisibility(VISIBLE);
  }

  private class MainOnClickListener implements View.OnClickListener {

    @Override
    public void onClick(View v) {
      clearData();
      user.setName(rooms.get(selectedArena).getName());
      initMainToolbar(HIDE_ICON, ScreenType.MAIN, this);
      presenter.onBackPressed();
    }
  }

  private class MapOnClickListener implements View.OnClickListener {

    @Override
    public void onClick(View v) {
      TimeDataModel.INSTANCE.clearPids();
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
