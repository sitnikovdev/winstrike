package ru.prsolution.winstrike.common;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.prsolution.winstrike.R;
import ru.prsolution.winstrike.common.fragment.AddGameFragment;
import ru.prsolution.winstrike.common.fragment.DelGameFragment;
import ru.prsolution.winstrike.common.fragment.StartGameFragment;
import ru.prsolution.winstrike.common.vpadapter.BaseViewPagerAdapter;
import ru.prsolution.winstrike.ui.main.MainScreenActivity;


public class FavoriteGamesActivity extends AppCompatActivity {
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbar_title;


    private BaseViewPagerAdapter viewPagerAdapter;
    private final String SCREEN_START = "start";
    private final String SCREEN_DEL = "del";
    private final String SCREEN_ADD = "add";
    private String currentScreen;

    private void setupViewPager(ViewPager viewPager) {
        viewPagerAdapter = new BaseViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragments(StartGameFragment.newInstance());
        viewPagerAdapter.addFragments(AddGameFragment.newInstance());
        viewPagerAdapter.addFragments(DelGameFragment.newInstance());
        viewPager.setAdapter(viewPagerAdapter);
    }


    void setToolbarBackAction() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_arrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        startActivity(new Intent(this, MainScreenActivity.class));
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_favgames);
        ButterKnife.bind(this);

        setToolbarTitle("Любимые игры");
        setToolbarBackAction();

        Bundle extras = getIntent().getExtras();
        if (savedInstanceState == null) {
            setupViewPager(viewPager);
            viewPager.setCurrentItem(0);
            if (extras == null) {
                currentScreen = null;
            } else {
                currentScreen = extras.getString("screen");
            }
        }
        Log.d("myLog", " Screen is " + currentScreen);

        // MAIN Activity
        if (currentScreen != null)
            switch (currentScreen) {
                case SCREEN_START:
                    viewPager.setCurrentItem(0);
                    break;
                case SCREEN_ADD:
                    viewPager.setCurrentItem(1);
                    break;
                case SCREEN_DEL:
                    viewPager.setCurrentItem(2);
                    break;
            }
    }

    public void setToolbarTitle(String title) {
        toolbar_title.setText(title);
    }

}


