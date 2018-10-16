
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
}

