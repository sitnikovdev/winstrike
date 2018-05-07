package ru.prsolution.winstrike.mvp.apimodels;

public class SeatApi {
    String public_id;
    Integer seatXLeft;
    Integer seatYTop;
    Double seatAngle;
    Integer seatType;
    String seatStatus;
    private boolean selected;

    public SeatApi(String public_id, Integer seatXLeft, Integer seatYTop, Double seatAngle, Integer seatType, String seatStatus) {
        this.public_id = public_id;
        this.seatXLeft = seatXLeft;
        this.seatYTop = seatYTop;
        this.seatAngle = seatAngle;
        this.seatType = seatType;
        this.seatStatus = seatStatus;
    }

    public String getSeatStatus() {
        return seatStatus;
    }


    public void setSeatStatus(String seatStatus) {
        this.seatStatus = seatStatus;
    }

    public String getPublic_id() {
        return public_id;
    }

    public void setPublic_id(String public_id) {
        this.public_id = public_id;
    }

    public Integer getSeatXLeft() {
        return seatXLeft;
    }

    public void setSeatXLeft(Integer seatXLeft) {
        this.seatXLeft = seatXLeft;
    }

    public Integer getSeatYTop() {
        return seatYTop;
    }

    public void setSeatYTop(Integer seatYTop) {
        this.seatYTop = seatYTop;
    }

    public Double getSeatAngle() {
        return seatAngle;
    }

    public void setSeatAngle(Double seatAngle) {
        this.seatAngle = seatAngle;
    }

    public Integer getSeatType() {
        return seatType;
    }

    public void setSeatType(Integer seatType) {
        this.seatType = seatType;
    }

    public boolean isSelected() {
        return this.selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
