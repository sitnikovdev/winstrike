package ru.prsolution.winstrike.common.rvadapter;
/*
 * Created by oleg on 01.02.2018.
 */

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.prsolution.winstrike.R;
import ru.prsolution.winstrike.ui.common.ChooseSeatLinearLayout;
import ru.prsolution.winstrike.common.fragment.ClubDetailFragment;

public class ClubDetailAdapter extends FragmentPagerAdapter implements ViewPager.PageTransformer {
    private Float scale = 0f;
    public static Float BIG_SCALE = 1.0f;
    static Float SMALL_SCALE = 0.6f;
    static Float DIFF_SCALE = BIG_SCALE - SMALL_SCALE;
    @BindView(R.id.root)
    ChooseSeatLinearLayout root;
    ClubDetailFragment context;

    public ClubDetailAdapter(ClubDetailFragment context, FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
         int pos = position;
        // make the first pager bigger than others
        if (pos == ClubDetailFragment.FIRST_PAGE)
            scale = BIG_SCALE;
        else
            scale = SMALL_SCALE;

        pos %= ClubDetailFragment.PAGES;
        return ClubDetailFragment.newInstance(0);
    }

    @Override
    public int getCount() {
        return ClubDetailFragment.PAGES * ClubDetailFragment.LOOPS;
    }

    @Override
    public void transformPage(@NonNull View page, float position) {
        ButterKnife.bind(this, page);
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
