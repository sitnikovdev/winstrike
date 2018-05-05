package ru.prsolution.winstrike.common.entity;
/*
 * Created by oleg on 01.02.2018.
 */


public class ChatModel {
    String name;
    int avatar;
    int bubble;
    String time;
    String message;
    int type;

    public ChatModel(String name, int avatar, int bubble, String time, String message, int type) {
        this.name = name;
        this.avatar = avatar;
        this.bubble = bubble;
        this.time = time;
        this.message = message;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAvatar() {
        return avatar;
    }

    public void setAvatar(int avatar) {
        this.avatar = avatar;
    }

    public int getBubble() {
        return bubble;
    }

    public void setBubble(int bubble) {
        this.bubble = bubble;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
