package ru.prsolution.winstrike.common.entity;
/*
 * Created by oleg on 01.02.2018.
 */

public class NewsModel {
     String headTitle;
     int thumbnail;
     String descriptions;
     String date;
     int  views;

     public NewsModel(String headTitle, int thumbnail, String descriptions, String date, int views) {
          this.headTitle = headTitle;
          this.thumbnail = thumbnail;
          this.descriptions = descriptions;
          this.date = date;
          this.views = views;
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

     public String getDescriptions() {
          return descriptions;
     }

     public void setDescriptions(String descriptions) {
          this.descriptions = descriptions;
     }

     public String getDate() {
          return date;
     }

     public void setDate(String date) {
          this.date = date;
     }

     public int getViews() {
          return views;
     }

     public void setViews(int views) {
          this.views = views;
     }
}
