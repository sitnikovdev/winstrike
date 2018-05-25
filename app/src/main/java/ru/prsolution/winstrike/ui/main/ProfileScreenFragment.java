package ru.prsolution.winstrike.ui.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import javax.inject.Inject;

import butterknife.ButterKnife;
import ru.prsolution.winstrike.R;
import ru.prsolution.winstrike.WinstrikeApp;
import ru.prsolution.winstrike.mvp.presenters.ProfilePresenter;
import ru.prsolution.winstrike.mvp.views.ProfileView;
import ru.prsolution.winstrike.networking.Service;
import ru.prsolution.winstrike.common.RouterProvider;


/**
 * Created by terrakok 26.11.16
 */
public class ProfileScreenFragment extends MvpAppCompatFragment implements ProfileView {
    private static final String EXTRA_NAME = "extra_name";
    private static final String EXTRA_NUMBER = "extra_number";

    @Inject
    Service service;

    @InjectPresenter
    ProfilePresenter presenter;

    @ProvidePresenter
    ProfilePresenter provideMainScreenPresenter() {
        return new ProfilePresenter(service,
                ((RouterProvider) getParentFragment()).getRouter()
                , getArguments().getInt(EXTRA_NUMBER)
        );
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        WinstrikeApp.INSTANCE.getAppComponent().inject(this);
        super.onCreate(savedInstanceState);
    }

    public static ProfileScreenFragment getNewInstance(String name, int number) {
        ProfileScreenFragment fragment = new ProfileScreenFragment();
        Bundle arguments = new Bundle();
        arguments.putString(EXTRA_NAME, name);
        arguments.putInt(EXTRA_NUMBER, number);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frm_profile, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

/*    @Override
    public boolean onBackPressed() {
        presenter.onBackPressed();
//        startActivity(new Intent(getActivity(), MainScreenActivity.class));
        return true;
    }*/

}
