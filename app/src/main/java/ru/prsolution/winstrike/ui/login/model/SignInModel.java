package ru.prsolution.winstrike.ui.login.model;

/**
 * Created by designer on 07/03/2018.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SignInModel {

    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("password")
    @Expose
    private String password;

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

    public SignInModel(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public SignInModel() {

    }
}

