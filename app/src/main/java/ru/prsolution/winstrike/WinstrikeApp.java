package ru.prsolution.winstrike;

import android.app.Application;
import android.util.DisplayMetrics;

import com.crashlytics.android.Crashlytics;

import java.io.File;

import io.fabric.sdk.android.Fabric;
import retrofit2.Retrofit;
import ru.prsolution.winstrike.db.entity.UserEntity;
import ru.prsolution.winstrike.di.AppComponent;
import ru.prsolution.winstrike.di.DaggerAppComponent;
import ru.prsolution.winstrike.di.module.ContextModule;
import ru.prsolution.winstrike.di.module.NetworkModule;
import ru.prsolution.winstrike.mvp.apimodels.RoomLayout;
import ru.prsolution.winstrike.mvp.models.SeatModel;
import ru.prsolution.winstrike.networking.Service;


public class WinstrikeApp extends Application {
    public static final boolean DEBUG = false;
    public static WinstrikeApp INSTANCE;
    private AppComponent sAppComponent;
    private UserEntity user;
    private SeatModel seat;
    private RoomLayout roomLayout;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
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
                    .networkModule(new NetworkModule(cacheFile))
                    .build();
        }
        return sAppComponent;
    }

    public Service getService() {
        File cacheFile = new File(getCacheDir(), "responses");
        NetworkModule networkModule = new NetworkModule(cacheFile);
        Retrofit retrofit = networkModule.provideCall();
        return new Service(networkModule.providesNetworkService(retrofit));
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

    public UserEntity getUser() {
        return this.user;
    }

    public void setSeat(SeatModel seat) {
        this.seat = seat;
    }

    public SeatModel getSeat() {
        return seat;
    }

    public RoomLayout getRoomLayout() {
        return this.roomLayout;
    }

    public void setRoomLayout(RoomLayout roomLayout) {
        this.roomLayout = roomLayout;
    }
}
