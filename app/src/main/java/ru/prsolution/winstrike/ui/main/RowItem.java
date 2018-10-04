package ru.prsolution.winstrike.ui.main;

public class RowItem {
  private Integer address;
  private String title;

  public RowItem(String Title, Integer address){
    this.title = Title;
    this.address = address;
  }

  public String getTitle(){
    return title;
  }

  public void setTitle(String Title){

    this.title = Title;
  }

  public Integer getAddress() {
    return address;
  }

  public void setAddress(Integer address) {
    this.address = address;
  }

  @Override
  public String toString() {
    return title;
  }

}
