package com.example.qr_generator;

import static com.example.qr_generator.Constants.MAIN_PAGE_TITLE;
import static com.example.qr_generator.Constants.MAIN_VIEW_HEIGHT;
import static com.example.qr_generator.Constants.MAIN_VIEW_WIDTH;
import static com.example.qr_generator.Constants.MAIN_VIEW_XML;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {

  @Override
  public void start(Stage stage) throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(MAIN_VIEW_XML));
    Scene scene = new Scene(fxmlLoader.load(), MAIN_VIEW_WIDTH, MAIN_VIEW_HEIGHT);
    stage.setTitle(MAIN_PAGE_TITLE);
    stage.setScene(scene);
    stage.show();
  }

  public static void main(String[] args) {
    launch();
  }
}