
package ru.prsolution.winstrike.mvp.apimodels;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Computer implements Serializable
{

    @SerializedName("active")
    @Expose
    private Boolean active;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("public_id")
    @Expose
    private String publicId;
    @SerializedName("create_at")
    @Expose
    private String createAt;
    private final static long serialVersionUID = -1318326118487899001L;

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

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

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

}
