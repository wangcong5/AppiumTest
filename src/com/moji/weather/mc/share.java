package com.moji.weather.mc;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.TakesScreenshot;

public class share extends mojiWeatherBase {
	// 分享功能

	/**
	 * 2016/09/25
	 * 
	 * @author MC
	 * @throws InterruptedException
	 */
	
	public void share() throws InterruptedException {
		driver.findElement(By.id("com.moji.mjweather:id/share_iv")).click();
		snapshot((TakesScreenshot) driver, "分享页面.png");
		Thread.sleep(10000);
	}
}
