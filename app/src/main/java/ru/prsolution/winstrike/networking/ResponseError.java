package ru.prsolution.winstrike.networking;

import com.google.gson.annotations.SerializedName;

public class ResponseError {
    @SerializedName("status")
    public String status;

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    @SuppressWarnings({"unused", "used by Retrofit"})
    public ResponseError() {
    }

    public ResponseError(String status) {
        this.status = status;
    }
}
