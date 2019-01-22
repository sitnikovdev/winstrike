package ru.prsolution.winstrike.domain.models

/**
 * Created by designer on 07/03/2018.
 */
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class FCMModel {

    @SerializedName("fcm_pid")
    @Expose
    var token: String? = null

}

