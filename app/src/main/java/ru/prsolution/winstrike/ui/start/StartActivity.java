package ru.prsolution.winstrike.ui.start;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;

import io.fabric.sdk.android.Fabric;
import ru.prsolution.winstrike.WinstrikeApp;
import timber.log.Timber;

/*
 * Created by oleg on 31.01.2018.
 */

public class StartActivity extends AppCompatActivity {


/*    private class CrashlyticsTree extends Timber.Tree {
        private static final String CRASHLYTICS_KEY_PRIORITY = "priority";
        private static final String CRASHLYTICS_KEY_TAG = "tag";
        private static final String CRASHLYTICS_KEY_MESSAGE = "message";

        @Override
        protected void log(int priority, String tag, String message, Throwable t) {

            if (priority == Log.VERBOSE || priority == Log.DEBUG || priority == Log.INFO) {
                return;
            }

            Crashlytics.setInt(CRASHLYTICS_KEY_PRIORITY, priority);
            Crashlytics.setString(CRASHLYTICS_KEY_TAG, tag);
            Crashlytics.setString(CRASHLYTICS_KEY_MESSAGE, message);

            if (t == null) {
                Crashlytics.logException(new Exception(message));
            } else {
                Crashlytics.logException(t);
            }
        }
    }*/

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
        } else {
            Timber.plant(new CrashlyticsTree());
        }

        Timber.e("StartActivity: AppConfig.DEBUG: %s", WinstrikeApp.DEBUG);

        Intent splashActivity = new Intent(this, SplashActivity.class);
        startActivity(splashActivity);
    }

}
