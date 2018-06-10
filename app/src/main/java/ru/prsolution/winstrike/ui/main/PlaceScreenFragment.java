package ru.prsolution.winstrike.ui.main;

import android.content.Intent;
import android.databinding.DataBindingUtil;
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
import ru.prsolution.winstrike.common.utils.AuthUtils;
import ru.prsolution.winstrike.common.vpadapter.OrdersViewModel;
import ru.prsolution.winstrike.common.vpadapter.SeatAdapter;
import ru.prsolution.winstrike.databinding.FmtPaidBinding;
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

    private List<OrdersViewModel> mOrders = new ArrayList<>();


    FmtPaidBinding binding;
    private  SeatAdapter mSeatAdapter;

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
        this.mPayList = getArguments().getParcelableArrayList(ORDERS);
        mSeatAdapter = new SeatAdapter(mPayList);
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
            binding = DataBindingUtil.inflate(inflater, R.layout.fmt_paid, container, false);
            view = binding.getRoot();
            binding.setAdapter(mSeatAdapter);
            initRView();
        } else {
            view = inflater.inflate(R.layout.fmt_nopaid, container, false);
            ButterKnife.bind(this, view);
        }

        return view;
    }

    private void initRView() {
        binding.rvPay.addItemDecoration(new BottomDecoratorHelper(350));
        binding.rvPay.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvPay.setAdapter(mSeatAdapter);
    }


    @Override
    public boolean onBackPressed() {
//        presenter.onBackPressed();
        startActivity(new Intent(getActivity(), MainScreenActivity.class));
        return true;
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
