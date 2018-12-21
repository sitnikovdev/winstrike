package ru.prsolution.winstrike.ui.start;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;

import io.fabric.sdk.android.Fabric;
import ru.prsolution.winstrike.WinstrikeApp;
import timber.log.Timber;

/*
 * Created by oleg on 31.01.2018.
 */

public class StartActivity extends AppCompatActivity {


    public class NotLoggingTree extends Timber.Tree {
        @Override
        protected void log(final int priority, final String tag, final String message, final Throwable throwable) {
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Fabric.with(this, new Crashlytics());

        CrashlyticsCore core = new CrashlyticsCore.Builder()
                .disabled(WinstrikeApp.DEBUG)
                .build();
        Fabric.with(this, new Crashlytics.Builder().core(core).build(), new Crashlytics());

        if (WinstrikeApp.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
        else {
            Timber.plant(new NotLoggingTree());
        }
/*        else {
            Timber.plant(new CrashlyticsTree());
        }*/

        Timber.e("StartActivity: AppConfig.DEBUG: %s", WinstrikeApp.DEBUG);

        Intent splashActivity = new Intent(this, SplashActivity.class);
        startActivity(splashActivity);
    }

}
