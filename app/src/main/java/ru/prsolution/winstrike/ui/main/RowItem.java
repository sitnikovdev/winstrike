package ru.prsolution.winstrike.ui.main;

public class RowItem {
  private String  title;
  private String address;

  public RowItem(String Title, String address){
    this.title = Title;
    this.address = address;
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

}
