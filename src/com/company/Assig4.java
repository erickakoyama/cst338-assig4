package com.company;

/**
 * Project Members: Ericka Koyama, Holly Stephens, Ngoc Tran Do
 * CST 338 Software Design Assignment 4 - Barcode Scanner
 */


public class Assig4 {

   public static void main(String[] args) {
      String[] sImageIn =
      {
         "                                               ",
         "                                               ",
         "                                               ",
         "     * * * * * * * * * * * * * * * * * * * * * ",
         "     *                                       * ",
         "     ****** **** ****** ******* ** *** *****   ",
         "     *     *    ****************************** ",
         "     * **    * *        **  *    * * *   *     ",
         "     *   *    *  *****    *   * *   *  **  *** ",
         "     *  **     * *** **   **  *    **  ***  *  ",
         "     ***  * **   **  *   ****    *  *  ** * ** ",
         "     *****  ***  *  * *   ** ** **  *   * *    ",
         "     ***************************************** ",
         "                                               ",
         "                                               ",
         "                                               "

      };


      String[] sImageIn_2 =
      {
         "                                          ",
         "                                          ",
         "* * * * * * * * * * * * * * * * * * *     ",
         "*                                    *    ",
         "**** *** **   ***** ****   *********      ",
         "* ************ ************ **********    ",
         "** *      *    *  * * *         * *       ",
         "***   *  *           * **    *      **    ",
         "* ** * *  *   * * * **  *   ***   ***     ",
         "* *           **    *****  *   **   **    ",
         "****  *  * *  * **  ** *   ** *  * *      ",
         "**************************************    ",
         "                                          ",
         "                                          ",
         "                                          ",
         "                                          "

      };

      BarcodeImage bc = new BarcodeImage(sImageIn);
      DataMatrix dm = new DataMatrix(bc);

      // First secret message
      dm.translateImageToText();
      dm.displayTextToConsole();
      dm.displayImageToConsole();

      // second secret message
      bc = new BarcodeImage(sImageIn_2);
      dm.scan(bc);
      dm.translateImageToText();
      dm.displayTextToConsole();
      dm.displayImageToConsole();

      // create your own message
      dm.readText("What a great resume builder this is!");
      dm.generateImageFromText();
      dm.displayTextToConsole();
      dm.displayImageToConsole();
   }
}

/**
 * BarcodeIO interface that will be implemented by other classes. Used to store some version of an image and some
 * version of the text associated with that image.
 */
interface BarcodeIO {
   /**
    * Accepts and stores a copy of an image represented by BarcodeImage.
    */
   public boolean scan(BarcodeImage bc);

   /**
    * Accepts a text string that will be encoded in an image later.
    */
   public boolean readText(String text);

   /**
    * Creates an image (BarcodeImage) from the text stored in the implementing class.
    */
   public boolean generateImageFromText();

   /**
    * Creates a text string from an image (BarcodeImage).
    */
   public boolean translateImageToText();

   /**
    * Print out the text string to the console.
    */
   public void displayTextToConsole();

   /**
    * Print out the image to the console.
    */
   public void displayImageToConsole();
}

/**
 * Store and retrieve the 2D data.
 */
class BarcodeImage implements Cloneable {
   public static final int MAX_HEIGHT = 30;
   public static final int MAX_WIDTH = 65;
   private boolean[][] imageData; // Stores the inbound image. White = false, black = true.

   /**
    * Default constructor. Instantiates a 2D array.
    */
   BarcodeImage() {
      imageData = new boolean[MAX_HEIGHT][MAX_WIDTH];
      for (int row = 0; row < MAX_HEIGHT; row++) {
         for (int col = 0; col < MAX_WIDTH; col++) {
            imageData[row][col] = false;
         }
      }
   }

