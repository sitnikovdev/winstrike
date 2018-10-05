package ru.prsolution.winstrike.ui.main;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ShareCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.Space;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
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

import java.util.List;
import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
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
import ru.prsolution.winstrike.ui.main.CarouselSeatFragment.OnChoosePlaceButtonsClickListener;
import ru.prsolution.winstrike.ui.main.ChooseScreenFragment.onMapShowProcess;
import ru.prsolution.winstrike.ui.main.ProfileFragment.OnProfileButtonsClickListener;
import ru.prsolution.winstrike.ui.start.SplashActivity;
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
    , onMapShowProcess {

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


  @BindView(R.id.toolbar_text) TextView tvToolbarTitle;
  @BindView(R.id.ab_container) RelativeLayout flFragmentContainer;
  @BindView(R.id.head_image) ImageView ivHeadImage;
  @BindView(R.id.spin) AppCompatSpinner spArenaSelect;
  @BindView(R.id.description) TextView tvCarouselDescription;
  @BindView(R.id.category) TextView tvCarouselTitleCategory;
  @BindView(R.id.spacer) Space spSpace;
  @BindView(R.id.main_toolbar) Toolbar toolbar;
  @BindView(R.id.viewpager) AHBottomNavigationViewPager viewPager;
  @BindView(R.id.tablayout) TabLayout tabLayout;
  @Nullable @BindView(R.id.tv_title) TextView tvToolbarHead;

  @InjectPresenter
  MainScreenPresenter presenter;


  @Inject
  public Service service;

  @Inject
  Router router;

  @Inject
  NavigatorHolder navigatorHolder;
  private UserProfileObservable user;

  private AcMainscreenBinding binding;

  List<RowItem> rowItems;
  public static final Integer[] titles = new Integer[] { R.string.spin_arena1,
      R.string.spin_arena2 };

  public static final Integer[] address = { R.string.spin_address1,
      R.string.spin_address2
      };
  private  int mSelectedIndex = 0;


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
    this.mMainOnClickListener = null;
    this.mMapOnClickListener = null;
  }

  @Override
  protected void onStart() {
    super.onStart();
    AuthUtils.INSTANCE.setLogout(false);
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

      RowItem item = new RowItem(getString(titles[i]),getString(address[i]),false);
      rowItems.add(item);
    }
    AppCompatSpinner spinner;
    spinner = (AppCompatSpinner) findViewById(R.id.spin);
    CustomSpinnAdapter spAdapter = new CustomSpinnAdapter(this,
        R.layout.item_arena, R.id.title, rowItems){

      @Override
      public View getView(int position, View convertView, ViewGroup parent) {

        return rowview(convertView, position, parent);
      }


      @Override
      public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return rowview(convertView, position, parent);
      }

      private View rowview(View convertView, int position, ViewGroup parent) {

        RowItem rowItem = getItem(position);

        viewHolder holder;
        View rowview = convertView;


        if (rowview == null) {

          holder = new viewHolder();
          flater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
          rowview = flater.inflate(R.layout.item_arena, null, false);

          holder.txtTitle = (TextView) rowview.findViewById(R.id.title);
          holder.txtAddress = (TextView) rowview.findViewById(R.id.address);

          holder.txtTitle.setTextColor(mContext.getColor(R.color.color_black));
          holder.txtAddress.setTextColor(mContext.getColor(R.color.color_black));

          Timber.d("Change color to black. Position: %s",position);


/*          switch (position) {
            case 0:
              holder.txtTitle.setTextColor(mContext.getColor(R.color.color_accent));
              holder.txtAddress.setTextColor(mContext.getColor(R.color.color_accent));
              break;
            case 1:
              holder.txtTitle.setTextColor(mContext.getColor(R.color.color_black));
              holder.txtAddress.setTextColor(mContext.getColor(R.color.color_black));
              break;
            default:
              holder.txtTitle.setTextColor(mContext.getColor(R.color.color_black));
              holder.txtAddress.setTextColor(mContext.getColor(R.color.color_black));
              break;
          }*/



          rowview.setTag(holder);
        } else {
          holder = (viewHolder) rowview.getTag();
        }
        holder.txtTitle.setText(rowItem.getTitle());
        holder.txtAddress.setText(rowItem.getAddress());

        return rowview;
      }


       class viewHolder {
        TextView txtTitle;
        TextView txtAddress;
      }

    };
    spinner.setAdapter(spAdapter);
    spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
        // your code here
        Timber.d("On item selected.");
        mSelectedIndex = position;
