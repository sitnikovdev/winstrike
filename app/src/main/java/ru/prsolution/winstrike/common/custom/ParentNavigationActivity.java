package ru.prsolution.winstrike.common.custom;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.prsolution.winstrike.R;

/*
 * Created by oleg on 31.01.2018.
 */

public class ParentNavigationActivity extends AppCompatActivity {
    @BindView(R.id.root_settings)
    public
    ConstraintLayout root_settings;


    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
        setupMenu();
    }

    private void setupMenu() {
        ConstraintLayout left_drawer = root_settings;
        NavigationLayout navigationLayout = new NavigationLayout(getApplicationContext(), left_drawer);
        left_drawer.addView(navigationLayout);
    }
}
