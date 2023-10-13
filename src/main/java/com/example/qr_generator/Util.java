package com.example.qr_generator;

import static com.example.qr_generator.Constants.BARCODE;
import static com.example.qr_generator.Constants.BARCODE_WINDOW_TITLE;
import static com.example.qr_generator.Constants.CREATED;
import static com.example.qr_generator.Constants.DATE_TIME_PATTERN;
import static com.example.qr_generator.Constants.FONT;
import static com.example.qr_generator.Constants.FORMAT;
import static com.example.qr_generator.Constants.IMAGE_VIEW_HEIGHT;
import static com.example.qr_generator.Constants.IMAGE_VIEW_WIDTH;
import static com.example.qr_generator.Constants.IMAGE_VIEW_XML;
import static com.example.qr_generator.Constants.NAME;
import static com.example.qr_generator.Constants.PRICE_PREFIX;
import static com.example.qr_generator.Constants.PROP_ACTION;
import static com.example.qr_generator.Constants.PROP_CREATED;
import static com.example.qr_generator.Constants.PROP_NAME;
import static com.example.qr_generator.Constants.PROP_PRICE;
import static com.example.qr_generator.Constants.PROP_SL;
import static com.example.qr_generator.Constants.REGULAR_EXPRESSION;
import static com.example.qr_generator.Constants.SEPARATOR;
import static com.example.qr_generator.Constants.BUTTON_NAME;
import static com.example.qr_generator.Constants.FILE_NAME;
import static com.example.qr_generator.Constants.FIRST_ID;
import static com.example.qr_generator.Constants.NUMBER;
import static com.example.qr_generator.Constants.SL;

import com.example.qr_generator.modal.TableData;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import net.sourceforge.barbecue.Barcode;
import net.sourceforge.barbecue.BarcodeException;
import net.sourceforge.barbecue.BarcodeFactory;
import net.sourceforge.barbecue.BarcodeImageHandler;
import net.sourceforge.barbecue.output.OutputException;

public class Util {

  private static final List<TableData> tableData = new ArrayList<>();

