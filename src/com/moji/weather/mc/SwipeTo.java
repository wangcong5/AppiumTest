package com.moji.weather.mc;

import io.appium.java_client.android.AndroidDriver;

public class SwipeTo {
	public void swipeToUp(AndroidDriver driver, int during){
		  int width = driver.manage().window().getSize().width;
		  int height = driver.manage().window().getSize().height;
		  driver.swipe(width / 2, height * 3/ 4, width /2 , height /4, during);
		 }
		 
		 public void swipeToDown(AndroidDriver driver, int during){
		  int width = driver.manage().window().getSize().width;
		  int height = driver.manage().window().getSize().height;
		  System.out.println(width );
		  System.out.println(height);
		  driver.swipe(width / 2, height / 4, width /2 , height * 3 /4, during);
		 }
		 
		 public void swipeToLeft(AndroidDriver driver, int during){
		  int width = driver.manage().window().getSize().width;
		  int height = driver.manage().window().getSize().height;
		  driver.swipe(width * 3 / 4 , height / 2, width / 4, height / 2, during);
		 }
		 
		 public void swipeToRight(AndroidDriver driver, int during){
		  int width = driver.manage().window().getSize().width;
		  int height = driver.manage().window().getSize().height;
		  driver.swipe(width / 4, height / 2, width * 3 / 4, height / 2, during);
		 }
}
