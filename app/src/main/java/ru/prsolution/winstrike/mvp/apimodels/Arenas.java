
package ru.prsolution.winstrike.mvp.apimodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Arenas implements Serializable {

  @SerializedName("rooms")
  @Expose
  private List<Room> rooms = null;

  public List<Room> getRooms() {
    return rooms;
  }

  public void setRooms(List<Room> rooms) {
    this.rooms = rooms;
  }

}
