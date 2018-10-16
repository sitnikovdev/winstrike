
package ru.prsolution.winstrike.mvp.apimodels;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Rooms implements Serializable {

  @SerializedName("room")
  @Expose
  private Room room;
  private final static long serialVersionUID = 3350672797887197123L;

  public Room getRoom() {
    return room;
  }

  public void setRoom(Room room) {
    this.room = room;
  }

}
