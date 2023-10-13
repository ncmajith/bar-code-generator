module com.example.qr_generator {
  requires javafx.controls;
  requires javafx.fxml;
  requires org.controlsfx.controls;
  requires org.kordamp.bootstrapfx.core;
  requires barbecue;
  requires java.desktop;

  opens com.example.qr_generator to javafx.fxml;

  exports com.example.qr_generator;
  exports com.example.qr_generator.modal;
}