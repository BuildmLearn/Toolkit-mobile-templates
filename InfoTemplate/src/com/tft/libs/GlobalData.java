package com.tft.libs;

public class GlobalData {
	   private static GlobalData instance = null;
	   String iTitle = null;
	   int iSelectedIndex = -1;
	   protected GlobalData() {
	      // Exists only to defeat instantiation.
	   }
	   public static GlobalData getInstance() {
	      if(instance == null) {
	         instance = new GlobalData();
	      }
	      return instance;
	   }
	}