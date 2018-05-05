package ru.prsolution.winstrike.common.entity;
/*
 * Created by oleg on 01.02.2018.
 */

public class BonusModel {
    String headTitle;
    int thumbnail;
    String time;
    String bonus;

    public BonusModel(String headTitle, int thumbnail, String time, String bonus) {
        this.headTitle = headTitle;
        this.thumbnail = thumbnail;
        this.time = time;
        this.bonus = bonus;
    }

    public String getHeadTitle() {
        return headTitle;
    }

    public void setHeadTitle(String headTitle) {
        this.headTitle = headTitle;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getBonus() {
        return bonus;
    }

    public void setBonus(String bonus) {
        this.bonus = bonus;
    }
}
