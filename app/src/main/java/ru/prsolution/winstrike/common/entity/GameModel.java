package ru.prsolution.winstrike.common.entity;
/*
 * Created by oleg on 01.02.2018.
 */

public class GameModel {
    String name;
    Boolean isFavared;
    int icon;
    int type = 1;

    int userCounts;

    public Boolean isFavorite() {
        return isFavared;
    }

    public void setFavared(Boolean favared) {
        isFavared = favared;
    }

    public GameModel(String name, Boolean isFavared) {
        this.name = name;
        this.icon = icon;
        this.isFavared = isFavared;
        this.userCounts = userCounts;
    }


    @Override
    public String toString() {
         return  this.name;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String headTitle) {
        this.name = headTitle;
    }

    public int getUserCounts() {
        return userCounts;
    }

    public void setUserCounts(int userCounts) {
        this.userCounts = userCounts;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
