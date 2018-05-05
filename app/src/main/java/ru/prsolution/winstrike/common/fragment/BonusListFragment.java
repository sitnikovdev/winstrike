package ru.prsolution.winstrike.common.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.prsolution.winstrike.R;
import ru.prsolution.winstrike.common.entity.BonusModel;
import ru.prsolution.winstrike.common.rvadapter.BonusAdapter;
import ru.prsolution.winstrike.common.rvlistener.OnItemBonusClickListener;


public class BonusListFragment extends Fragment {
    public static final String TAG = BonusListFragment.class.getSimpleName();
    private List<BonusModel> mBonusList = new ArrayList<>();

    @Nullable
    @BindView(R.id.rv_bonus)
    RecyclerView rv_bonus;


    public BonusListFragment() {
    }

    public static BonusListFragment newInstance(int index) {
        BonusListFragment fragment = new BonusListFragment();
        Bundle b = new Bundle();
        b.putInt("index", index);
        fragment.setArguments(b);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView;
        Boolean isNoPay = true;
//        View rootView = inflater.inflate(R.layout.fmt_bonus, container, false);


        if (isNoPay) {
            rootView = inflater.inflate(R.layout.item_nopaid, container, false);
            ButterKnife.bind(this, rootView);
        } else {
            rootView = inflater.inflate(R.layout.fmt_bonus, container, false);
            ButterKnife.bind(this, rootView);
            rv_bonus.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

            BonusAdapter adapter = new BonusAdapter(getContext(), mBonusList, (OnItemBonusClickListener) getActivity());
            rv_bonus.setAdapter(adapter);
        }

        return rootView;
    }

}