//        RowItem item = (RowItem) parentView.getItemAtPosition(position);
//        item.setSelected(true);
      }

      @Override
      public void onNothingSelected(AdapterView<?> parentView) {
        // your code here
        Timber.d("On item Nothing selected.");
      }

    });

    user.setName(getResources().getString(R.string.app_club));

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

    String token = "Bearer " + AuthUtils.INSTANCE.getToken();

    String fcmToken = AuthUtils.INSTANCE.getFcmtoken();
    if (!fcmToken.isEmpty()) {
      sendRegistrationToServer(token, fcmToken);
    }
  }

  public void sendRegistrationToServer(String authToken, String refreshedToken) {
    FCMModel fcmModel = new FCMModel();
    fcmModel.setToken(refreshedToken);
    presenter.sendFCMTokenToServer(authToken, fcmModel);
  }


  private void clearData() {
    TimeDataModel.INSTANCE.clearPids();
    TimeDataModel.INSTANCE.clearDateTime();
  }

  private void initCarouselSeat(int currentItem) {

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


  private void setupViewPager(ViewPager viewPager) {
    pagerAdapter = new BaseViewPagerAdapter(getSupportFragmentManager());
    pagerAdapter.addFragments(ProfileFragment.newInstance(), "Профиль");
    pagerAdapter.addFragments(AppFragment.newInstance(), "Приложение");
    viewPager.setAdapter(pagerAdapter);
  }


  private void initViews() {
    initMainToolbar(HIDE_MENU, getResources().getString(R.string.app_club), HIDE_ICON, ScreenType.MAIN, mMainOnClickListener);
    initBottomNavigationBar();

    // Hide profile interface element
    setProfileScreenInterfaceVisibility(false);

    dlgLegend();
    dlgSingOut();
  }

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


  public void initMainToolbar(Boolean hide_menu, String title, Boolean hideNavIcon, ScreenType screenType, View.OnClickListener listener) {
    setSupportActionBar(toolbar);
    toolbar.setNavigationOnClickListener(listener);

    mScreenType = screenType;
    // getMenuInflater().inflate(R.menu.main_toolbar_menu, menu);
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

  public void initMainToolbar(Boolean hide_menu, String title, Boolean hideNavIcon, ScreenType screenType) {
    setSupportActionBar(toolbar);
    toolbar.setNavigationOnClickListener(mMainOnClickListener);

    mScreenType = screenType;
    // getMenuInflater().inflate(R.menu.main_toolbar_menu, menu);
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


  private void setHomeScreenStateVisibily(Boolean isVisible) {
    if (isVisible) {
      spArenaSelect.setVisibility(View.VISIBLE);
      ivHeadImage.setVisibility(View.VISIBLE);
      tvCarouselDescription.setVisibility(View.VISIBLE);
      tvCarouselTitleCategory.setVisibility(View.VISIBLE);
      viewPagerSeat.setVisibility(View.VISIBLE);
    } else {
      spArenaSelect.setVisibility(View.GONE);
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
//                        toolbar.setNavigationOnClickListener(mMainOnClickListener);
            initMainToolbar(HIDE_MENU, getResources().getString(R.string.app_club), SHOW_ICON, ScreenType.MAIN, mMainOnClickListener);
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
//                        toolbar.setNavigationOnClickListener(mMapOnClickListener);
            initMainToolbar(HIDE_MENU, "Winstrike Arena", SHOW_ICON, ScreenType.MAP, mMapOnClickListener);
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
            initMainToolbar(HIDE_MENU, "Оплата", SHOW_ICON, ScreenType.MAP, mMainOnClickListener);
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
  public void onProfileUpdate(String name, String passw) {
    if (passw.isEmpty()) {
      Toast.makeText(this, "Введите текущий пароль", Toast.LENGTH_LONG).show();
    }
    if (name.isEmpty()) {
      Toast.makeText(this, "Введите имя пользователя", Toast.LENGTH_LONG).show();
    }
    if (!TextUtils.isEmpty(passw) && !TextUtils.isEmpty(name)) {
      Timber.d("Update user profile..");
      // call api for update profile here ...
      ProfileModel profile = new ProfileModel();
      profile.setName(name);
      profile.setPassword(passw);
      // TODO: 19/05/2018 Pass real public id here
      String publicId = AuthUtils.INSTANCE.getPublicid();
      String token = "Bearer " + AuthUtils.INSTANCE.getToken();
      presenter.updateProfile(token, profile, publicId);
      AuthUtils.INSTANCE.setName(name);
//            WinstrikeApp.getInstance().getUser().setName(name);
      String title = "Настройки";
      if (AuthUtils.INSTANCE.getName() != null) {
        if (!TextUtils.isEmpty(AuthUtils.INSTANCE.getName())) {
          title = AuthUtils.INSTANCE.getName();
        }
      }
      user.setName(name);
      initMainToolbar(SHOW_MENU, title, SHOW_ICON, ScreenType.PROFILE, mMainOnClickListener);
    }
  }


  public void onFailtureUpdateProfile(String appErrorMessage) {
    Timber.d("Wrong update profile");
    Toast.makeText(this, "Не удалось обновить профиль", Toast.LENGTH_LONG).show();
  }

  public void onProfileUpdateSuccessfully(MessageResponse authResponse) {
    Timber.d("Profile is updated");
    Toast.makeText(this, "Профиль успешно обновлен", Toast.LENGTH_LONG).show();
  }


  private void shareImg() {
    Uri attached_Uri = Uri.parse("android.resource://" + getPackageName()
        + "/drawable/" + "winstrike_share");
    Intent shareIntent = ShareCompat.IntentBuilder.from(this)
        .setType("image/jpg")
        .setStream(attached_Uri)
        .getIntent();
    shareIntent.putExtra(Intent.EXTRA_TEXT, "Winstrike Arena - киберспорт в центре Москвы.\n" +
        "Качай приложение и играй за 50 рублей/час");
    startActivity(Intent.createChooser(shareIntent, "Send"));
  }


  @Override
  public void onRecommendButtonClick() {
    shareImg();

  }

  @Override
  public void onPushClick(String isOn) {
    Toast.makeText(this, "Push is: " + isOn, Toast.LENGTH_LONG).show();
  }

  @Override
  public void onGooglePlayButtonClick() {
    String url = "https://play.google.com/store/apps/details?id=ru.prsolution.winstrike";
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

    TimeDataModel.INSTANCE.clearPids();

    showFragmentHolderContainer(true);

    initMainToolbar(HIDE_MENU, getResources().getString(R.string.app_club), SHOW_ICON, ScreenType.MAIN, mMainOnClickListener);

//        MapInfoSingleton.getInstance().setSeat(seat);
    WinstrikeApp.getInstance().setSeat(seat);
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

  public ArrayList<OrderModel> getOrders() {
    return this.mPayList;
  }


  public class BottomNavigationListener implements AHBottomNavigation.OnTabSelectedListener {

    @Override
    public boolean onTabSelected(int position, boolean wasSelected) {
      switch (position) {
        case HOME_TAB_POSITION:
          showFragmentHolderContainer(false);
          setHomeScreenStateVisibily(true);
          setProfileScreenInterfaceVisibility(false);
          initMainToolbar(HIDE_MENU, getResources().getString(R.string.app_club), HIDE_ICON, ScreenType.MAIN, mMainOnClickListener);
          presenter.onTabHomeClick();
          break;
        case PLACE_TAB_POSITION:
          showFragmentHolderContainer(true);
          setProfileScreenInterfaceVisibility(false);
          initMainToolbar(HIDE_MENU, "Оплаченные места", SHOW_ICON, ScreenType.MAIN, mMainOnClickListener);
          String token = "Bearer " + AuthUtils.INSTANCE.getToken();
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
          initMainToolbar(SHOW_MENU, title, SHOW_ICON, ScreenType.PROFILE, mMainOnClickListener);

          presenter.onTabUserClick();
          break;
      }
      return true;
    }
  }

  private class MainOnClickListener implements View.OnClickListener {

    @Override
    public void onClick(View v) {
      clearData();
      initMainToolbar(HIDE_MENU, getResources().getString(R.string.app_club), HIDE_ICON, ScreenType.MAIN, this);
      presenter.onBackPressed();
    }
  }

  private class MapOnClickListener implements View.OnClickListener {

    @Override
    public void onClick(View v) {
      TimeDataModel.INSTANCE.clearPids();
      initMainToolbar(HIDE_MENU, getResources().getString(R.string.app_club), SHOW_ICON, ScreenType.MAIN, this);
      router.replaceScreen(Screens.CHOOSE_SCREEN, 0);
    }
  }


  @Override
  protected void onStop() {
    super.onStop();
//        presenter.onStop();
/*        presenter.onStop();
        this.mMainOnClickListener = null;
        this.mMapOnClickListener = null;*/
    //this.mUserViewModel.delete();
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
}