   /**
    * Overloaded constructor. Takes a 1D array of Strings and converts it to a 2D array.
    *
    * @param strData The 1D array of Strings to convert.
    */
   BarcodeImage(String[] strData) {
      imageData = new boolean[MAX_HEIGHT][MAX_WIDTH];
      if (checkSize(strData)) {
         /**
          * Read string array in bottom up to pad to lower left corner.
          * Since [0][0] is the top left corner, this means [MAX_HEIGHT][0] is our starting position.
          * MAX_HEIGHT - strData.length will be the last row of image values
          */
         int row = MAX_HEIGHT;
         /**
          * Grab strings in reverse order from strData
          * col will correspond to both the current position of the current string and the current position of our
          * inner 2D array.
          */
         for (int i = strData.length; i > 0; i--) {
            String line = strData[i - 1];
            int col = 0;
            /*
             * We want to fill our 2D array with false values after we exceed the length of our string.
             * This is why it's necessary to loop through MAX_WIDTH each time.
             */
            while (col < MAX_WIDTH) {
               boolean value = false; // Default value.
               if (col < line.length()) { // Check that we've not exceeded string length before trying to access data.
                  value = (line.charAt(col) == '*') ? true : false; // Assign boolean representation of string data.
               }
               imageData[row - 1][col] = value;
               col++;
            }
            row--; // Decrement row as we loop to grab the next string.
         }
         // Fill top remaining space in imageData with false.
         while (row > 0) {
            for (int j = 0; j < MAX_WIDTH; j++) {
               imageData[row - 1][j] = false;
            }
            row--;
         }
      }
   }

   /**
    * Accessor for each bit in the image.
    *
    * @param row The position of the outer array.
    * @param col The position of the inner array.
    * @return The data in the specified location or false if there is an error accessing the data.
    */
   public boolean getPixel(int row, int col) {
      if (row < MAX_HEIGHT && col < MAX_WIDTH) {
         return imageData[row][col];
      }
      return false;
   }

   /**
    * Accessor for each bit in the image.
    *
    * @param row   The position of the outer array.
    * @param col   The position of the inner array.
    * @param value The value to be placed in the specified location.
    * @return True on success, false on failure.
    */
   public boolean setPixel(int row, int col, boolean value) {
      if (row < MAX_HEIGHT && col < MAX_WIDTH) {
         imageData[row][col] = value;
         return true;
      }
      return false;
   }

   /**
    * Utility method. Checks the incoming data for every conceivable size or null error.
    *
    * @param data The image data.
    * @return True if data is okay, false otherwise.
    */
   private boolean checkSize(String[] data) {
      if (null == data || data.length > MAX_HEIGHT) return false;
      for (int i = 0; i < data.length; i++) {
         if (null == data[i] || data[i].length() > MAX_WIDTH) return false;
      }
      return true;
   }

   /**
    * Utility method. Outputs the contents of imageData. Converts back to the image format
    */
   public void displayToConsole() {
      for (int i = 0; i < MAX_HEIGHT; i++) {
         String line = "";
         for (int j = 0; j < MAX_WIDTH; j++) {
            String value = (imageData[i][j]) ? "*" : " ";
            line = line + value;
         }
         System.out.println(line);
      }
   }

   /**
    * An override of the clone method in Cloneable interface.
    *
    * @return a copy of the BarcodeImage object.
    */
   public Object clone() throws CloneNotSupportedException {
      try {
         return (BarcodeImage) super.clone();
      } catch (CloneNotSupportedException e) {
         return null;
      }
   }
}

/**
 * DataMatrix class is designed to encode and decode 2D barcodes.
 */
class DataMatrix implements BarcodeIO {
   public static final char BLACK_CHAR = '*';
   public static final char WHITE_CHAR = ' ';
   private BarcodeImage image;
   private String text;
   private int actualWidth; // width of real signal
   private int actualHeight; // height of real signal

   public DataMatrix() {
      image = new BarcodeImage();
      text = "";
      actualWidth = 0;
      actualHeight = 0;
   }

   public DataMatrix(BarcodeImage bc) {
      if (bc == null) {
         image = new BarcodeImage();
      } else {
         scan(bc);
      }

      text = "";
   }

   public DataMatrix(String text) {
      image = new BarcodeImage();

      if (!readText(text))
         text = "";

      actualWidth = 0;
      actualHeight = 0;
   }

