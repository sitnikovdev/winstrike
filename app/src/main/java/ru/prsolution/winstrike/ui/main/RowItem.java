package ru.prsolution.winstrike.ui.main;

public class RowItem {
  private String  title;
  private String address;
  private Boolean mSelected;

  public RowItem(String Title, String address, Boolean selected){
    this.title = Title;
    this.address = address;
    this.mSelected = selected;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  @Override
  public String toString() {
    return getTitle();
  }

  public Boolean getmSelected() {
    return mSelected;
  }

  public void setSelected(Boolean mSelected) {
    this.mSelected = mSelected;
  }
}
