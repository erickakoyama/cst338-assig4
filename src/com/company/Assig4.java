package com.company;
//

/**
 * Project Members: Ericka Koyama, Holly Stephens, Ngoc Tran Do CST 338 Software Design Assignment 4 - Barcode Scanner
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

//      BarcodeImage bc = new BarcodeImage(sImageIn);
//      DataMatrix dm = new DataMatrix(bc);
//
//      // First secret message
//      dm.translateImageToText();
//      dm.displayTextToConsole();
//      dm.displayImageToConsole();
//
//      // second secret message
//      bc = new BarcodeImage(sImageIn_2);
//      dm.scan(bc);
//      dm.translateImageToText();
//      dm.displayTextToConsole();
//      dm.displayImageToConsole();
//
//      // create your own message
//      dm.readText("What a great resume builder this is!");
//      dm.generateImageFromText();
//      dm.displayTextToConsole();
//      dm.displayImageToConsole();
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
 * Store and retrieve the 2D data.
 */
class BarcodeImage implements Cloneable {
   public static final int MAX_HEIGHT = 30;
   public static final int MAX_WIDTH = 65;
   private boolean[][] imageData; // Stores the inbound image. White = false, black = true.

   /**
    * Default constructor. Instatiates a 2D array.
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
      if (null == data || data.length >= MAX_HEIGHT) return false;
      for (int i = 0; i < data.length; i++) {
         if (null == data[i] || data[i].length() >= MAX_WIDTH) return false;
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
   public BarcodeImage clone() throws CloneNotSupportedException {
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

   private void cleanImage() {
      boolean[][] newImage = new boolean[47][16]; //1st string example
      int amountColtoMove = 0, downNum = 1, numRowToDelete;
      int maxRow = newImage.length;

      for (int i = 0; i < newImage.length; i++) {
         for (int j = 0; j < newImage[i].length; j++) {
            if (newImage[i][j] == true) {
               amountColtoMove = j;
               downNum++;
               break;
            }
         }
      }
      numRowToDelete = maxRow - downNum;
      newImage = shiftImage(numRowToDelete, amountColtoMove, newImage);

   }

   private boolean[][] shiftImage(int offset1, int offset2, boolean img[][]) {

      boolean[][] shiftedArray = new boolean[img.length][img[0].length];
      for (int row = 0; row < img.length; row++) {
         for (int col = 0; col < img[row].length; col++) {
            shiftedArray[row][col] = img[row][(col + 1) % img[row].length];
         }
      }
      return shiftedArray;
   }
   
   /**
    * Utility method that sets the image to white (false).
    */
   private void clearImage() {
      boolean[][] tempImage = new boolean[47][16];
      for(int row = 0; row < tempImage.length; row++) {
         for(int col = 0; col < tempImage[row].length; col++) {
            tempImage[row][col] = false;
         }
      }
   }

   /**
    * Compute the height of the signal.
    * Pre-condition: Image is scanned-in and cleaned.
    * @return Height of signal.
    */
   private int computeSignalHeight() {
      image = new BarcodeImage(getPreFormattedMockStringInput()); // TODO: Remove this once we have constructors
      int signalHeight = 0;

      for (int i = 0; i < image.MAX_HEIGHT; i++) { // should be i < image.MAX_HEIGHT
         if (image.getPixel(i, 0)) { // if the square is black, it's the top of the spine
            signalHeight = (image.MAX_HEIGHT - i) - 2; // subtract top and bottom spine from signal height
            break;
         }
      }

      return signalHeight;
   }

   /**
    * Compute the width of the signal.
    * Pre-condition: Image is scanned-in and cleaned.
    * @return Width of signal.
    */
   private int computeSignalWidth() {
      image = new BarcodeImage(getPreFormattedMockStringInput()); // TODO: Remove this once we have constructors
      int signalWidth = 0;

      while (image.getPixel(image.MAX_HEIGHT - 1, signalWidth)) signalWidth++;

      return signalWidth - 2; // subtract right and left spine
   }

   public void displayTextToConsole() {
   }

   public void displayImageToConsole() {
   }

   /**
    * Print out full image data including any blanks in top and right.
    */
   public void displayRawImage() {
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
            System.out.println("i: " + i + ", j: " + j);
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

   private boolean[][] getMockImageGrid() {  // TODO: TEMP DO NOT SUBMIT THIS!!!!!
      boolean[][] mockImageData = new boolean[][]{ // 10 x 20 grid, signal is already pushed to bottom-left; extra
            // blank column on right
            {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false,
                  false, false, false, false, false}, // blank row
            {true, false, true, false, true, false, true, false, true, false, true, false, true, false, true, false,
                  true, false, true, false},
            {true, true, false, false, false, false, false, false, false, false, false, false, false, false, false,
                  false, false, false, false, false},
            {true, false, false, false, false, false, false, false, false, false, false, false, false, false, false,
                  false, false, false, true, false},
            {true, true, false, false, false, false, false, false, false, false, false, false, false, false, false,
                  false, false, false, false, false},
            {true, false, false, false, false, false, false, false, false, false, false, false, false, false, false,
                  false, false, false, true, false},
            {true, true, false, false, false, false, false, false, false, false, false, false, false, false, false,
                  false, false, false, false, false},
            {true, false, false, false, false, false, false, false, false, false, false, false, false, false, false,
                  false, false, false, true, false},
            {true, false, false, false, false, false, false, false, false, false, false, false, false, false, false,
                  false, false, false, false, false},
            {true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true,
                  true, true, false},
      };

      return mockImageData;
   }

   private String[] getPreFormattedMockStringInput() { // TODO: TEMP DO NOT SUBMIT THIS
      String[] sImage_preformat =
            new String[]{
                  "                                          ",
                  "                                          ",
                  "                                          ",
                  "                                          ",
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

            };

      return sImage_preformat;
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
    * Get ASCII character from a single column in the data.
    * Pre-condition: Image is scanned-in and cleaned.
    *
    * @param col Column in data to read from.
    * @return Character represented by column.
    */
   public char readCharFromCol(int col) {
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
      return true;
   }

   /**
    * Generate text from internal image.
    * Pre-condition: Image is scanned-in and cleaned.
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
   public boolean writeCharToCol(int col, int code) {
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