   private void cleanImage() {
      int amountColtoMove = 0, numRowToDelete = 0;
      int increment = 0, offset = 0;
      int maxRow = image.MAX_HEIGHT;
      //get value for shifting left

      for (int row = 0; row < image.MAX_HEIGHT; row++) {  //to get first true value locate bottom left of image
         for (int column = 0; column < image.MAX_WIDTH; column++)  //since all image has solid bottom left
         {  //to know how much to move left
            if (image.getPixel(row, column) == true) {
               amountColtoMove = column;            //pass in the value of the column
               increment++;                     //get the actual height of the image
               break;
            } else ;
         }
      }

      //	Fill in column by column
      if (amountColtoMove > 0) {
         for (int col = 0; col < image.MAX_WIDTH - amountColtoMove; col++) {
            for (int row = 0; row < image.MAX_HEIGHT; row++) {
               image.setPixel(row, col, image.getPixel(row, (col + amountColtoMove)));
            }
         }
      }
      //	Start on shift down
      for (int a = image.MAX_HEIGHT - 1; a >= 0; a--) {   //to get first true value locate bottom right of image
         for (int b = 0; b < image.MAX_WIDTH; b++) {      //to know how much to move down
            if (image.getPixel(a, b) == true) {
               offset = a;
               break;
            } else ;
         }
      }
      numRowToDelete = maxRow - increment - offset;
      //	shift down the image
      if (numRowToDelete > 0) {
         for (int row = image.MAX_HEIGHT - 1; row >= numRowToDelete; row--) {
            for (int col = 0; col < image.MAX_WIDTH; col++) {
               image.setPixel(row, col, image.getPixel((row - numRowToDelete), col));
               image.setPixel(row - numRowToDelete, col, false);
            }
         }
      }
   }

   /**
    * Utility method that sets the image to white (false).
    */
   private void clearImage() {
      for (int row = 0; row < image.MAX_HEIGHT; row++) {
         for (int col = 0; col < image.MAX_WIDTH; col++) {
            image.setPixel(row, col, false);
         }
      }
   }

   /**
    * Compute the height of the signal. Pre-condition: Image is scanned-in and cleaned.
    *
    * @return Height of signal.
    */
   private int computeSignalHeight() {
      int signalHeight = 0;

      for (int i = 0; i < image.MAX_HEIGHT; i++) {
         if (image.getPixel(i, 0)) { // if the square is black, it's the top of the spine
            signalHeight = (image.MAX_HEIGHT - i) - 2; // subtract top and bottom spine from signal height
            break;
         }
      }

      return signalHeight;
   }

   /**
    * Compute the width of the signal. Pre-condition: Image is scanned-in and cleaned.
    *
    * @return Width of signal.
    */
   private int computeSignalWidth() {
      int signalWidth = 0;

      while (image.getPixel(image.MAX_HEIGHT - 1, signalWidth)) signalWidth++;

      return signalWidth > 0 ? signalWidth - 2 : signalWidth; // subtract right and left spine
   }

   /**
    * Print out internal text.
    */
   public void displayTextToConsole() {
      System.out.println("The text stored is: " + text);
   }

   /**
    * Print the image without top-right blanks to the console.
    */
   public void displayImageToConsole() {
      if (image == null) return;

      int startingRowIndex = image.MAX_HEIGHT - (actualHeight + 2);

      for (int i = startingRowIndex; i < image.MAX_HEIGHT; i++) {
         for (int j = 0; j < actualWidth + 2; j++) {
            String maybeNewLine = j == actualWidth + 1 ? "\n" : ""; // newline to terminate row
            char symbolToPrint = image.getPixel(i, j) ? BLACK_CHAR : WHITE_CHAR;
            System.out.print(symbolToPrint + maybeNewLine);
         }
      }
   }

   /**
    * Print out full image data including any blanks in top and right.
    */
   public void displayRawImage() {
      if (image == null) return;

      for (int i = 0; i < image.MAX_HEIGHT; i++) {
         for (int j = 0; j < image.MAX_WIDTH; j++) {
            String maybeNewLine = j == image.MAX_WIDTH - 1 ? "\n" : ""; // newline to terminate row
            System.out.print(image.getPixel(i, j) + maybeNewLine);
         }
      }
   }

   /**
    * Generate image from internal text.
    *
    * @return Whether the image was able to be generated.
    */
   public boolean generateImageFromText() {
      if (text == null || text.length() == 0) {
         return false;
      }

      image = new BarcodeImage();
      actualHeight = 8; // ASCII Byte
      actualWidth = text.length();
      int startingRowIndex = image.MAX_HEIGHT - (actualHeight + 2); // situate at bottom left corner

      for (int i = startingRowIndex; i < image.MAX_HEIGHT; i++) {
         for (int j = 0; j < actualWidth + 2; j++) {
            if (j == 0) {// left spine solid
               image.setPixel(i, j, true);
            } else if (i == startingRowIndex && j % 2 == 0) { // top spine alternating
               image.setPixel(i, j, true);
            } else if (j == (actualWidth + 1)) {  // right border
               if (actualWidth % 2 == 1) { // if text length is odd, then border alternates on evens
                  if (i % 2 == 0) image.setPixel(i, j, true);
               } else {
                  if (i % 2 == 1) image.setPixel(i, j, true);
               }
            } else if (i == (image.MAX_HEIGHT - 1)) { // bottom spine solid
               image.setPixel(i, j, true);
            }

            if (i == startingRowIndex && j < actualWidth) {
               writeCharToCol(j + 1, (int) text.charAt(j)); // write char in j+1 column
            }
         }
      }

      return true;
   }

