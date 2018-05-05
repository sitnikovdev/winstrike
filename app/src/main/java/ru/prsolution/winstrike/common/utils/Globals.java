package ru.prsolution.winstrike.common.utils;
/*
 * Created by oleg on 03.02.2018.
 */

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

import java.util.List;

import ru.prsolution.winstrike.R;

public class Globals {
    private static Globals instance;

    private Globals(){}

    public static synchronized Globals getInstance() {
        if (instance == null) {
            instance = new Globals();
        }
        return instance;
    }


    public static void pushRoot(Fragment fragment,  FragmentActivity activity) {
        ViewGroup container =activity.findViewById(R.id.container);
        container.removeAllViews();

        Fragment topFragment =activity.getSupportFragmentManager().findFragmentById(R.id.container);

        if (topFragment != null) {
          activity.getSupportFragmentManager().popBackStack(topFragment.getClass().getName(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

        while (activity.getSupportFragmentManager().getBackStackEntryCount() > 0) {
           activity.getSupportFragmentManager().popBackStack();
        }

       activity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment, fragment.getClass().getName())
                .commit();
    }


    @NonNull
    public static ViewPager getViewPager(List<Fragment> fragments,FragmentActivity context) {
        FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(context.getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }

        };

        ViewPager viewPager = context.findViewById(R.id.container);
        viewPager.setAdapter(fragmentPagerAdapter);
        viewPager.setCurrentItem(0);
        return viewPager;
    }


}
