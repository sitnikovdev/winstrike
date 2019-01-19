package ru.prsolution.winstrike.mvp.apimodels
import android.graphics.drawable.Drawable
import com.google.gson.annotations.SerializedName
import ru.prsolution.winstrike.R

class Room  {

    @SerializedName("name")
    var name: String? = null

    @SerializedName("public_id")
    var publicId: String? = null

    @SerializedName("metro")
    var metro: String? = null

    @SerializedName("room_layout_pid")
    var roomLayoutPid: String? = null

    @SerializedName("description")
    var description: String? = null

    @SerializedName("image_url")
    var imageUrl: String? = null

    var imageDefault: Int = R.drawable.vip

    @SerializedName("locale")
    var locale: String? = null

    @SerializedName("usual_description")
    var usualDescription: String? = null

    @SerializedName("usual_image_url")
    var usualImageUrl: String? = null

    @SerializedName("vip_image_url")
    var vipImageUrl: String? = null

    @SerializedName("vip_description")
    var vipDescription: String? = null
}

