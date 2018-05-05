package ru.prsolution.winstrike.common.entity;
/*
 * Created by oleg on 31.01.2018.
 */

public class ClubModel {
    String name;
    int commonPrice;
    int vipPrice;
    int  eventPrice;
    int thumbnail;

    public ClubModel(String name, int commonPrice, int vipPrice, int eventPrice, int thumbnail) {
        this.name = name;
        this.commonPrice = commonPrice;
        this.vipPrice = vipPrice;
        this.eventPrice = eventPrice;
        this.thumbnail = thumbnail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCommonPrice() {
        return commonPrice;
    }

    public void setCommonPrice(int commonPrice) {
        this.commonPrice = commonPrice;
    }

    public int getVipPrice() {
        return vipPrice;
    }

    public void setVipPrice(int vipPrice) {
        this.vipPrice = vipPrice;
    }

    public int getEventPrice() {
        return eventPrice;
    }

    public void setEventPrice(int eventPrice) {
        this.eventPrice = eventPrice;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }
}
