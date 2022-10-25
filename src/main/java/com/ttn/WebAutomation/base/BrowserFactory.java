package com.ttn.WebAutomation.base;

/**
 * This Java program demonstrates the Browser initiated code .
 *
 * @author TTN
 */

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.testng.Reporter;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import com.ttn.WebAutomation.listeners.MyProjectListener;

//import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class BrowserFactory {
	public static Map<String, EventFiringWebDriver> drivers = new HashMap<String, EventFiringWebDriver>();
	private WebDriver wDriver;
	private static MyProjectListener listeners;
	private EventFiringWebDriver driver;
	private RemoteWebDriver rDriver;
	private String exePath= System.getProperty("user.dir") + "/resources/exefiles";

	/*
	 * Normal method for getting browsers
	 */
	public EventFiringWebDriver getBrowser(String browserName,String Port) throws MalformedURLException {
		driver = null;
		String l_browserName = browserName.toLowerCase();
		switch (l_browserName) {
		case "firefox":
			configFirefoxDriver();
			break;
		case "ie":
			configIEDriver();
			break;
		case "chrome":
			configChromeDriver();
			break;
		case "safari":
			configSafariDriver();
			break;
		default:
			configChromeDriver();
		}
		return driver;
	}

	/*
	 * Singleton Design Pattern for getting browsers
	 */
	public EventFiringWebDriver getSingletonBrowser(String browserName,String port) throws MalformedURLException {
		driver = null;
		String l_browserName = browserName.toLowerCase();
		switch (l_browserName) {
		case "firefox":
			driver = drivers.get("firefox");
			if (driver != null) {
				FirefoxDriver f = (FirefoxDriver) driver.getWrappedDriver();
				if (f.getSessionId() == null) {
					configFirefoxDriver();
					drivers.put("firefox", driver);
				}
			} else {
				configFirefoxDriver();
				drivers.put("firefox", driver);
			}
			break;
		case "ie":
			driver = drivers.get("ie");
			if (driver != null) {
				InternetExplorerDriver ie = (InternetExplorerDriver) driver.getWrappedDriver();
				if (ie.getSessionId() == null) {
					configIEDriver();
					drivers.put("ie", driver);
				}
			} else {
				configIEDriver();
				drivers.put("ie", driver);
			}
			break;
		case "chrome":

			driver = drivers.get("chrome");
			if (driver != null) {
				ChromeDriver cd = (ChromeDriver) driver.getWrappedDriver();
				if (cd.getSessionId() == null) {
					configChromeDriver();
					drivers.put("chrome", driver);
				}
			} else {
				configChromeDriver();
				drivers.put("chrome", driver);
			}
			break;
		case "safari":
			driver = drivers.get("safari");
			if (driver != null) {
				SafariDriver sd = (SafariDriver) driver.getWrappedDriver();
				if (sd.getSessionId() == null) {
					configSafariDriver();
					drivers.put("safari", driver);
				}
			} else {
				configSafariDriver();
				drivers.put("safari", driver);
			}
			break;
		case "grid":

			if(port.equalsIgnoreCase("4547") ) {
				driver = drivers.get("chrome");
				if (driver != null) {
					RemoteWebDriver cd = (RemoteWebDriver) driver.getWrappedDriver();

					if (cd.getSessionId() == null) {
						//configChrome("4547");
						drivers.put("chrome", driver);
					}
				} else {
					//configChrome("4547");
					drivers.put("chrome", driver);
				}
			}
			else{
				if(port.equalsIgnoreCase("4545") )
				{
					driver = drivers.get("firefox");
					if (driver != null) {
						RemoteWebDriver cd = (RemoteWebDriver) driver.getWrappedDriver();

						if (cd.getSessionId() == null) {
							configFirefox("4545");
							drivers.put("firefox", driver);
						}
					} else {
						configFirefox("4545");
						drivers.put("firefox", driver);
					}
				}
			}
			{

			}
			break;
		default:
			driver = drivers.get("chrome");
			if (driver != null) {
				ChromeDriver cd1 = (ChromeDriver) driver.getWrappedDriver();
				if (driver == null || cd1.getSessionId() == null) {
					configChromeDriver();
					drivers.put("chrome", driver);
				}
			} else {
				configChromeDriver();
				drivers.put("chrome", driver);
			}
		}
		return driver;
	}

	/*
	 * Chrome Browser Configuration Method
	 */

	private void configChromeDriver() throws MalformedURLException {
		ChromeOptions options = new ChromeOptions();

		if (BaseLib.OS.toLowerCase().contains("Windows".toLowerCase())){

			System.setProperty("webdriver.chrome.driver", exePath + "/Windows-chromedriver.exe");


		}
		if(BaseLib.OS.toLowerCase().contains("Linux".toLowerCase())){
			System.setProperty("webdriver.chrome.driver", exePath + "/Linux-chromedriver");
			//options.addArguments("headless");
			//options.addArguments("window-size=1200x600");
		} if(BaseLib.OS.toLowerCase().contains("Mac".toLowerCase())){
			System.setProperty("webdriver.chrome.driver", exePath + "/Mac-chromedriver");
		}
		if(BaseLib.OS.toLowerCase().contains("Ubuntu".toLowerCase())){
			System.setProperty("webdriver.chrome.driver", exePath + "/Linux-chromedriver");
			//options.addArguments("headless");
			//options.addArguments("window-size=1200x600");
		}

		wDriver = new ChromeDriver(options);
		Reporter.log("Chrome Launched in " + BaseLib.OS, true);
		registerDriver();
	}

	public void configFirefox(String Port) throws MalformedURLException {
		System.out.println("Inside configFirefox"+Port);
        String nodeURL = "http://192.168.1.101:4546/wd/hub";
	
		DesiredCapabilities capabilities = DesiredCapabilities.firefox();
		if (Port.equalsIgnoreCase("4546")) {


			System.out.println("Firefox Browser Initiated");

		}

		wDriver = new RemoteWebDriver(new URL(nodeURL), capabilities);

		Reporter.log("Firefox Launched in " + BaseLib.OS, true);

		registerDriver();

	}
	/*
	public void configChrome(String Port) throws MalformedURLException {
		System.out.println("Inside Configchrome"+Port);
		
		String nodeURL = "http://URL:4547/wd/hub";
		DesiredCapabilities capabilities = DesiredCapabilities.chrome();

		// for headless
		ChromeOptions chromeOptions = new ChromeOptions();
		chromeOptions.addArguments("--headless");
		//chromeOptions.addArguments("--incognito");
		capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);


		if (Port.equalsIgnoreCase("4547")) {
			System.out.println("Chrome Browser Initiated");

		}
		
		wDriver = new RemoteWebDriver(new URL(nodeURL), capabilities);
		
		Reporter.log("Chrome Launched in " + BaseLib.OS, true);
		
		registerDriver();

	}

	 */
	/*
	 * Firefox Browser Configuration Method
	 */
	private void configFirefoxDriver() {
		if (BaseLib.OS.toLowerCase().contains("Windows".toLowerCase())) {
			System.setProperty("webdriver.gecko.driver", exePath + "/Windows-geckodriver.exe");
		} if(BaseLib.OS.toLowerCase().contains("Linux".toLowerCase())){
			System.setProperty("webdriver.gecko.driver", exePath + "/Linux-geckodriver");
		} if(BaseLib.OS.toLowerCase().contains("Mac".toLowerCase())){
			System.setProperty("webdriver.gecko.driver", exePath + "/Mac-geckodriver");
		}
		wDriver = new FirefoxDriver();
		Reporter.log("Firefox Launched in " + BaseLib.OS, true);
		registerDriver();
	}

	/*
	 * Internet Explorer Browser Configuration Method
	 */
	private void configIEDriver() {
		System.setProperty("webdriver.ie.driver", exePath + "/Windows-IEDriverServer.exe");
		wDriver = new InternetExplorerDriver();
		Reporter.log("IE Launched in " + BaseLib.OS, true);
		registerDriver();
	}

	/*
	 * Safari Browser Configuration Method
	 */
	private void configSafariDriver() {
		try {
			wDriver = new SafariDriver();
		} catch (Exception e) {
			e.printStackTrace();
			Reporter.log(e.getMessage(), true);
		}
		Reporter.log("Safari Launched in " + BaseLib.OS, true);
		registerDriver();
	}

	/*
	 * Method to register Browser
	 */
	private void registerDriver() {
		listeners = new MyProjectListener();
		driver = new EventFiringWebDriver(wDriver);

		driver.register(listeners);
	}

	/*
	 * Method to close All browsers
	 */
	public void closeAllDriver() {
		for (String key : drivers.keySet()) {
			drivers.get(key).close();
			drivers.get(key).quit();
		}
	}
}
