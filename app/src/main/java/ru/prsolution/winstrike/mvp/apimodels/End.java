
package ru.prsolution.winstrike.mvp.apimodels;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class End implements Serializable
{

    @SerializedName("x")
    @Expose
    private Integer x;
    @SerializedName("y")
    @Expose
    private Integer y;
    private final static long serialVersionUID = 327424254676383776L;

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

}
