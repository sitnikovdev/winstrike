package ru.prsolution.winstrike.ui.guides;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.prsolution.winstrike.R;
import ru.prsolution.winstrike.common.ViewPagerCustomDuration;

/*
 * Created by oleg on 31.01.2018.
 */

public class GuideActivity extends AppCompatActivity {
    private PagerAdapter pagerAdapter;
    private ViewPager viewPager;
    private Integer GUIDE_COUNT = 3;
    @BindView(R.id.vp_pager)
    ViewPager vp_pager;

    public ViewPager getViewPager() {
        ViewPagerCustomDuration vp = (ViewPagerCustomDuration) findViewById(R.id.vp_pager);
        vp.setScrollDurationFactor(4.0); // make the animation twice as slow
        return vp;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fmt_guides);
        ButterKnife.bind(this);


        pagerAdapter = new GuidesFragmentPagerAdapter(getSupportFragmentManager());
        vp_pager.setAdapter(pagerAdapter);


    }

    private class GuidesFragmentPagerAdapter extends FragmentPagerAdapter {

        public GuidesFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return GuideFragment.newInstance(position);
        }

        @Override
        public int getCount() {
           return  GUIDE_COUNT;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
