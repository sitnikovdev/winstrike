
package ru.prsolution.winstrike.mvp.apimodels;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Room implements Serializable
{

    @SerializedName("layouts")
    @Expose
    private List<Layout> layouts = null;
    @SerializedName("locale")
    @Expose
    private String locale;
    @SerializedName("active_layout_pid")
    @Expose
    private String activeLayoutPid;
    @SerializedName("public_id")
    @Expose
    private String publicId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("create_at")
    @Expose
    private final static long serialVersionUID = -2030458791997759852L;

    public String getActiveLayoutPid() {
        return activeLayoutPid;
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
}
