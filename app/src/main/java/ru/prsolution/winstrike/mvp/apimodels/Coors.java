
package ru.prsolution.winstrike.mvp.apimodels;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Coors implements Serializable
{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("x")
    @Expose
    private Integer x;
    @SerializedName("y")
    @Expose
    private Integer y;
    @SerializedName("angle")
    @Expose
    private Double angle;
    @SerializedName("type")
    @Expose
    private Integer type;
    private final static long serialVersionUID = -6921130380543258340L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public Double getAngle() {
        return angle;
    }

    public void setAngle(Double angle) {
        this.angle = angle;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

}
