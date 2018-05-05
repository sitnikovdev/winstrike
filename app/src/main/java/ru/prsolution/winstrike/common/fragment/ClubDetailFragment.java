package ru.prsolution.winstrike.common.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.prsolution.winstrike.R;
import ru.prsolution.winstrike.common.rvadapter.ClubDetailAdapter;
import ru.prsolution.winstrike.common.entity.ClubModel;


public class ClubDetailFragment extends Fragment {
    private List<ClubModel> mClubList = new ArrayList<>();

    public static int PAGES = 3;
    // You can choose a bigger number for LOOPS, but you know, nobody will fling
    // more than 1000 times just in order to test your "infinite" ViewPager :D
    public static int LOOPS = 1000;
    public static int FIRST_PAGE = PAGES * LOOPS / 2;
    @BindView(R.id.view_pager_seat)
    ViewPager view_pager_seat;

    public ClubDetailFragment() {
    }

    public static ClubDetailFragment newInstance(int index) {
        ClubDetailFragment fragment = new ClubDetailFragment();
        Bundle b = new Bundle();
        b.putInt("index", index);
        fragment.setArguments(b);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fmt_club_detail, container, false);
        ButterKnife.bind(this, rootView);

        ClubDetailAdapter adapter = new ClubDetailAdapter(this, getActivity().getSupportFragmentManager());
        view_pager_seat.setAdapter(adapter);
        view_pager_seat.setPageTransformer(false, adapter);
        view_pager_seat.setCurrentItem(FIRST_PAGE);
        view_pager_seat.setOffscreenPageLimit(3);

        // Set margin for pages as a negative number, so a part of next and
        // previous pages will be showed
        view_pager_seat.setPageMargin(-600);

        return rootView;
    }

}


