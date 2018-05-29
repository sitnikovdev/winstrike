package ru.prsolution.winstrike.mvp.models;

/**
 * Created by designer on 07/03/2018.
 */

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import ru.prsolution.winstrike.BR;

public class LoginViewModel extends BaseObservable {

    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("password")
    @Expose
    private String password;
    private String loginMessage;

    @Bindable
    public String getLoginMessage() {
        return loginMessage;
    }

    @Bindable
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;

    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public LoginViewModel() {
    }

    public void loginSucceeded() {
        loginMessage = "Login Successful!!";
        notifyPropertyChanged(BR.loginMessage);
    }

    public void loginFailed() {
        loginMessage = "Login Failed!!";
        notifyPropertyChanged(BR.loginMessage);
    }

}

