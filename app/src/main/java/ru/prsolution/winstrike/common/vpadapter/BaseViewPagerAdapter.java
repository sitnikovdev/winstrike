package ru.prsolution.winstrike.common.vpadapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


/**
 * BottomNav
 * Created by Suleiman19 on 6/12/17.
 * Copyright (c) 2017. Suleiman Ali Shakir. All rights reserved.
 */

public class BaseViewPagerAdapter extends SmartFragmentStatePagerAdapter {
    private final List<Fragment> fragments = new ArrayList<>();
    private final static List<String> mFragmentTitleList = new ArrayList<>();

    public BaseViewPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    public void addFragments(Fragment fragment) {
        fragments.add(fragment);
    }

    public void addFragments(Fragment fragment, String title) {
        fragments.add(fragment);
        mFragmentTitleList.add(title);
    }

    @Override
    public Fragment getItem(int position) {
        Log.e("fragment", "Fragment: " + fragments.get(position));
        Log.e("fragment", "This position is: " + position);
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
         return mFragmentTitleList.get(position);
    }
}
