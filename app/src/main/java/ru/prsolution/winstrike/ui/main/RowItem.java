package ru.prsolution.winstrike.ui.main;

public class RowItem {
  private String Title;

  public RowItem(String Title){
    this.Title = Title;
  }

  public String getTitle(){

    return Title;
  }

  public void setTitle(String Title){

    this.Title = Title;
  }


  @Override
  public String toString() {
    return Title ;
  }

}
