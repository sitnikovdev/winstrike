package ru.prsolution.winstrike.datasource.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class User {

    @SerializedName("confirmed")
    @Expose
    var confirmed: Boolean? = null
    @SerializedName("confirmed_on")
    @Expose
    var confirmedOn: Any? = null
    @SerializedName("email")
    @Expose
    var email: String? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("phone")
    @Expose
    var phone: String? = null
    @SerializedName("public_id")
    @Expose
    var publicId: String? = null
    @SerializedName("registered_on")
    @Expose
    var registeredOn: String? = null
    @SerializedName("social_id")
    @Expose
    var socialId: Any? = null

    @SerializedName("role")
    @Expose
    var role: String? = null

    @SerializedName("user_id")
    @Expose
    var id: String? = null

    var password: String? = null

}
