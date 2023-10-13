package com.example.qr_generator;

import com.example.qr_generator.modal.TableData;
import java.io.IOException;
import java.util.Objects;
import java.util.stream.Collectors;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class HelloController {

  @FXML
  TableView<TableData> tableView;
  @FXML
  private TextField nameTextField;
  @FXML
  private TextField searchTextFiled;
  ObservableList<TableData> observableData;
  @FXML
  private TextField priceTextField;

  @FXML
  protected void onHelloButtonClick() throws IOException {
    Util.addDataAndGenerateBarCode(nameTextField, priceTextField, observableData);
    if (Objects.isNull(observableData)) {
      observableData = Util.initializeTableData(tableView);
    }
  }

  @FXML
  protected void onSearchButtonRelease() {
    var searchString = searchTextFiled.getText();
    Util.searchItems(searchString, observableData);
  }

  public void initialize() {
    observableData = Util.initializeTableData(tableView);
  }
}