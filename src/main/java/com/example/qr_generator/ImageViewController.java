package com.example.qr_generator;


import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import java.lang.System.Logger;
import javafx.fxml.FXML;
import javafx.print.Paper;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Printer;
import javafx.print.Printer.MarginType;
import javax.imageio.ImageIO;


public class ImageViewController {

  @FXML
  ImageView imageView;

  public void initialize() {
    Image image = new Image("file:barcode.png");
    imageView.setImage(image);
  }

  @FXML
  protected void onPrintButtonClick() throws IOException {
    printImage();
  }

  private void printImage() throws IOException {
    File file = new File("barcode.png");
    BufferedImage image = ImageIO.read(file);
    PrinterJob printJob = PrinterJob.getPrinterJob();
    printJob.setPrintable(new Printable() {
      @Override
      public int print(Graphics graphics, PageFormat pageFormat, int pageIndex)
          throws PrinterException {
        int x = (int) Math.ceil(pageFormat.getImageableX());
        int y = (int) Math.ceil(pageFormat.getImageableY());
        if (pageIndex != 0) {
          return NO_SUCH_PAGE;
        }
        graphics.drawImage(image, x, y, image.getWidth(), image.getHeight(), null);
        return PAGE_EXISTS;
      }
    });
    try {
      printJob.print();
    } catch (PrinterException e1) {
      System.out.println("Printer ex");
    }
    file.delete();
  }

}
