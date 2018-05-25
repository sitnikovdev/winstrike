package ru.prsolution.winstrike.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.prsolution.winstrike.R;
import ru.prsolution.winstrike.WinstrikeApp;
import ru.prsolution.winstrike.common.PlacesAdapter;
import ru.prsolution.winstrike.mvp.apimodels.OrderModel;
import ru.prsolution.winstrike.mvp.presenters.PlacesPresenter;
import ru.prsolution.winstrike.mvp.views.PlacesView;
import ru.prsolution.winstrike.networking.Service;
import ru.prsolution.winstrike.common.BackButtonListener;
import ru.prsolution.winstrike.common.BottomDecoratorHelper;
import ru.prsolution.winstrike.common.RouterProvider;
import timber.log.Timber;


/**
 * Created by terrakok 26.11.16
 */
public class PlaceScreenFragment extends MvpAppCompatFragment implements PlacesView, BackButtonListener {
    private static final String EXTRA_NAME = "extra_name";
    private static final String ORDERS = "extra_number";
    private PlacesAdapter adapter;
    private List<OrderModel> mPayList = new ArrayList<>();


    @Nullable
    @BindView(R.id.rv_pay)
    RecyclerView rv_pay;

    @Inject
    Service service;

    @InjectPresenter
    PlacesPresenter presenter;


    @ProvidePresenter
    PlacesPresenter provideMainScreenPresenter() {
        return new PlacesPresenter(service,
                ((RouterProvider) getParentFragment()).getRouter()
                , getArguments().getParcelableArrayList(ORDERS)
        );
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        WinstrikeApp.INSTANCE.getAppComponent().inject(this);
        super.onCreate(savedInstanceState);
        mPayList = presenter.getOrders();
        Timber.d("UserEntity order list size: %s", mPayList.size());
    }

    public static PlaceScreenFragment getNewInstance(String name, ArrayList<OrderModel> orders) {
        PlaceScreenFragment fragment = new PlaceScreenFragment();
        Bundle arguments = new Bundle();
        arguments.putString(EXTRA_NAME, name);
        arguments.putParcelableArrayList(ORDERS, orders);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view;
        if (!mPayList.isEmpty()) {
            view = inflater.inflate(R.layout.fmt_paid, container, false);
            ButterKnife.bind(this, view);
            initRView();
        } else {
            view = inflater.inflate(R.layout.fmt_nopaid, container, false);
            ButterKnife.bind(this, view);
        }

        return view;
    }

    private void initRView() {
        rv_pay.addItemDecoration(new BottomDecoratorHelper(350));
        rv_pay.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        adapter = new PlacesAdapter(getContext(), mPayList);
        rv_pay.setAdapter(adapter);
    }


    @Override
    public boolean onBackPressed() {
//        presenter.onBackPressed();
        startActivity(new Intent(getActivity(), MainScreenActivity.class));
        return true;
    }


    @Override
    public void onGetOrdersSuccess(List<OrderModel> orders) {
/*        Timber.d("Success get layout data from server: %s", orders);
        adapter = new PlacesAdapter(getContext(), orders, (OnItemPayClickListener) getActivity());
        rv_pay.setAdapter(adapter);*/
    }

    @Override
    public void onGetOrdersFailure(String appErrorMessage) {
        Timber.d("Failure get layout from server: %s", appErrorMessage);
        if (appErrorMessage.contains("416")) toast("Выбран не рабочий диапазон времени");
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.onStop();
    }

    @Override
    public void showWait() {
    }

    @Override
    public void removeWait() {
    }


    protected void toast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
}
