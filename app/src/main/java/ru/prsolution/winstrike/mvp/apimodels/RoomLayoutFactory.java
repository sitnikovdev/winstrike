
package ru.prsolution.winstrike.mvp.apimodels;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RoomLayoutFactory implements Serializable
{

    @SerializedName("room_layout")
    @Expose
    private RoomLayout roomLayout;
    private final static long serialVersionUID = -6262896985114877791L;

    public RoomLayout getRoomLayout() {
        return roomLayout;
    }

    public void setRoomLayout(RoomLayout roomLayout) {
        this.roomLayout = roomLayout;
    }

}
