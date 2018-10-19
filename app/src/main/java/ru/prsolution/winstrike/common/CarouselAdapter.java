package ru.prsolution.winstrike.common;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import ru.prsolution.winstrike.R;
import ru.prsolution.winstrike.WinstrikeApp;

/**
 * Created by designer on 02/04/2018.
 */

public class CarouselAdapter extends FragmentPagerAdapter implements ViewPager.PageTransformer {

    private FragmentActivity context;

    private final List<Fragment> mFragmentList = new ArrayList<>();
    private int PAGES = 3;
    float dpHeight, dpWidth;

    public Float BIG_SCALE;
    public Float SMALL_SCALE;
    public Float DIFF_SCALE;

    public void setPagesCount(int pages) {
       this.PAGES = pages;
    }

    public CarouselAdapter(FragmentActivity context) {
        super(context.getSupportFragmentManager());
        this.context = context;


        dpHeight = WinstrikeApp.getInstance().getDisplayHeightDp();
        dpWidth = WinstrikeApp.getInstance().getDisplayWidhtDp();

        if (dpWidth <= 360.0) {
            BIG_SCALE = 0.9f;
            SMALL_SCALE = 0.5f;
        } else {
            BIG_SCALE = 1.0f;
            SMALL_SCALE = 0.6f;
        }
        DIFF_SCALE = BIG_SCALE - SMALL_SCALE;
    }


    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }


    public void addFragment(Fragment fragment, int position) {
        mFragmentList.add(position, fragment);
    }

    @Override
    public int getCount() {
        return PAGES;
    }

    @Override
    public void transformPage(@NonNull View page, float position) {
        ChooseSeatLinearLayout root = page.findViewById(R.id.root);
        ChooseSeatLinearLayout myLinearLayout = root;
        Float scale = BIG_SCALE;
        if (position > 0) {
            scale -= position * DIFF_SCALE;
        } else {
            scale += position * DIFF_SCALE;
        }
        if (scale < 0) scale = 0f;
        myLinearLayout.setScaleBoth(scale);
    }

}


