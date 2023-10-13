package com.example.qr_generator.modal;

import javafx.scene.control.Button;

public class TableData {

  private String id;
  private String itemName;
  private String qrNumber;
  private String createdDate;
  private String price;
  private Button actionButton;

  public String getPrice() {
    return price;
  }

  public void setPrice(String price) {
    this.price = price;
  }

  public TableData() {
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Button getActionButton() {
    return actionButton;
  }

  public void setActionButton(Button actionButton) {
    this.actionButton = actionButton;
  }

  public String getItemName() {
    return itemName;
  }

  public void setItemName(String itemName) {
    this.itemName = itemName;
  }

  public String getQrNumber() {
    return qrNumber;
  }

  public void setQrNumber(String qrNumber) {
    this.qrNumber = qrNumber;
  }

  public String getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(String createdDate) {
    this.createdDate = createdDate;
  }
}
