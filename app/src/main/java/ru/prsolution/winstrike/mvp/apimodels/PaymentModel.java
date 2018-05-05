package ru.prsolution.winstrike.mvp.apimodels;

/**
 * Created by designer on 07/03/2018.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PaymentModel {

    @SerializedName("start_at")
    @Expose
    private String startAt;
    @SerializedName("end_at")
    @Expose
    private String end_at;
    @SerializedName("places")
    @Expose
    private List<String> placesPid;


    public String getStartAt() {
        return startAt;
    }

    public void setStartAt(String startAt) {
        this.startAt = startAt;
    }

    public String getEnd_at() {
        return end_at;
    }

    public void setEnd_at(String end_at) {
        this.end_at = end_at;
    }

    public List<String> getPlacesPid() {
        return placesPid;
    }

    public void setPlacesPid(List<String> placesPid) {
        this.placesPid = placesPid;
    }

    @Override
    public String toString() {
        return "start_at: " + startAt + "end_at: " + end_at + " Places: " + placesPid.toString();
    }
}

