package ru.prsolution.winstrike.mvp.apimodels;

/**
 * Created by designer on 07/03/2018.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import timber.log.Timber;

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

    public void setPlacesPid(LinkedHashMap<Integer,String> placesPid) {
        // TODO: 13/05/2018   convert to List<String>
        List<String> pids = new ArrayList();
        Timber.d("pids than need to converted: %s",placesPid);
        Set<Map.Entry<Integer, String>> entrySet = placesPid.entrySet();
        Iterator<Map.Entry<Integer,String>> iterator = entrySet.iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer,String> entry = iterator.next();
            String value = entry.getValue();
            pids.add(value);
        }
        this.placesPid = pids;
    }

    @Override
    public String toString() {
        return "start_at: " + startAt + "end_at: " + end_at + " Places: " + placesPid.toString();
    }
}

