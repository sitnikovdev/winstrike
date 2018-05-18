package ru.prsolution.winstrike.mvp.apimodels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ConfirmSmsModel {
    @SerializedName("username")
    @Expose
    var username: String? = null

}
