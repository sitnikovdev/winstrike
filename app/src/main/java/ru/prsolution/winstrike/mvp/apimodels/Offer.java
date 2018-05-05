
package ru.prsolution.winstrike.mvp.apimodels;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Offer implements Serializable
{

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("cost")
    @Expose
    private Integer cost;
    @SerializedName("public_id")
    @Expose
    private String publicId;
    @SerializedName("create_at")
    @Expose
    private String createAt;
    private final static long serialVersionUID = 4831832608759182598L;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
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

}