  public static void addDataAndGenerateBarCode(TextField nameTextField, TextField priceTextField,
      ObservableList<TableData> observableData) throws IOException {

    String fieldValue = nameTextField.getText();
    String price = priceTextField.getText();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);
    String formattedDateTime = LocalDateTime.now().format(formatter);
    if (!validateInput(fieldValue, price)) {
      try (BufferedReader bufferedReader = new BufferedReader(new FileReader(FILE_NAME))) {

        String line;
        ArrayList<String> listOfLines = new ArrayList<>();

        while ((line = bufferedReader.readLine()) != null) {

          listOfLines.add(line);
        }

        if (!listOfLines.isEmpty()) {

          String lastLine = listOfLines.get(listOfLines.size() - 1);
          String id = (Long.parseLong(lastLine.split(SEPARATOR)[0]) + 1) + "";
          String barcode = (Long.parseLong(lastLine.split(SEPARATOR)[2]) + 1) + "";
          var tableData = new TableData();
          tableData.setId(id);
          tableData.setCreatedDate(formattedDateTime);
          tableData.setItemName(fieldValue);
          tableData.setQrNumber(barcode);
          tableData.setActionButton(new Button(BUTTON_NAME));
          tableData.setPrice(price);
          observableData.add(tableData);
          Util.tableData.add(tableData);
          createAndSaveData(id, fieldValue, barcode, formattedDateTime, price);
          openBarcodeView(tableData, Util.class.getResource(IMAGE_VIEW_XML));
        }
      } catch (IOException e) {

        createAndSaveData(FIRST_ID, fieldValue, NUMBER, formattedDateTime, price);
      } catch (BarcodeException | OutputException ex) {

        ex.printStackTrace();
      }
    } else {
      Alert alert = new Alert(AlertType.ERROR);
      alert.setTitle("Invalid Stock");
      alert.setContentText("The entered value is already preset or not acceptable");
      alert.show();
    }
  }

  private static void createAndSaveData(String id, String fieldValue, String barcode,
      String formattedDateTime, String price) throws IOException {
    String inputValue =
        id.concat(SEPARATOR) + fieldValue.concat(SEPARATOR).concat(barcode).concat(SEPARATOR)
            .concat(formattedDateTime).concat(SEPARATOR).concat(price);
    File file = new File(FILE_NAME);
    FileWriter fileWriter = new FileWriter(file, true);
    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
    bufferedWriter.write(inputValue);
    bufferedWriter.newLine();
    bufferedWriter.close();
    fileWriter.close();
  }

  private static boolean validateInput(String fieldValue, String price) {
    Pattern pattern = Pattern.compile(REGULAR_EXPRESSION);
    Matcher matcher = pattern.matcher(price);
    return null == fieldValue || fieldValue.isEmpty() || fieldValue.isBlank()
        || fieldValue.contains(SEPARATOR) || checkValueIsAlreadyPresentedOrNot(fieldValue)
        || !matcher.matches();
  }

  private static boolean checkValueIsAlreadyPresentedOrNot(String fieldValue) {
    boolean isPresent = false;
    try (BufferedReader bufferedReader = new BufferedReader(new FileReader(FILE_NAME))) {
      String line;
      ArrayList<String> listOfLines = new ArrayList<>();
      while ((line = bufferedReader.readLine()) != null) {

        listOfLines.add(line.split(SEPARATOR)[1]);
      }
      isPresent = listOfLines.contains(fieldValue);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return isPresent;
  }

  public static ObservableList<TableData> initializeTableData(TableView tableView) {

    ObservableList<TableData> observableData = null;
    try (BufferedReader bufferedReader = new BufferedReader(new FileReader(FILE_NAME))) {

      TableColumn<TableData, String> sl = new TableColumn<>(SL);
      TableColumn<TableData, String> name = new TableColumn<>(NAME);
      TableColumn<TableData, String> time = new TableColumn<>(CREATED);
      TableColumn<TableData, String> price = new TableColumn<>(Constants.PRICE);
      TableColumn<TableData, Button> action = new TableColumn<>(BUTTON_NAME);
      tableView.getColumns().addAll(sl, name, time, price, action);
      String line;
      while ((line = bufferedReader.readLine()) != null) {

        var tableData = new TableData();
        tableData.setId(line.split(SEPARATOR)[0]);
        tableData.setItemName(line.split(SEPARATOR)[1]);
        tableData.setQrNumber(line.split(SEPARATOR)[2]);
        tableData.setCreatedDate(line.split(SEPARATOR)[3]);
        tableData.setPrice(line.split(SEPARATOR)[4]);
        tableData.setActionButton(new Button(BUTTON_NAME));
        Util.tableData.add(tableData);
      }
      observableData = FXCollections.observableArrayList(tableData);
      sl.setCellValueFactory(new PropertyValueFactory<>(PROP_SL));
      name.setCellValueFactory(new PropertyValueFactory<>(PROP_NAME));
      time.setCellValueFactory(new PropertyValueFactory<>(PROP_CREATED));
      action.setCellValueFactory(new PropertyValueFactory<>(PROP_ACTION));
      price.setCellValueFactory(new PropertyValueFactory<>(PROP_PRICE));
      action.setCellFactory(column -> new TableCell<TableData, Button>() {
        @Override
        protected void updateItem(Button item, boolean empty) {

          super.updateItem(item, empty);
          if (item == null || empty) {
            setGraphic(null);
          } else {
            setGraphic(item);
            item.setOnAction(actionButtonEventHandler());
          }
        }

        private EventHandler<ActionEvent> actionButtonEventHandler() {

          return event -> {
            try {
              openBarcodeView(getTableView().getItems().get(getIndex()),
                  getClass().getResource(IMAGE_VIEW_XML));
            } catch (Exception ex) {
              ex.printStackTrace();
            }
          };
        }
      });

      tableView.setItems(observableData);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return observableData;
  }

  private static void openBarcodeView(TableData rowData, URL imageView)
      throws BarcodeException, OutputException, IOException {
    var bufferedImage = generateBarCode(rowData.getQrNumber());
    generateQrCodeWithPrice(rowData, bufferedImage);
    FXMLLoader fxmlLoader = new FXMLLoader();
    fxmlLoader.setLocation(imageView);
    Scene scene = new Scene(fxmlLoader.load(), IMAGE_VIEW_WIDTH, IMAGE_VIEW_HEIGHT);
    Stage stage = new Stage();
    stage.setTitle(BARCODE_WINDOW_TITLE);
    stage.setScene(scene);
    stage.show();
  }

  public static BufferedImage generateBarCode(String barcodeText)
      throws BarcodeException, OutputException {
    Barcode barcode = BarcodeFactory.createCode128(barcodeText);
    return BarcodeImageHandler.getImage(barcode);
  }

  public static void searchItems(String searchString, ObservableList<TableData> observableData) {
    if (null != observableData && !observableData.isEmpty()) {
      if (null == searchString || searchString.isBlank() || searchString.isEmpty()) {
        observableData.clear();
        observableData.addAll(tableData);
      } else {
        var observableList = observableData.stream()
            .filter(tableData -> tableData.getItemName().contains(searchString)).toList();
        if (!observableList.isEmpty()) {
          observableData.clear();
          observableData.addAll(observableList);
        } else {
          observableData.clear();
        }
      }
    }
  }

  public static void generateQrCodeWithPrice(TableData tableData, BufferedImage originalImage) {
    try {

      int newWidth = originalImage.getWidth();
      int newHeight = originalImage.getHeight() + 50;
      BufferedImage newImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
      Graphics2D graphics = newImage.createGraphics();
      graphics.setColor(Color.WHITE);
      graphics.fillRect(0, 0, newWidth, newHeight);
      graphics.drawImage(originalImage, 0, 0, null);
      Font font = new Font(FONT, Font.BOLD, 20);
      Color textColor = Color.BLACK;
      graphics.setFont(font);
      graphics.setColor(textColor);
      int textX = originalImage.getHeight() - 10;
      int textY = originalImage.getHeight() + 30;
      String overlayText = PRICE_PREFIX.concat(tableData.getPrice());
      graphics.drawString(overlayText, textX, textY);
      File outputFile = new File(BARCODE);
      ImageIO.write(newImage, FORMAT, outputFile);
      graphics.dispose();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
