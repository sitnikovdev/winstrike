package ru.prsolution.winstrike.common;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/*
 * Created by oleg on 05.02.2018.
 */

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());

        onViewReady(savedInstanceState, getIntent());
    }

    @CallSuper
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {

    }

    protected abstract int getContentView();


}
