package com.moji.weather.mc;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidKeyCode;

public class lib {
	public static AndroidDriver<WebElement> driver;
	// private static AndroidDriver driver;
	String[] cityNameList = { "北京市", "天津市", "上海市", "重庆市", "沈阳市", "大连市", "长春市", "哈尔滨市" };

	@Before
	public void setUp() throws InterruptedException, IOException {
		File classpathRoot = new File(System.getProperty("user.dir"));
		File appDir = new File(classpathRoot, "apps");
		File app = new File(appDir, "mojiweather-V6.0002.02-20160817-release-5055.apk");

		// 判断apk是否存在
		if (!app.exists()) {
			System.out.println("本次需要安装的apk不存在");
		}

		// 设置启动参数
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability(CapabilityType.BROWSER_NAME, "");
		// support Chinese
		capabilities.setCapability("unicodekeyboard", "True");
		capabilities.setCapability("resetkeyboard", "True");
		// no need sign
		capabilities.setCapability("noSign", "True");
		capabilities.setCapability("platformName", "Android");
		capabilities.setCapability("deviceName", "Android Emulator");
		capabilities.setCapability("platformVersion", "4.4");
		// if no need install don't add this
		capabilities.setCapability("app", app.getAbsolutePath());
		capabilities.setCapability("app-package", "com.moji.mjweather");
		capabilities.setCapability("app-activity", "com.moji.activity.MainActivity");
		driver = new AndroidDriver<>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
		// 设置隐式等待超时时间
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		Thread.sleep(10000);
	}

	@After
	public void tearDown() {
		driver.quit();
	}

	/**
	 * 新手教程页面 1.首次安装，有新手教程，通过滑动跳过； 2.新手教程为4页时，for循环i<3；
	 * 3.新手教程为5页时，for循环i<4，同时注意默认下载应用后弹出安装提示框的处理；
	 */

	public void swipes() throws InterruptedException, IOException {
		int width = driver.manage().window().getSize().width;
		int height = driver.manage().window().getSize().height;
		Thread.sleep(5000);
		for (int i = 0; i < 4; i++) {
			driver.swipe(width * 13 / 16, height / 2, width / 16, height / 2, 1500);
			Thread.sleep(1000);
			System.out.println("第" + (i + 1) + "次滑动完成");
		}
		System.out.println("新手教程滑动完成");

		driver.findElementById("com.moji.mjweather:id/iv_content_clck_area").click();
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		addCity();
		deleteCity();
	}

	/**
	 * 向上滑动屏幕
	 * 
	 * @param driver
	 * @param during
	 */

	public void swipeToUp(AndroidDriver<WebElement> driver, int during) {
		int width = driver.manage().window().getSize().width;
		int height = driver.manage().window().getSize().height;
		// 取1/4屏宽，取1/2会点到时景页面相机，无法向上滑动
		driver.swipe(width / 4, height * 13 / 16, width / 4, height / 16, during);
	}

	/**
	 * 向下滑动
	 * 
	 * @param driver
	 * @param during
	 */
	public void swipeToDown(AndroidDriver<WebElement> driver, int during) {
		int width = driver.manage().window().getSize().width;
		int height = driver.manage().window().getSize().height;
		// height改成3/16，否则向下滑动时，第一个落点在底部Title栏，导致向下滑动失败
		driver.swipe(width / 2, height * 3 / 16, width / 2, height * 13 / 16, during);
		// wait for page loading
	}

	// 向左
	public void swipeToLeft(AndroidDriver<WebElement> driver, int during) {
		int width = driver.manage().window().getSize().width;
		int height = driver.manage().window().getSize().height;
		driver.swipe(width * 13 / 16, height / 2, width / 16, height / 2, during);
		// wait for page loading
	}

	// 向右
	public void swipeToRight(AndroidDriver<WebElement> driver, int during) {
		int width = driver.manage().window().getSize().width;
		int height = driver.manage().window().getSize().height;
		driver.swipe(width / 16, height / 2, width * 13 / 16, height / 2, during);
		// wait for page loading
	}

	/**
	 * 1.从天气首页点击城市名进入城市管理列表点击添加城市的“+”，进入城市搜索页面 依次添加8个城市；
	 * 2.添加第10个城市，弹出“最多添加9个城市”提示
	 * 
	 * @throws InterruptedException
	 */
	// 添加城市
	/**
	 * 城市名优化，控件可能会发生变化，改下resourceId即可
	 * @throws InterruptedException
	 */
	
