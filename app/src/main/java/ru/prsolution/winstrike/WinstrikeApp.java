package ru.prsolution.winstrike;

import android.app.Application;
import android.util.DisplayMetrics;

import java.io.File;

import ru.prsolution.winstrike.di.AppComponent;
import ru.prsolution.winstrike.di.DaggerAppComponent;
import ru.prsolution.winstrike.di.module.AppRepositoryModule;
import ru.prsolution.winstrike.di.module.ContextModule;
import ru.prsolution.winstrike.networking.NetworkModule;


/**
 * Created by Konstantin Tckhovrebov (aka @terrakok)
 * on 11.10.16
 */

public class WinstrikeApp extends Application {
    public static final boolean DEBUG = true;
    public static WinstrikeApp INSTANCE;
    private AppComponent sAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
    }

    public static WinstrikeApp getInstance() {
        return INSTANCE;
    }


    public AppComponent getAppComponent() {
        if (sAppComponent == null) {
            File cacheFile = new File(getCacheDir(), "responses");
            sAppComponent = DaggerAppComponent.builder()
                    .contextModule(new ContextModule(this))
                    .appRepositoryModule(new AppRepositoryModule(this))
                    .networkModule(new NetworkModule(cacheFile))
                    .build();
        }
        return sAppComponent;
    }


    public float getDisplayWidht() {
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        return dpWidth;
    }

    public float getDisplayHeight() {
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        return dpHeight;
    }

}
