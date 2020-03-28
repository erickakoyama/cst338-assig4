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
   private BarcodeImage image;

   public boolean scan(BarcodeImage bc) {
      return true;
   }


   public boolean readText(String text) {
      return true;
   }


   public boolean generateImageFromText() {
      return true;
   }

   private char readCharFromCol(int col) {
      return 'T';
   }

   private boolean WriteCharToCol(int col, int code) {
      return true;
   }


   public boolean translateImageToText() {
      return true;
   }


   public void displayTextToConsole() {
   }


   public void displayImageToConsole() {
   }
}
