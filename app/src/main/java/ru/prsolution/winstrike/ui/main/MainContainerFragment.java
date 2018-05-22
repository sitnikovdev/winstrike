package ru.prsolution.winstrike.ui.main;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import javax.inject.Inject;

import ru.prsolution.winstrike.R;
import ru.prsolution.winstrike.mvp.apimodels.OrderModel;
import ru.prsolution.winstrike.mvp.views.MainScreenView;
import ru.prsolution.winstrike.ui.Screens;
import ru.prsolution.winstrike.WinstrikeApp;
import ru.prsolution.winstrike.subnavigation.LocalCiceroneHolder;
import ru.prsolution.winstrike.ui.common.BackButtonListener;
import ru.prsolution.winstrike.ui.common.RouterProvider;
import ru.prsolution.winstrike.ui.common.ScreenType;
import ru.terrakok.cicerone.Cicerone;
import ru.terrakok.cicerone.Navigator;
import ru.terrakok.cicerone.Router;
import ru.terrakok.cicerone.android.SupportAppNavigator;

public class MainContainerFragment extends Fragment implements RouterProvider, BackButtonListener {
    private static final String EXTRA_NAME = "tcf_extra_name";

    private Navigator navigator;

    @Inject
    LocalCiceroneHolder ciceroneHolder;

    public static MainContainerFragment getNewInstance(String name) {
        MainContainerFragment fragment = new MainContainerFragment();

        Bundle arguments = new Bundle();
        arguments.putString(EXTRA_NAME, name);
        fragment.setArguments(arguments);

        return fragment;
    }

    private String getContainerName() {
        return getArguments().getString(EXTRA_NAME);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        WinstrikeApp.INSTANCE.getAppComponent().inject(this);
        super.onCreate(savedInstanceState);
    }

    private Cicerone<Router> getCicerone() {
        return ciceroneHolder.getCicerone(getContainerName());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tab_container, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ArrayList<OrderModel> orderModels =  ((MainScreenActivity)getActivity()).getOrders();

        if (getFragmentManager().findFragmentById(R.id.ab_container).getTag().equals("MAIN")) {
            getCicerone().getRouter().replaceScreen(Screens.START_SCREEN, 0);
        }

        if (getFragmentManager().findFragmentById(R.id.ab_container).getTag().equals("PLACES")) {
            getCicerone().getRouter().replaceScreen(Screens.PLACE_SCREEN, orderModels);
        }

        if (getFragmentManager().findFragmentById(R.id.ab_container).getTag().equals("USER")) {
            getCicerone().getRouter().replaceScreen(Screens.USER_SCREEN, 0);
        }

        if (getFragmentManager().findFragmentById(R.id.ab_container).getTag().equals("CHOOSE")) {
            getCicerone().getRouter().replaceScreen(Screens.CHOOSE_SCREEN, 0);
        }

        if (getFragmentManager().findFragmentById(R.id.ab_container).getTag().equals("MAP")) {
//              MapScreenFragment.getNewInstance(getContainerName(), 0);
            getCicerone().getRouter().replaceScreen(Screens.MAP_SCREEN, 0);
        }

        if (getFragmentManager().findFragmentById(R.id.ab_container).getTag().equals("PAY")) {
            getCicerone().getRouter().replaceScreen(Screens.PAY_SCREEN, 0);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        getCicerone().getNavigatorHolder().setNavigator(getNavigator());
    }

    @Override
    public void onPause() {
        getCicerone().getNavigatorHolder().removeNavigator();
        super.onPause();
    }

    private Navigator getNavigator() {
        if (navigator == null) {
            navigator = new SupportAppNavigator(getActivity(), getChildFragmentManager(), R.id.ftc_container) {

                @Override
                protected Intent createActivityIntent(Context context, String screenKey, Object data) {
                    if (screenKey.equals(Screens.GITHUB_SCREEN)) {
                        return new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/terrakok/Cicerone"));
                    }
                    return null;
                }

                @Override
                protected Fragment createFragment(String screenKey, Object data) {
                    Fragment fragment = null;
                    if (screenKey.equals(Screens.START_SCREEN)) {
                        fragment = HomeScreenFragment.getNewInstance(getContainerName(), (int) data);
                    }
                    if (screenKey.equals(Screens.PLACE_SCREEN)) {
                        fragment = PlaceScreenFragment.getNewInstance(getContainerName(), (ArrayList<OrderModel>) data);
                    }
                    if (screenKey.equals(Screens.USER_SCREEN)) {
                        fragment = ProfileScreenFragment.getNewInstance(getContainerName(), (int) data);
                    }
                    if (screenKey.equals(Screens.CHOOSE_SCREEN)) {
                        fragment = ChooseScreenFragment.getNewInstance(getContainerName(), (int) data);
                    }
                    if (screenKey.equals(Screens.MAP_SCREEN)) {
                        fragment = MapScreenFragment.getNewInstance(getContainerName(), (int) data);
                    }
                    if (screenKey.equals(Screens.PAY_SCREEN)) {
                        fragment = PayScreenFragment.getNewInstance(getContainerName(), (String) data);
                    }

                    return fragment;
                }

                @Override
                protected void exit() {
                    ((RouterProvider) getActivity()).getRouter().exit();
                }
            };
        }
        return navigator;
    }

    @Override
    public Router getRouter() {
        return getCicerone().getRouter();
    }

    @Override
    public boolean onBackPressed() {
        Fragment fragment = getChildFragmentManager().findFragmentById(R.id.ftc_container);
        if (fragment != null
                && fragment instanceof BackButtonListener
                && ((BackButtonListener) fragment).onBackPressed()) {
            return true;
        } else {
//            ((RouterProvider) getActivity()).getRouter().finishChain();
            ((RouterProvider) getActivity()).getRouter().replaceScreen(Screens.START_SCREEN,0);
            MainScreenActivity activity = (MainScreenActivity) getActivity();
            activity.initMainToolbar(true,"Winstrike",true,ScreenType.MAIN);
            activity.showBottomTab();
            activity.highlightTab(MainScreenView.HOME_TAB_POSITION);
            activity.setProfileScreenInterfaceVisibility(false);

            return true;
        }
    }

}
