package ru.prsolution.winstrike.common.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.prsolution.winstrike.R;
import ru.prsolution.winstrike.mvp.apimodels.OrderModel;


public class PlacesListFragment extends Fragment {
    public static final String TAG = PlacesListFragment.class.getSimpleName();
    private List<OrderModel> mPayList = new ArrayList<>();

    @Nullable
    @BindView(R.id.rv_pay)
    RecyclerView rv_pay;

    Boolean isNoPay;


    public PlacesListFragment() {
    }

    public static PlacesListFragment newInstance(int index) {
        PlacesListFragment fragment = new PlacesListFragment();
        Bundle b = new Bundle();
        b.putInt("index", index);
        fragment.setArguments(b);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        isNoPay = true;
        View rootView;

        if (isNoPay) {
            rootView = inflater.inflate(R.layout.item_nopaid, container, false);
            ButterKnife.bind(this, rootView);
        } else {
            rootView = inflater.inflate(R.layout.fmt_paid, container, false);
            ButterKnife.bind(this, rootView);
        }

        return rootView;
/*        rv_pay.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        PayAdapter.EMPTY_PAY = 0;
        PayAdapter adapter = new PayAdapter(getContext(), mPayList, (OnItemPayClickListener) getActivity());
        rv_pay.setAdapter(adapter);

        return rootView;*/
    }

}


