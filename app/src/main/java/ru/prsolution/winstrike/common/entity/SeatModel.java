package ru.prsolution.winstrike.common.entity;
/*
 * Created by oleg on 01.02.2018.
 */

import android.os.Parcel;
import android.os.Parcelable;

public class SeatModel implements Parcelable {
    String  type;
    int thumbnail;

    public SeatModel(String type, int thumbnail) {
        this.type = type;
        this.thumbnail = thumbnail;
    }

    protected SeatModel(Parcel in) {
        type = in.readString();
        thumbnail = in.readInt();
    }

    public static final Creator<SeatModel> CREATOR = new Creator<SeatModel>() {
        @Override
        public SeatModel createFromParcel(Parcel in) {
            return new SeatModel(in);
        }

        @Override
        public SeatModel[] newArray(int size) {
            return new SeatModel[size];
        }
    };

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(type);
        parcel.writeInt(thumbnail);
    }
}
