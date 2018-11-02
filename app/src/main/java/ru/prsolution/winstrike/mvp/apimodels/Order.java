
package ru.prsolution.winstrike.mvp.apimodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Order {

  @SerializedName("room_name")
  @Expose
  private String roomName;

  @SerializedName("place")
  @Expose
  private Place place;
  @SerializedName("cost")
  @Expose
  private Integer cost;
  @SerializedName("end_at")
  @Expose
  private String endAt;
  @SerializedName("access_code")
  @Expose
  private String accessCode;
  @SerializedName("place_pid")
  @Expose
  private String placePid;
  @SerializedName("start_at")
  @Expose
  private String startAt;
  @SerializedName("user_pid")
  @Expose
  private String userPid;
  @SerializedName("public_id")
  @Expose
  private String publicId;
  @SerializedName("create_at")
  @Expose
  private String createAt;

  public Place getPlace() {
    return place;
  }

  public void setPlace(Place place) {
    this.place = place;
  }

  public Integer getCost() {
    return cost;
  }

  public void setCost(Integer cost) {
    this.cost = cost;
  }

  public String getEndAt() {
    return endAt;
  }

  public void setEndAt(String endAt) {
    this.endAt = endAt;
  }

  public String getAccessCode() {
    return accessCode;
  }

  public void setAccessCode(String accessCode) {
    this.accessCode = accessCode;
  }

  public String getPlacePid() {
    return placePid;
  }

  public void setPlacePid(String placePid) {
    this.placePid = placePid;
  }

  public String getStartAt() {
    return startAt;
  }

  public void setStartAt(String startAt) {
    this.startAt = startAt;
  }

  public String getUserPid() {
    return userPid;
  }

  public void setUserPid(String userPid) {
    this.userPid = userPid;
  }

  public String getPublicId() {
    return publicId;
  }

  public void setPublicId(String publicId) {
    this.publicId = publicId;
  }

  public String getCreateAt() {
    return createAt;
  }

  public void setCreateAt(String createAt) {
    this.createAt = createAt;
  }

  public String getRoomName() {
    return roomName;
  }

  public void setRoomName(String roomName) {
    this.roomName = roomName;
  }
}
