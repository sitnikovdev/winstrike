package ru.prsolution.winstrike;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.File;

import ru.prsolution.winstrike.common.utils.TinyDB;
import ru.prsolution.winstrike.di.AppComponent;
import ru.prsolution.winstrike.mvp.apimodels.AuthResponse;
import ru.prsolution.winstrike.mvp.apimodels.User;
import ru.prsolution.winstrike.ui.common.MapInfoSingleton;

/**
 * Created by ennur on 6/28/16.
 */
public class BaseApp extends AppCompatActivity {
    AppComponent appComponent;
    public ProgressDialog mProgressDialog;
    private TinyDB tinyDB;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tinyDB = new TinyDB(this);
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }



    protected void saveUser(AuthResponse authResponse) {
/*        tinyDB.putString("token", authResponse.getToken());
        tinyDB.putString("public_id", authResponse.getUsersList().getPublicId());
        tinyDB.putString("phone", authResponse.getUsersList().getPhone());
        if (authResponse.getUsersList().getEmail() != null) {
            tinyDB.putString("email", authResponse.getUsersList().getEmail());
        }
        tinyDB.putString("role", authResponse.getUsersList().getRole());
        tinyDB.putBoolean("loggedin", true);*/
    }

    protected void setOperation() {
        tinyDB.putString("operation", "createNewUser");
    }



    protected void clearUser() {
        tinyDB.putString("token", "");
        tinyDB.putString("public_id", "");
        tinyDB.putString("phone", "");
    }

    protected void setConfirmed(Boolean confirmed) {
        tinyDB.putBoolean("confirmed", confirmed);
    }


    protected void saveUserName(String name) {
        if (MapInfoSingleton.getInstance().getUser() == null) {
            User user = new User();
            MapInfoSingleton.getInstance().setUser(user);
            MapInfoSingleton.getInstance().getUser().setName(name);
        }
    }


    public void clearApplicationData() {
        File cache = getCacheDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                if (!s.equals("lib")) {
                    deleteDir(new File(appDir, s));
                    Log.i("TAG", "File /data/data/APP_PACKAGE/" + s + " DELETED");
                }
            }
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        return dir.delete();
    }



}
