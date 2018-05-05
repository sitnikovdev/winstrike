package ru.prsolution.winstrike.common.logging;
/*
 * Created by oleg on 08.03.2018.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import ru.prsolution.winstrike.mvp.apimodels.User;


public class UsersServices {

    @SerializedName("users")
    @Expose
    private List<User> users = null;

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

}
