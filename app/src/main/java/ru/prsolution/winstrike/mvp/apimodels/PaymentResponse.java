
package ru.prsolution.winstrike.mvp.apimodels;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentResponse implements Serializable
{

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("redirect_url")
    @Expose
    private String redirectUrl;
    private final static long serialVersionUID = 2905219164848985577L;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

}
