package com.company;

/**
 * Project Members: Ericka Koyama, Holly Stephens, Ngoc Tran Do
 * CST 338 Software Design
 * Assignment 4 - Barcode Scanner
 */


public class Assig4 {

   public static void main(String[] args) {
      // write your code here
   }
}

/**
 *
 */
interface BarcodeIO {
   public boolean scan(BarcodeImage bc);

   public boolean readText(String text);

   public boolean generateImageFromText();

   public boolean translateImageToText();

   public void displayTextToConsole();

   public void displayImageToConsole();
}

/**
 *
 */
class BarcodeImage implements Cloneable {
   public BarcodeImage clone() {
      return new BarcodeImage(); // TEMP
   }
}

/**
 *
 */
class DataMatrix implements BarcodeIO {
   public boolean scan(BarcodeImage bc) {
   }


   public boolean readText(String text) {
   }


   public boolean generateImageFromText() {
   }


   public boolean translateImageToText() {
   }


   public void displayTextToConsole() {
   }


   public void displayImageToConsole() {
   }
}