   public int getActualHeight() {
      return actualHeight;
   }

   public int getActualWidth() {
      return actualWidth;
   }

   /**
    * Read in a text string.
    *
    * @param text Text to use internally.
    * @return Whether the text was able to be set.
    */
   public boolean readText(String text) {
      boolean isValid = text != null && text.length() > 0 && text.length() <= image.MAX_WIDTH - 2;

      if (isValid) {
         this.text = text;
      }

      return isValid;
   }

   /**
    * Get ASCII character from a single column in the data. Pre-condition: Image is scanned-in and cleaned.
    *
    * @param col Column in data to read from.
    * @return Character represented by column.
    */
   private char readCharFromCol(int col) {
      int colValue = 0;
      int startingRowIndex = image.MAX_HEIGHT - (actualHeight + 1);

      for (int i = startingRowIndex; i < image.MAX_HEIGHT - 1; i++) {
         if (image.getPixel(i, col)) {
            int highestPowerOf2 = actualHeight - 1;
            int offset = i - startingRowIndex;
            colValue += Math.pow(2, highestPowerOf2 - offset);
         }
      }

      return (char) colValue;
   }

   public boolean scan(BarcodeImage bc) {
      if (bc == null) return false;

      try {
         image = (BarcodeImage) bc.clone();
      } catch (CloneNotSupportedException e) {
         return false;
      }

      cleanImage();
      actualHeight = computeSignalHeight();
      actualWidth = computeSignalWidth();

      return true;
   }

   /**
    * Generate text from internal image. Pre-condition: Image is scanned-in and cleaned.
    *
    * @return Whether the text was able to be generated.
    */
   public boolean translateImageToText() {
      if (image == null) {
         return false;
      }

      String text = "";

      for (int i = 1; i <= actualWidth; i++) {
         text += readCharFromCol(i);
      }

      this.text = text;

      return true;
   }

   /**
    * Encode an ASCII character into column of signal data.
    *
    * @param col  The column in the data to write the character.
    * @param code The character in ASCII. Whether the character was able to be written.
    * @return
    */
   private boolean writeCharToCol(int col, int code) {
      String binaryString = Integer.toBinaryString(code);
      int stringLength = binaryString.length();
      int startingRowIndex = image.MAX_HEIGHT - (actualHeight + 1);

      // PadLeft
      while (stringLength < actualHeight) {
         binaryString = "0" + binaryString;
         stringLength++;
      }

      for (int i = startingRowIndex; i < image.MAX_HEIGHT - 1; i++) {
         int offset = i - startingRowIndex;
         image.setPixel(i, col, binaryString.charAt(offset) == '1');
      }

      return true;
   }
}


/* -------------------- Output ---------------------------*
The text stored is: CSUMB CSIT online program is top notch.
* * * * * * * * * * * * * * * * * * * * *
*                                       *
****** **** ****** ******* ** *** *****
*     *    ******************************
* **    * *        **  *    * * *   *
*   *    *  *****    *   * *   *  **  ***
*  **     * *** **   **  *    **  ***  *
***  * **   **  *   ****    *  *  ** * **
*****  ***  *  * *   ** ** **  *   * *
*****************************************
The text stored is: You did it!  Great work.  Celebrate.
* * * * * * * * * * * * * * * * * * *
*                                    *
**** *** **   ***** ****   *********
* ************ ************ **********
** *      *    *  * * *         * *
***   *  *           * **    *      **
* ** * *  *   * * * **  *   ***   ***
* *           **    *****  *   **   **
****  *  * *  * **  ** *   ** *  * *
**************************************
The text stored is: What a great resume builder this is!
* * * * * * * * * * * * * * * * * * *
*                                    *
***** * ***** ****** ******* **** **
* ************************************
**  *    *  * * **    *    * *  *  *
* *               *    **     **  *  *
**  *   * * *  * ***  * ***  *
**      **    * *    *     *    *  * *
** *  * * **   *****  **  *    ** ***
**************************************
-------------------------------------------------------- */