package ru.prsolution.winstrike.ui.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import butterknife.ButterKnife;
import ru.prsolution.winstrike.R;
import ru.prsolution.winstrike.mvp.presenters.HomePresenter;
import ru.prsolution.winstrike.mvp.views.HomeView;
import ru.prsolution.winstrike.common.BackButtonListener;
import ru.prsolution.winstrike.common.RouterProvider;
import ru.prsolution.winstrike.ui.main.MainScreenActivity;


/**
 * Created by terrakok 26.11.16
 */
public class HomeScreenFragment extends MvpAppCompatFragment implements HomeView, BackButtonListener {
    private static final String EXTRA_NAME = "extra_name";
    private static final String EXTRA_NUMBER = "extra_number";

    @InjectPresenter
    HomePresenter presenter;

    @ProvidePresenter
    HomePresenter provideMainScreenPresenter() {
        return new HomePresenter(((MainScreenActivity) getActivity()).getService(),
                ((RouterProvider) getParentFragment()).getRouter()
        );
    }

    public static Fragment getNewInstance(String name, int number) {
        Fragment fragment;
        fragment = new HomeScreenFragment();
        Bundle arguments = new Bundle();
        arguments.putString(EXTRA_NAME, name);
        arguments.putInt(EXTRA_NUMBER, number);
        fragment.setArguments(arguments);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fmt_home, container, false);
        ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public boolean onBackPressed() {
        return false;
    }


    @Override
    public void showWait() { }

    @Override
    public void removeWait() {

    }

}
