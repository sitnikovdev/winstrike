package ru.prsolution.winstrike;

import android.app.Application;
import android.os.Environment;
import android.util.DisplayMetrics;

import com.squareup.leakcanary.LeakCanary;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

import ru.prsolution.winstrike.di.AppComponent;
import ru.prsolution.winstrike.di.DaggerAppComponent;
import ru.prsolution.winstrike.di.module.AppRepositoryModule;
import ru.prsolution.winstrike.di.module.ContextModule;
import ru.prsolution.winstrike.networking.NetworkModule;
import timber.log.Timber;


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
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
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


    public float getDisplayWidhtDp() {
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        return dpWidth;
    }

    public float getDisplayHeightDp() {
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        return dpHeight;
    }

    public float getDisplayHeightPx() {
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        float dpHeight = displayMetrics.heightPixels;
        return dpHeight;
    }

    public float getDisplayWidhtPx() {
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels;
        return dpWidth;
    }

    //Copy db on SD-card
    public void dbCopy(String dbname) {
        try {
            File sd = Environment.getExternalStorageDirectory();

            if (sd.canWrite()) {
                String currentDBPath = "/data/data/" + getPackageName() + "/databases/" + dbname;
                String backupDBPath = dbname + ".db";
                File currentDB = new File(currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }
            }
        } catch (UnknownError e) {
            Timber.d("Can't write db  %s to sd card. Cause: %s", dbname, e.getCause());
        } catch (Exception e) {
            Timber.d("Can't write db  %s to sd card. Cause: %s", dbname, e.getCause());
        }
    }
}
