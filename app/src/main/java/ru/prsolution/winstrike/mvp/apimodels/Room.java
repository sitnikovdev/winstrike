
package ru.prsolution.winstrike.mvp.apimodels;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Room implements Serializable {

  @SerializedName("name")
  @Expose
  private String name;
  @SerializedName("public_id")
  @Expose
  private String publicId;
  @SerializedName("room_layout_pid")
  @Expose
  private String roomLayoutPid;

  @SerializedName("description")
  @Expose
  private String description;

  @SerializedName("image_url")
  @Expose
  private String imageUrl;

  @SerializedName("locale")
  @Expose
  private String locale;

  @SerializedName("usual_description")
  @Expose
  private String usualDescription;

  @SerializedName("usual_image_url")
  @Expose
  private String usualImageUrl;

  @SerializedName("vip_description")
  @Expose
  private String vipDescription;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPublicId() {
    return publicId;
  }

  public void setPublicId(String publicId) {
    this.publicId = publicId;
  }

  public String getRoomLayoutPid() {
    return roomLayoutPid;
  }

  public void setRoomLayoutPid(String roomLayoutPid) {
    this.roomLayoutPid = roomLayoutPid;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public String getLocale() {
    return locale;
  }

  public void setLocale(String locale) {
    this.locale = locale;
  }

  public String getUsualDescription() {
    return usualDescription;
  }

  public void setUsualDescription(String usualDescription) {
    this.usualDescription = usualDescription;
  }

  public String getUsualImageUrl() {
    return usualImageUrl;
  }

  public void setUsualImageUrl(String usualImageUrl) {
    this.usualImageUrl = usualImageUrl;
  }

  public String getVipDescription() {
    return vipDescription;
  }

  public void setVipDescription(String vipDescription) {
    this.vipDescription = vipDescription;
  }
}

