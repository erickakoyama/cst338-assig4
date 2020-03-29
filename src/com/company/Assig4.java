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
 * Store and retreive the 2D data.
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
         for(int row = 0; row < MAX_HEIGHT; row++) {
            for(int col = 0; col < MAX_WIDTH; col++) {
               imageData[row][col] = false;
            }
         }
   }

   /**
    * Overloaded constructor. Takes a 1D array of Strings and converts it to a 2D array.
    * @param strData The 1D array of Strings to convert.
    */
   BarcodeImage(String[] strData) {
      imageData = new boolean[MAX_HEIGHT][MAX_WIDTH];
      if(checkSize(strData))
      {
         /**
          * Read string array in bottom up to pad to lower left corner.
          * Since [0][0] is the top left corner, this means [MAX_HEIGHT][0] is our starting position.
          * MAX_HEIGHT - strData.length will be the last row of image values
          */
         int row = MAX_HEIGHT;
         /**
          * Grab strings in reverse order from strData
          * col will correspond to both the current position of the current string and the current position of our inner 2D array.
          */ 
         for(int i = strData.length; i > 0; i--) {
            String line = strData[i-1];
            int col = 0;
            /*
             * We want to fill our 2D array with false values after we exceed the length of our string.
             * This is why it's necessary to loop through MAX_WIDTH each time.
             */
            while(col < MAX_WIDTH) {
               boolean value = false; // Default value.
               if(col < line.length()) { // Check that we've not exceeded string length before trying to access data.
                  value = (line.charAt(col) == '*')? true : false; // Assign boolean representation of string data.
               }
               imageData[row-1][col] = value;
               col++;
            }
            row--; // Decrement row as we loop to grab the next string.
         }
         // Fill top remaining space in imageData with false.
         while(row > 0){
            for(int j = 0; j < MAX_WIDTH; j++) {
               imageData[row-1][j] = false;
            }
            row--;
         }
      }
   }

   /**
    * Accessor for each bit in the image.
    * @param row The position of the outer array.
    * @param col The position of the inner array.
    * @return The data in the specified location or false if there is an error accessing the data.
    */
   public boolean getPixel(int row, int col) {
      if(row < MAX_HEIGHT && col < MAX_WIDTH) {
         return imageData[row][col];
      }
      return false;
   }

   /**
    * Accessor for each bit in the image.
    * @param row The position of the outer array.
    * @param col The position of the inner array.
    * @param value The value to be placed in the specified location.
    * @return True on success, false on failure.
    */
   public boolean setPixel(int row, int col, boolean value) {
      if(row < MAX_HEIGHT && col < MAX_WIDTH) {
         imageData[row][col] = value;
         return true;
      }
      return false;
   }

   /**
    * Utility method. Checks the incoming data for every conceivable size or null error.
    * @param data The image data.
    * @return True if data is okay, false otherwise.
    */
   private boolean checkSize(String[] data) {
      if(null == data || data.length >= MAX_HEIGHT) return false;
      for(int i = 0; i < data.length; i++) {
         if(null == data[i] || data[i].length() >= MAX_WIDTH) return false;
      }
      return true;
   }

   /**
    * Utility method. Outputs the contents of imageData.
    * Converts back to the image format 
    */
   public void displayToConsole() {
      for(int i = 0; i < MAX_HEIGHT; i++) {
         String line = "";
         for(int j = 0; j < MAX_WIDTH; j++) {
            String value = (imageData[i][j])? "*" : " ";
            line = line + value;
         }
         System.out.println(line);
      }
   }

   /**
    * An override of the clone method in Cloneable interface.
    * @return a copy of the BarcodeImage object.
    */
   public BarcodeImage clone() throws CloneNotSupportedException {
      try {
         return (BarcodeImage) super.clone();
      }
      catch(CloneNotSupportedException e) {
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
      // this method help to resize the image to the right position. get call by scan(barcodeImage pic)
      /// somehow take bc obj(from mainclass), and since the object should contain the updated array size?
      // to have an 2d array /
      boolean newImage[][] = new boolean[47][16]; //1st string example
      int amountColtoMove = 0, downNum = 1, numRowToDelete;
      int maxRow = newImage.length;

      for ( int i = 0; i < newImage.length; i++ ) {
         for ( int j = 0; j < newImage[i].length; j++ ) {
            if ( newImage[i][j] == true) {
               amountColtoMove = j;
               downNum++;
               break;
            }
         }
      }
      numRowToDelete = maxRow - downNum;
      shiftImageDown(numRowToDelete);
      shiftImageLeft(amountColtoMove);
   }

   private void shiftImageDown(int offset) {}
   private void shiftImageLeft(int offset) {}

   /**
    * Compute the height of the signal.
    * Pre-condition: Signal is placed at bottom-left of image.
    * @return Height of signal.
    */
   private int computeSignalHeight() { // TODO: Update this method once we get BarcodeImage data.
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
   private int computeSignalWidth() { // TODO: Update this method once we get BarcodeImage data.
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

   public void displayTextToConsole() {
   }

   public void displayImageToConsole() {
   }

   /**
    * Generate image from internal text.
    * @return Whether the image was able to be generated.
    */
   public boolean generateImageFromText() {
      if (text == null) {
         return false;
      }

      int stringSize = text.length() + 2;
      String[] strData = new String[stringSize];
      BarcodeImage newImage = new BarcodeImage();

      // TODO: WIP


      return true;
   }

   public int getActualHeight() {
      return actualHeight;
   }

   public int getActualWidth() {
      return actualWidth;
   }

   private boolean[][] getMockImageGrid() {  // TODO: TEMP DO NOT SUBMIT THIS!!!!!
      boolean[][] mockImageData = new boolean[][]{ // 10 x 20 grid, signal is already pushed to bottom-left; extra blank column on right
            {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false}, // blank row
            {true, false, true, false, true, false, true, false, true, false, true, false, true, false, true, false, true, false, true, false},
            {true, true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false},
            {true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true, false},
            {true, true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false},
            {true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true, false},
            {true, true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false},
            {true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true, false},
            {true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false},
            {true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, false},
      };

      return mockImageData;
   }

   /**
    * Read in a text string.
    * @param text Text to use internally.
    * @return Whether the text was able to be set.
    */
   public boolean readText(String text) {
      boolean isValid = text != null && text.length() <= image.MAX_WIDTH - 2;

      if (isValid) {
         this.text = text;
      }

      return isValid;
   }

   public boolean scan(BarcodeImage bc) {
      return true;
   }

   /**
    * Get ASCII character from a single column in the data.
    * @param col Column in data to read from.
    * @return    Character represented by column.
    */
   public char readCharFromCol(int col) { // TODO: Update after BarcodeImage is done
      int colValue = 0;
      boolean[][] mockImage = getMockImageGrid();
      int mockActualHeight = 8;
      int startingRowIndex = mockImage.length - (mockActualHeight + 1);

      // i should start from image.MAX_HEIGHT - (actualHeight + 1); i < image.MAX_HEIGHT - 1
      for(int i = startingRowIndex; i < mockImage.length - 1; i++) {
         if (mockImage[i][col]) {
            int highestPowerOf2 = mockActualHeight - 1;
            int offset = i - startingRowIndex;
            colValue += Math.pow(2, highestPowerOf2 - offset);
         }
      }

      return (char) colValue;
   }

   /**
    * Generate text from internal image.
    * @return Whether the text was able to be generated.
    */
   public boolean translateImageToText() {
      if (image == null) {
         return false;
      }

      String text = "";
      int mockActualWidth = 8;

      for (int i = 1; i <= mockActualWidth; i++) {
         text += readCharFromCol(i);
      }

      this.text = text;

      return true;
   }

   /**
    * Encode an ASCII character into column of signal data.
    * @param col  The column in the data to write the character.
    * @param code The character in ASCII.
    *             Whether the character was able to be written.
    * @return
    */
   public boolean writeCharToCol(int col, int code) { // TODO: Update after BarcodeImage is done
      if (code < 0 || code > 255) {
         return false;
      }

      String binaryString = Integer.toBinaryString(code);
      int stringLength = binaryString.length();
      int mockActualHeight = 8;
      boolean[][] mockImage = getMockImageGrid();
      int startingRowIndex = mockImage.length - (mockActualHeight + 1);

      // PadLeft
      while (stringLength < mockActualHeight)
      {
         binaryString = "0" + binaryString;
         stringLength++;
      }

      // i should start from image.MAX_HEIGHT - (actualHeight + 1); i < image.MAX_HEIGHT - 1
      for(int i = startingRowIndex; i < mockImage.length - 1; i++) {
         if (mockImage[i][col]) {
            int offset = i - startingRowIndex;
            mockImage[i][col] = binaryString.charAt(offset) == '1';
         }
      }

      return true;
   }
}