	public void addCity() throws InterruptedException {
		// String[] cityNameList = {"北京市", "天津市", "上海市", "重庆市", "沈阳市", "大连市",
		// "长春市", "哈尔滨市" };
		for (int i = 0; i < cityNameList.length; i++) {
			// 进入城市管理列表
			driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
			driver.findElementById("com.moji.mjweather:id/area_name_tv").click();
			Thread.sleep(5000);

			List<WebElement> list = driver.findElementsByClassName("android.widget.ImageView");// 获取ImageView的所有元素
			WebElement targetEle = list.get(2);// 获取列表中第二个ImageView元素,进入添加城市
			targetEle.click();

			WebElement el = driver.findElement(By.name(cityNameList[i].toString()));
			el.click();

			if (i == 7) {
				// 添加完最后一个城市会进入到天气首页，再点击一次首页城市名，进入城市管理页面
				driver.findElementById("com.moji.mjweather:id/area_name_tv").click();
				snapshot((TakesScreenshot) driver, "城市管理列表.png");
			}
		}
		// 添加第十个城市
		// 获取ImageView的所有元素
		List<WebElement> list = driver.findElementsByClassName("android.widget.ImageView");
		WebElement targetEle = list.get(2);// 获取列表中第二个ImageView元素,进入添加城市
		targetEle.click();
		snapshot((TakesScreenshot) driver, "添加第十个城市.png");
	}

	/**
	 * 删除城市 1.从首页进入城市管理列表，删除所有9个城市 2.删完所有城市，页面停留在“搜索城市”页面，可通过添加城市进入天气首页
	 * 
	 * @author MC
	 * @throws InterruptedException
	 */
	
	public void deleteCity() throws InterruptedException {
		 driver.findElementById("com.moji.mjweather:id/area_name_tv").click();//模块调试
		// 城市管理列表编辑
		List<WebElement> list0 = driver.findElementsByClassName("android.widget.ImageView");// 获取ImageView的所有元素

		// snapshot((TakesScreenshot) driver, "准备删除.png");
		// 循环删除城市
		for (int i = 0; i < cityNameList.length + 1; i++) {
			WebElement targetEle0 = list0.get(1);// 获取列表中第二个ImageView元素(笔icon),准备删除城市
			targetEle0.click();
			driver.findElementById("com.moji.mjweather:id/item_city_name_handle").click();
			WebElement e1 = driver.findElementByName("删除");
			e1.click();

			List<WebElement> list1 = driver.findElementsByClassName("android.widget.ImageView");// 获取ImageView的所有元素
			WebElement targetEle1 = list1.get(0);// 获取列表中第一个ImageView元素,准备删除城市
			targetEle1.click();
			Thread.sleep(1000);
			if (i == 8) {
				snapshot((TakesScreenshot) driver, "删除城市后.png");
			}
		}
	}

