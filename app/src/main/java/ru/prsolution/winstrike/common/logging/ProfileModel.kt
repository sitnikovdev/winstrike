package ru.prsolution.winstrike.common.logging

/**
 * Created by designer on 07/03/2018.
 */
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ProfileModel {

    @SerializedName("phone")
    @Expose
    var phone: String? = null

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("old_password")
    @Expose
    var password: String? = null

}

