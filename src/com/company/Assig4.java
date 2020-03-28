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

   private boolean[][] getMockImageGrid() {  // TODO: TEMP DO NOT SUBMIT THIS!!!!!
      boolean[][] mockImageData = new boolean[][]{ // 6 x 20 grid, signal is already pushed to bottom-left
            {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false},
            {true, false, true, false, true, false, true, false, true, false, true, false, true, false, true, false, true, false, true, false},
            {true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false},
            {true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true, false},
            {true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false},
            {true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, false},
      };

      return mockImageData;
   }

   /**
    * Compute the height of the signal.
    * Pre-condition: Signal is placed at bottom-left of image.
    * @return Height of signal.
    */
   public int computeSignalHeight() { // TODO: Update this method once we get BarcodeImage data.
      int signalHeight = 0;
      boolean[][] mockImage = getMockImageGrid();

      for (int i = 0; i < mockImage.length; i++) { // should be i < image.MAX_HEIGHT
         if (mockImage[i][0]) { // if the square is black, it's the top of the spine
            signalHeight = (mockImage.length - i) - 2; // subtract top and bottom spine from signal height
            break;
         }
      }

      return signalHeight;
   }

   /**
    * Compute the width of the signal.
    * Pre-condition: Signal is placed at bottom-left of image.
    * @return Width of signal.
    */
   public int computeSignalWidth() { // TODO: Update this method once we get BarcodeImage data.
      int signalWidth = 0;
      boolean[][] mockImage = getMockImageGrid();

      boolean[] bottomRow = mockImage[mockImage.length - 1];

      for (boolean cell: bottomRow) {
         if (cell) {
            signalWidth++;
         } else {
            break;
         }
      }

      return signalWidth - 2; // subtract right and left spine
   }

   public boolean translateImageToText() {
      return true;
   }


   public void displayTextToConsole() {
   }


   public void displayImageToConsole() {
   }
}
