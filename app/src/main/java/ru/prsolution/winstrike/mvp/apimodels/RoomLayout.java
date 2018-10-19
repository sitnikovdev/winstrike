
package ru.prsolution.winstrike.mvp.apimodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class RoomLayout implements Serializable {

  @SerializedName("places")
  @Expose
  private List<Place> places = null;
  @SerializedName("room_pid")
  @Expose
  private String roomPid;
  @SerializedName("public_id")
  @Expose
  private String publicId;
  @SerializedName("name")
  @Expose
  private String name;
  @SerializedName("create_at")
  @Expose
  private String createAt;
  @SerializedName("walls")
  @Expose
  private List<Wall> walls = null;
  @SerializedName("labels")
  @Expose
  private List<Label> labels = null;

  private final static long serialVersionUID = -7881296003903503498L;

  public List<Place> getPlaces() {
    return places;
  }

  public void setPlaces(List<Place> places) {
    this.places = places;
  }

  public String getRoomPid() {
    return roomPid;
  }

  public void setRoomPid(String roomPid) {
    this.roomPid = roomPid;
  }

  public String getPublicId() {
    return publicId;
  }

  public void setPublicId(String publicId) {
    this.publicId = publicId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCreateAt() {
    return createAt;
  }

  public void setCreateAt(String createAt) {
    this.createAt = createAt;
  }

  public List<Wall> getWalls() {
    return walls;
  }

  public void setWalls(List<Wall> walls) {
    this.walls = walls;
  }

  public List<Label> getLabels() {
    return labels;
  }

  public void setLabels(List<Label> labels) {
    this.labels = labels;
  }

}
