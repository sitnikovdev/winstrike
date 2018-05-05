
package ru.prsolution.winstrike.mvp.apimodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Wall implements Serializable
{

    @SerializedName("start")
    @Expose
    private Start start;
    @SerializedName("end")
    @Expose
    private End end;
    private final static long serialVersionUID = 8296872575103507327L;

    public Start getStart() {
        return start;
    }

    public void setStart(Start start) {
        this.start = start;
    }

    public End getEnd() {
        return end;
    }

    public void setEnd(End end) {
        this.end = end;
    }

}
