package ru.prsolution.winstrike.mvp.apimodels;
/*
 * Created by oleg on 31.01.2018.
 */

import android.os.Parcel;
import android.os.Parcelable;

public class OrderModel implements Parcelable {
    String placeName;
    String date;
    String time;
    String startAt;
    String endAt;
    String placePid;
    String publicPid;
    Boolean computerIsActive;
    String computerPid;
    String pcName;
    String accessCode;
    Integer cost;
    int thumbnail;

    protected OrderModel(Parcel in) {
        placeName = in.readString();
        date = in.readString();
        time = in.readString();
        startAt = in.readString();
        endAt = in.readString();
        placePid = in.readString();
        publicPid = in.readString();
        byte tmpComputerIsActive = in.readByte();
        computerIsActive = tmpComputerIsActive == 0 ? null : tmpComputerIsActive == 1;
        computerPid = in.readString();
        pcName = in.readString();
        accessCode = in.readString();
        if (in.readByte() == 0) {
            cost = null;
        } else {
            cost = in.readInt();
        }
        thumbnail = in.readInt();
    }

    public static final Creator<OrderModel> CREATOR = new Creator<OrderModel>() {
        @Override
        public OrderModel createFromParcel(Parcel in) {
            return new OrderModel(in);
        }

        @Override
        public OrderModel[] newArray(int size) {
            return new OrderModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(placeName);
        dest.writeString(date);
        dest.writeString(time);
        dest.writeString(startAt);
        dest.writeString(endAt);
        dest.writeString(placePid);
        dest.writeString(publicPid);
        dest.writeByte((byte) (computerIsActive == null ? 0 : computerIsActive ? 1 : 2));
        dest.writeString(computerPid);
        dest.writeString(pcName);
        dest.writeString(accessCode);
        if (cost == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(cost);
        }
        dest.writeInt(thumbnail);
    }



    public OrderModel(String placeName, Integer cost, String date, String time, String pcName, String accessCode, int thumbnail) {
        this.placeName = placeName;
        this.date = date;
        this.time = time;
        this.cost = cost;
        this.pcName = pcName;
        this.accessCode = accessCode;
        this.thumbnail = thumbnail;
    }

    public OrderModel() {

    }

    public String getStartAt() {
        return startAt;
    }

    public void setStartAt(String startAt) {
        this.startAt = startAt;
    }

    public String getEndAt() {
        return endAt;
    }

    public void setEndAt(String endAt) {
        this.endAt = endAt;
    }

    public String getPlacePid() {
        return placePid;
    }

    public void setPlacePid(String placePid) {
        this.placePid = placePid;
    }

    public String getPublicPid() {
        return publicPid;
    }

    public void setPublicPid(String publicPid) {
        this.publicPid = publicPid;
    }

    public Boolean getComputerIsActive() {
        return computerIsActive;
    }

    public void setComputerIsActive(Boolean computerIsActive) {
        this.computerIsActive = computerIsActive;
    }

    public String getComputerPid() {
        return computerPid;
    }

    public void setComputerPid(String computerPid) {
        this.computerPid = computerPid;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public String getAccessCode() {
        return accessCode;
    }

    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPcName() {
        return pcName;
    }

    public void setPcName(String pcName) {
        this.pcName = pcName;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }
}
