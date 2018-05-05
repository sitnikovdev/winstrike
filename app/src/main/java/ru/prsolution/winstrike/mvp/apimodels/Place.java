
package ru.prsolution.winstrike.mvp.apimodels;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Place implements Serializable
{

    @SerializedName("offer_pid")
    @Expose
    private String offerPid;
    @SerializedName("is_hidden")
    @Expose
    private Boolean isHidden;
    @SerializedName("computer")
    @Expose
    private Computer computer;
    @SerializedName("public_id")
    @Expose
    private String publicId;
    @SerializedName("offer")
    @Expose
    private Offer offer;
    @SerializedName("computer_pid")
    @Expose
    private String computerPid;
    @SerializedName("room_layout_pid")
    @Expose
    private String roomLayoutPid;
    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("create_at")
    @Expose
    private String createAt;

    @SerializedName("coors")
    @Expose
    private Coors coors;

    @SerializedName("status")
    @Expose
    private String status;


    private final static long serialVersionUID = 2061867234598659099L;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Coors getCoors() {
        return coors;
    }

    public void setCoors(Coors coors) {
        this.coors = coors;
    }

    public String getOfferPid() {
        return offerPid;
    }

    public void setOfferPid(String offerPid) {
        this.offerPid = offerPid;
    }

    public Boolean getIsHidden() {
        return isHidden;
    }

    public void setIsHidden(Boolean isHidden) {
        this.isHidden = isHidden;
    }

    public Computer getComputer() {
        return computer;
    }

    public void setComputer(Computer computer) {
        this.computer = computer;
    }

    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }

    public Offer getOffer() {
        return offer;
    }

    public void setOffer(Offer offer) {
        this.offer = offer;
    }

    public String getComputerPid() {
        return computerPid;
    }

    public void setComputerPid(String computerPid) {
        this.computerPid = computerPid;
    }

    public String getRoomLayoutPid() {
        return roomLayoutPid;
    }

    public void setRoomLayoutPid(String roomLayoutPid) {
        this.roomLayoutPid = roomLayoutPid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

}