	/**
	 * 天气首页操作
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void homePage() throws InterruptedException {
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		driver.findElementById("com.moji.mjweather:id/iv_voice").click();
		Thread.sleep(5000);
		snapshot((TakesScreenshot) driver, "天气首页.png");

		// 直接文本定位
		driver.findElementByAndroidUIAutomator("new UiSelector().text(\"反馈天气\")").click();

		List<WebElement> listReport = driver.findElementsByClassName("android.widget.TextView");// 获取TextView的所有元素
		WebElement targetReport = listReport.get(3);// 获取列表中第四个Textview，即“阴”
		targetReport.click();
		// 点击【发布】按钮
		driver.findElementByXPath(
				"//android.widget.Button[@resource-id=\"com.moji.mjweather:id/btn_weather_feedback_publish\"]").click();
		Thread.sleep(2000);

		// 输入文字
		/*
		 * driver.sendKeyEvent(AndroidKeyCode.HOME);
		 * driver.sendKeyEvent(AndroidKeyCode.BACK);
		 * //获取当前界面的activity,可用于断言是否跳转到预期的activity driver.currentActivity();
		 * driver.sendKeyEvent(AndroidKeyCode.HOME);
		 * 
		 * el = driver.findElementByName("Add note");
		 * assertThat(el.getText(),equalTo("Add note"));
		 */
		login();
	}

	/**
	 * 登录模块 Oppo A31调试通过，联想乐檬X3不能正常输入，或与机器本身系统有关，暂不解决
	 * 
	 * @throws InterruptedException
	 */
	
	public void login() throws InterruptedException {

		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		driver.findElementByAndroidUIAutomator("new UiSelector().text(\"我\")").click();
		Thread.sleep(1000);
		// 点击【我】页面手机icon
		driver.findElement(By.id("com.moji.mjweather:id/tv_login_phone")).click();
		Thread.sleep(1000);
		// 点击【我】页面“使用密码登录”
		driver.findElementById("com.moji.mjweather:id/tv_login_by_email").click();

		List<WebElement> textFieldsList = driver.findElementsByClassName("android.widget.EditText");
		textFieldsList.get(0).sendKeys("15527913638");
		textFieldsList.get(1).sendKeys("123456");
		// 另一种输入方法，与List效果一致
		/*
		 * driver.findElement(By.id(
		 * "com.moji.mjweather:id/et_login_input_account")).sendKeys(
		 * "15527913638"); driver.findElement(By.id(
		 * "com.moji.mjweather:id/et_login_input_password")).sendKeys("123456");
		 */
		driver.findElement(By.id("com.moji.mjweather:id/tv_action_login")).click();
		//登录成功后截屏
		snapshot((TakesScreenshot) driver, "登录成功页面.png");
		
		// 进入时景页面
		driver.findElementByAndroidUIAutomator("new UiSelector().text(\"时景\")").click();
		Thread.sleep(1000);
		System.out.println("等待时景页面加载...");
		Assert.assertTrue(driver.findElement(By.name("此刻")).isDisplayed());// 调试信息，等待时景页面加载完成
		System.out.println("时景页面加载完成");
		// 时景页向上滑动10次
		swipeToLeft(driver, 1000);
		swipeToRight(driver, 1000);
		swipe();
		driver.findElementByAndroidUIAutomator("new UiSelector().text(\"天气\")").click();
		driver.findElementById("com.moji.mjweather:id/iv_aqi_icon").click();
		Thread.sleep(10000);
	}

	/**
	 * 调试模块 向下不能滑动，已解决~~~
	 * 
	 * @throws InterruptedException
	 */

	
	public void swipe() throws InterruptedException {
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		// find keyword 我 and verify it is display
		Assert.assertTrue(driver.findElement(By.name("我")).isDisplayed());
		// Thread.sleep(10000);
		swipeToLeft(driver, 500);
		Thread.sleep(1000);

		swipeToRight(driver, 500);
		Thread.sleep(1000);

		for (int i = 0; i < 5; i++) {
			swipeToUp(driver, 500);
			System.out.println("第" + i + "次向上滑动完成");
			Thread.sleep(1000);
		}

		Thread.sleep(1000);
		System.out.println("-------------------");

		for (int i = 0; i < 5; i++) {
			swipeToDown(driver, 1000);
			System.out.println("第" + i + "次向下滑动完成");
			Thread.sleep(1000);
		}

		System.out.println("向下滑动完毕");
		Thread.sleep(5000);

	}
	
	//分享功能
	
	/**2016/09/25
	 * @author MC
	 * @throws InterruptedException
	 */
	public void share() throws InterruptedException{
		driver.findElement(By.id("com.moji.mjweather:id/share_iv")).click();
		snapshot((TakesScreenshot) driver, "分享页面.png");
		Thread.sleep(10000);
	} 

	/**
	 * 截屏 1.将相应页面截下来保存到指定磁盘位置
	 * 
	 * @param drivername
	 * @param filename
	 */
	public static void snapshot(TakesScreenshot drivername, String filename) {
		// this method will take screen shot ,require two parameters ,one is
		// driver name, another is file name

		String currentPath = System.getProperty("user.dir"); // get current work
		File scrFile = drivername.getScreenshotAs(OutputType.FILE);

		try {
			System.out.println("save snapshot path is:" + currentPath + "/" + filename);
			FileUtils.copyFile(scrFile, new File(currentPath + "\\" + filename));
		} catch (IOException e) {
			System.out.println("Can't save screenshot");
			e.printStackTrace();
		} finally {
			System.out.println("screen shot finished, it's in " + currentPath + " folder");
		}
	}

	/**
	 * 录制 1.录制MP4格式的视频文件，将所有操作记录下来
	 * 
	 * @throws IOException
	 */
	public void startRecord() throws IOException {
		Runtime rt = Runtime.getRuntime();
		// this code for record the screen of your device
		rt.exec("cmd.exe /C adb shell screenrecord /sdcard/runCase.mp4");
	}

	public void profileSetting() {

		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

		// Assert.assertTrue(driver.findElement(By.name("首页")).isDisplayed());
		WebElement myButton = driver.findElement(By.className("android.widget.ImageButton"));
		myButton.click();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.swipe(700, 500, 100, 500, 10);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		List<WebElement> textViews = driver.findElementsByClassName("android.widget.TextView");
		textViews.get(0).click();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

		List<WebElement> showClose = driver.findElementsById("com.moji.android:id/showcase_close");
		if (!showClose.isEmpty()) {
			snapshot((TakesScreenshot) driver, "moji_showClose.png");
			showClose.get(0).click();
		}
		Assert.assertTrue(
				driver.findElementsByClassName("android.widget.TextView").get(0).getText().contains("selenium"));

		driver.findElementById("com.moji.android:id/menu_people_edit").click();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		WebElement intro = driver.findElementById("com.moji.android:id/introduction");
		intro.click();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		WebElement content = driver.findElementById("com.moji.android:id/content");
		String text = content.getAttribute("text");
		content.click();
		// clearText(text);
		content.sendKeys("Appium Test. Create By Young");

		driver.findElementById("com.moji.android:id/menu_question_done").click();

		WebElement explanation = driver.findElementById("com.moji.android:id/explanation");
		explanation.click();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		content = driver.findElementById("com.moji.android:id/content");
		text = content.getAttribute("text");
		content.click();
		// clearText(text);
		content.sendKeys("Appium Test. Create By Young. This is an appium type hahahahah");
		snapshot((TakesScreenshot) driver, "moji.png");

	}

	/////////////////////////////////////////////////////////////////////////
	/**
	 * This method for delete text in textView reference
	 * http://www.cnblogs.com/tobecrazy/p/4592405.html
	 * 
	 * @param text
	 */

}
