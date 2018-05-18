package ru.prsolution.winstrike.mvp.apimodels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class NewPasswordModel {
    @SerializedName("username")
    @Expose
    var username: String? = null

    @SerializedName("new_password")
    @Expose
    var new_password: String? = null

}
