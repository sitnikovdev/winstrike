
package ru.prsolution.winstrike.mvp.apimodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("confirmed")
    @Expose
    private Boolean confirmed;
    @SerializedName("confirmed_on")
    @Expose
    private Object confirmedOn;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("public_id")
    @Expose
    private String publicId;
    @SerializedName("registered_on")
    @Expose
    private String registeredOn;
    @SerializedName("social_id")
    @Expose
    private Object socialId;

    @SerializedName("role")
    @Expose
    private String role;

    @SerializedName("user_id")
    @Expose
    private String id;


    private String password;

    public User() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Boolean getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(Boolean confirmed) {
        this.confirmed = confirmed;
    }

    public Object getConfirmedOn() {
        return confirmedOn;
    }

    public void setConfirmedOn(Object confirmedOn) {
        this.confirmedOn = confirmedOn;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Object getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }

    public String getRegisteredOn() {
        return registeredOn;
    }

    public void setRegisteredOn(String registeredOn) {
        this.registeredOn = registeredOn;
    }

    public Object getSocialId() {
        return socialId;
    }

    public void setSocialId(Object socialId) {
        this.socialId = socialId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
