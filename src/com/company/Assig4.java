package com.company;

/**
 * Project Members: Ericka Koyama, Holly Stephens, Ngoc Tran Do
 * CST 338 Software Design
 * Assignment 4 - Barcode Scanner
 */


public class Assig4 implements {

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
}

/**
 *
 */
class DataMatrix implements BarcodeIO {
   
   public DataMatrix()
	{
	  image = new BarcodeImage();
	  text = "";
	  actualWidth = 0;
	  actualHeight = 0;
	}
   
   public DataMatrix(BarcodeImage pix)
	{
	  if(!scan(pix))
	   image = new BarcodeImage();
	  else
	  {
      // in guideline need to use set "actualHeight" and "actualWidth" in scan(BarcodeImage bc)
	  }

	  text = "";
	}
   
   public matrix(String text)
	{
	  image = new BarcodeImage();
	  // need to use readText() method;
	  
	}
   
   public void readText(String text) //for constructor matrix(String text)
   {
   }
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
