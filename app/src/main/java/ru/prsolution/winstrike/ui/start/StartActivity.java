package ru.prsolution.winstrike.ui.start;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;


import javax.inject.Inject;

import ru.prsolution.winstrike.WinstrikeApp;
import ru.prsolution.winstrike.ui.guides.GuideActivity;
import ru.prsolution.winstrike.ui.login.SignInActivity;
import ru.prsolution.winstrike.ui.login.UserConfirmActivity;
import ru.prsolution.winstrike.common.FavPushDialogActivity;
import ru.prsolution.winstrike.common.HelpActivity;
import ru.prsolution.winstrike.ui.common.HtmlViewer;
import ru.prsolution.winstrike.common.LocationActivity;
import ru.prsolution.winstrike.ui.main.MainScreenActivity;
import ru.prsolution.winstrike.ui.login.RegisterActivity;
import ru.prsolution.winstrike.ui.common.YandexWebView;
import timber.log.Timber;

/*
 * Created by oleg on 31.01.2018.
 */

public class StartActivity extends AppCompatActivity {


    private class CrashlyticsTree extends Timber.Tree {
        private static final String CRASHLYTICS_KEY_PRIORITY = "priority";
        private static final String CRASHLYTICS_KEY_TAG = "tag";
        private static final String CRASHLYTICS_KEY_MESSAGE = "message";

        @Override
        protected void log(int priority, String tag, String message, Throwable t) {

            if (priority == Log.VERBOSE || priority == Log.DEBUG || priority == Log.INFO) {
                return;
            }

            /*Crashlytics.setInt(CRASHLYTICS_KEY_PRIORITY,priority);
            Crashlytics.setString(CRASHLYTICS_KEY_TAG,tag);
            Crashlytics.setString(CRASHLYTICS_KEY_MESSAGE,message);

            if (t == null) {
                Crashlytics.logException(new Exception(message));
            } else {
                Crashlytics.logException(t);
            }*/
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;


//        Fabric.with(this, new Crashlytics());
/*
        CrashlyticsCore core = new CrashlyticsCore.Builder()
                .disabled(WinstrikeApp.DEBUG)
                .build();
        Fabric.with(this, new Crashlytics.Builder().core(core).build());
*/

        if (WinstrikeApp.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new CrashlyticsTree());
        }

        Timber.e("StartActivity: AppConfig.DEBUG: %s", WinstrikeApp.DEBUG);

        Intent splashActivity = new Intent(this, SplashActivity.class);
        Intent locationActivity = new Intent(this, LocationActivity.class);
        Intent loginActivity = new Intent(this, SignInActivity.class);
        Intent registerActivity = new Intent(this, RegisterActivity.class);
//        Intent smsCodeActivity = new Intent(this, __SmsCodeActivity.class);
        Intent guideActivity = new Intent(this, GuideActivity.class);
        Intent favPushDialog = new Intent(this, FavPushDialogActivity.class);
        Intent helpActivity = new Intent(this, HelpActivity.class);
        Intent mainScreen = new Intent(this, MainScreenActivity.class);
        Intent testWebView = new Intent(this, YandexWebView.class);
        Intent webViewTest = new Intent(this, HtmlViewer.class);
        Intent smsCodeActivity = new Intent(this, UserConfirmActivity.class);

        // ATTENTION: This was auto-generated to handle app links.
        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();

        startActivity(registerActivity);
    }

}
