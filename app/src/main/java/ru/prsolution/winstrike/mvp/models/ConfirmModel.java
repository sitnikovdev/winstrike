package ru.prsolution.winstrike.mvp.models;

/**
 * Created by designer on 07/03/2018.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ConfirmModel {

    @SerializedName("username")
    @Expose
    private String phone;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}


