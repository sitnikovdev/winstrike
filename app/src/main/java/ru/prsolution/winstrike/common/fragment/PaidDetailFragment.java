package ru.prsolution.winstrike.common.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.aurelhubert.ahbottomnavigation.notification.AHNotification;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.prsolution.winstrike.R;
import ru.prsolution.winstrike.ui.main.MainScreenActivity;


public class PaidDetailFragment extends Fragment {
    @BindView(R.id.bottom_navigation)
    AHBottomNavigation bottomNavigation;


    public PaidDetailFragment() {
    }

    public static PaidDetailFragment newInstance(int index) {
        PaidDetailFragment fragment = new PaidDetailFragment();
        Bundle b = new Bundle();
        b.putInt("index", index);
        fragment.setArguments(b);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fmt_paid_detail, container, false);
        ButterKnife.bind(this, rootView);
        Intent intent = new Intent(getActivity(), MainScreenActivity.class);
        addBottomNavigationItems();
        bottomNavigation.setCurrentItem(1);
        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                switch (position) {
                    case 0:
                        intent.putExtra("fragment", "seatApi");
                        startActivity(intent);
                        break;
                    case 1:
                        intent.putExtra("fragment", "news");
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
                // remove notification badge
                int lastItemPos = bottomNavigation.getItemsCount() - 1;
                if (position == lastItemPos)
                    bottomNavigation.setNotification(new AHNotification(), lastItemPos);

                return true;
            }

        });

        return rootView;
    }

    private void addBottomNavigationItems() {
//        AHBottomNavigationItem item1 = new AHBottomNavigationItem("Чат", R.drawable.ic_chat );
        AHBottomNavigationItem item1 = new AHBottomNavigationItem("Места", R.drawable.ic_place);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem("Новости", R.drawable.ic_news);

        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
    }

}


