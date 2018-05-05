package ru.prsolution.winstrike.ui.common;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ru.prsolution.winstrike.common.entity.SeatModel;
import ru.prsolution.winstrike.mvp.apimodels.OrderModel;
import ru.prsolution.winstrike.mvp.apimodels.RoomLayout;
import ru.prsolution.winstrike.mvp.apimodels.User;

/**
 * Created by designer on 21/03/2018.
 */

public class  MapInfoSingleton {

    private static MapInfoSingleton mInstance;
    private ArrayList<String> pidList = null;
    private Date dateFrom = new Date();
    private Date dateTo = new Date();
    private SeatModel seat;
    private RoomLayout roomLayout;

    private String dateFromShort;
    private String dateToShort;

    private String rooms;
    private String selectedDate;
    private List<OrderModel> mPayList;
    private User user;
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public RoomLayout getRoomLayout() {
        return roomLayout;
    }

    public void setRoomLayout(RoomLayout roomLayout) {
        this.roomLayout = roomLayout;
    }

    public  void setUser(User user) {
        this.user = user;
    }

    public  User getUser() {
        return user;
    }
    public SeatModel getSeat() {
        return seat;
    }

    public void setSeat(SeatModel seat) {
        this.seat = seat;
    }

    public String getRooms() {
        return rooms;
    }

    public void setRooms(String rooms) {
        this.rooms = rooms;
    }

    public static MapInfoSingleton getInstance() {
        if(mInstance == null)
            mInstance = new MapInfoSingleton();

        return mInstance;
    }

    private MapInfoSingleton() {
        pidList = new ArrayList<String>();
    }
    // retrieve array from anywhere
    public ArrayList<String> getPidArray() {
        return this.pidList;
    }

    public Date getDateFrom() {
        return this.dateFrom;
    }

    public Date getDateTo() {
        return this.dateTo;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    //Add element to array
    public void addToArray(String value) {
        pidList.add(value);
    }

    public String getDateFromShort() {
        return dateFromShort;
    }

    public void setDateFromShort(String dateFromShort) {
        this.dateFromShort = dateFromShort;
    }

    public String getDateToShort() {
        return dateToShort;
    }

    public void setDateToShort(String dateToShort) {
        this.dateToShort = dateToShort;
    }

    public void setSelectedDate(String selectedDate) {
        this.selectedDate = selectedDate;
    }

    public String getSelectedDate() {
        return selectedDate;
    }

    public void setPayList(List<OrderModel> mPayList) {
       this.mPayList = mPayList;
    }

    public List<OrderModel> getmPayList() {
        return mPayList;
    }
}
