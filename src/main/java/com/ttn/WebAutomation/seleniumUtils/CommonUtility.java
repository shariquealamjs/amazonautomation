package com.ttn.WebAutomation.seleniumUtils;

/**
 * This Java program demonstrates the Common Utility of PRISM-Framework.
 *
 * @author TTN
 */

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.TestException;


public class CommonUtility {
	private String winHandle;
	protected static WebDriver driver;
	private int timeoutPageLoad;
	public Actions actions;
	public Select select;
	public final static Logger log = Logger.getLogger(CommonUtility.class);
	private Random rnd;
	private static final String LOWER_CASE = "abcdefghijklmnopqrstuvwxyz";
	private static final String UPPER_CASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String SPECIAL_CHAR = "!@#$%^&*";
	private static final String INT_CHAR = "0123456789";

	public CommonUtility(WebDriver driver) {
		CommonUtility.driver = driver;
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		actions = new Actions(driver);
	}

	//Handling locator type
	private static By byLocator(final String locator) {
		By result = null;

		if (locator.startsWith("//")) {
			result = By.xpath(locator);
		} else if (locator.startsWith("css=")) {
			result = By.cssSelector(locator.replace("css=", ""));
		} else if (locator.startsWith("#")) {
			result = By.name(locator.replace("#", ""));
		} else if (locator.startsWith("Link=")) {
			result = By.linkText(locator.replace("Link=", ""));
		} else if (locator.startsWith("xpath=")) {
			result = By.xpath(locator);
		} else if (locator.startsWith("(//")) {
			result = By.xpath(locator);
		} else {
			result = By.id(locator);
		}
		return result;
	}
	public static String[] getFormatedOR(WebElement element, Object... params) {

		System.out.println("the value is>>"+element.toString()+"<<<");
		String locatorType = element.toString().split("By.")[1].split(":")[0].trim();
		String locatorValue = element.toString().split("By.")[1].split(":")[1].trim();
		locatorValue = locatorValue.substring(0, locatorValue.length()-1);
		System.out.println("locatorType>>"+locatorType+"<<");
		System.out.println("locatorValue>>"+locatorValue+"<<");

		String formatedORPropertyValue = MessageFormat.format(locatorType +"~" +locatorValue, params);

		return  formatedORPropertyValue.split("~");

	}

	// Command for navigating to a particular URL
	public void navigateToURL(String URL) {
		System.out.println("Navigating to: " + URL);
		System.out.println("Thread id = " + Thread.currentThread().getId());

		try {
			driver.navigate().to(URL);
		} catch (Exception e) {
			System.out.println("URL did not load: " + URL);
			throw new TestException("URL did not load"); //Custom Exception can also be included
		}
	}

	//Command for getting the title of the current page
	public String getPageTitle() {
		try {
			System.out.print(String.format("The title of the page is: %s\n\n", driver.getTitle()));
			return driver.getTitle();
		} catch (Exception e) {
			throw new TestException(String.format("Current page title is: %s", driver.getTitle()));
		}
	}

	// Handle mouse double click action
	public void doubleClick(final String locator) {
		waitForElementPresent(locator, 50);
		final WebElement element = driver.findElement(CommonUtility.byLocator(locator));

		// build and perform the mouse click with Advanced User Interactions API
		actions.doubleClick(element).perform();
	}

	// Handle mouse double click action
	public void click(String locator) {
		waitForElementPresent(locator, 50);
		driver.findElement(byLocator(locator)).click();
	}

	// Wait for element present
	public static void waitForElementPresent(final String locator, final int timeout) {
		for (int i = 0; i < timeout; i++) {
			if (isElementPresent(locator)) {
				break;
			}

			try {
				Thread.sleep(1000);
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	// Assert element present
	public static Boolean isElementPresent(final String locator) {
		boolean result = false;
		try {
			driver.findElement(byLocator(locator));
			result = true;
		} catch (final Exception ex) {
		}
		return result;
	}

	public String getCurrentDate() throws ParseException {
		return getCurrentDate("MM/dd/yyyy");
	}
	// Get Current Date`
	public String getCurrentDate(String dateFormate) throws ParseException {
		final DateFormat format = new SimpleDateFormat(dateFormate);
		Date date = new Date();
		final String currentDate = format.format(date);
		System.out.println("Current Date:" + currentDate);
		return currentDate;
	}

	/**
	 * This method will return Random password with alpha numeric based on the size
	 *
	 * @param size of the password
	 * @return string as password
	 */
	public String getRandomPassword(int size) {
		log.info("Return alpha numeric password");
		return getRandomAlphaNumeric(size);
	}

	/**
	 * This method will return Random alpha numeric string depending on the provided size of the random number.
	 *
	 * @param size Size of the character length required
	 * @return Alpha numerical characters of the provided size
	 */
	public String getRandomAlphaNumeric(int size) {
		log.info("Creating object of Random Class");
		StringBuilder sb = new StringBuilder(size);
		log.info("Creating a string builder with size of Required new string");
		for (int i = 0; i < size; i = i + 1) {
			if (i % 2 == 0)
				sb.append(LOWER_CASE.charAt(rnd.nextInt(LOWER_CASE.length())));
			else
				sb.append(INT_CHAR.charAt(rnd.nextInt(INT_CHAR.length())));
		}
		log.info("Returning random generated string" + sb.toString());
		return sb.toString();
	}

	/**
	 * This method will return Random numbers depending on the provided size of the random number.
	 *
	 * @param size Size of the character length required
	 * @return random number of size provided
	 */
	public String getRandomNumber(int size) {
		log.info("Creating object of Random Class");
		StringBuilder sb = new StringBuilder(size);
		log.info("Creating a string builder with size of Required new string");
		for (int i = 0; i < size; i++)
			sb.append(INT_CHAR.charAt(rnd.nextInt(INT_CHAR.length())));
		log.info("Returning random generated string" + sb.toString());
		return sb.toString();

	}


	/**
	 * This method will return Random string depending on the provided size of the random string.
	 *
	 * @param size Size of the character length required
	 * @return random string of size provided
	 */
	public String getRandomString(int size) {

		log.info("Creating object of Random Class");
		StringBuilder sb = new StringBuilder(size);
		log.info("Creating a string builder with size of Required new string");
		for (int i = 0; i < size; i++)
			sb.append(LOWER_CASE.charAt(rnd.nextInt(LOWER_CASE.length())));
		log.info("Returning random generated string" + sb.toString());
		return sb.toString();

	}

	/**
	 * This method will return String in caps depending on the provided size of the random number.
	 *
	 * @param size Size of the character length required
	 * @return random String all in capital letters of size provided
	 */
	public String getRandomStringAllCaps(int size) {

		log.info("Creating object of Random Class");
		StringBuilder sb = new StringBuilder(size);
		log.info("Creating a string builder with size of Required new string");
		for (int i = 0; i < size; i++)
			sb.append(UPPER_CASE.charAt(rnd.nextInt(UPPER_CASE.length())));
		log.info("Returning random generated string" + sb.toString());
		return sb.toString();

	}

	/**
	 * This method will return AlphaNumeric Values where characters will be in Capital
	 *
	 * @param size
	 * @return Alphanumeric word
	 */
	public String getRandomAlphaNumericWithCaps(int size) {
		log.info("Creating object of Random Class");
		StringBuilder sb = new StringBuilder(size);
		log.info("Creating a string builder with size of Required new string");
		for (int i = 0; i < size; i = i + 1) {
			if (i % 2 == 0)
				sb.append(UPPER_CASE.charAt(rnd.nextInt(UPPER_CASE.length())));
			else
				sb.append(INT_CHAR.charAt(rnd.nextInt(INT_CHAR.length())));
		}
		log.info("Returning random generated string" + sb.toString());
		return sb.toString();
	}

	/**
	 * This method will return AlphaNumeric  with special characters
	 *
	 * @param size
	 * @return Random Alpha Numeric With Special Character
	 */
	public String getRandomAlphaNumericWithSpecialCharacter(int size) {
		log.info("Creating object of Random Class");
		StringBuilder sb = new StringBuilder(size);
		log.info("Creating a string builder with size of Required new string");
		for (int i = 0; i < size; i = i + 1) {
			char choice = INT_CHAR.charAt(rnd.nextInt(3));
			System.out.println(choice);
			switch (choice) {
			case '0':
				sb.append(UPPER_CASE.charAt(rnd.nextInt(UPPER_CASE.length())));
				break;
			case '1':
				sb.append(INT_CHAR.charAt(rnd.nextInt(INT_CHAR.length())));
				break;
			case '2':
				sb.append(SPECIAL_CHAR.charAt(rnd.nextInt(SPECIAL_CHAR.length())));
				break;
			}
		}
		log.info("Returning random generated string" + sb.toString());
		return sb.toString();
	}

	/**
	 * This method will return AlphaNumeric  with special characters with defined size of string, numbers and special
	 * characters
	 *
	 * @param sizeString
	 * @param sizeNumber
	 * @param sizeSpecialChar
	 * @return AlphaNumeric  with special characters
	 */
	public String getRandomAlphaNumericWithSpecialCharacter(int sizeString, int sizeNumber, int sizeSpecialChar) {

		int size = sizeNumber + sizeSpecialChar + sizeString;
		int stringCounter = 0;
		int numberCounter = 0;
		int specialCharCounter = 0;
		StringBuilder sb = new StringBuilder(size);


		for (int i = 0; i < size; i = i + 1) {
			char choice = INT_CHAR.charAt(rnd.nextInt(3));
			System.out.println(choice);
			switch (choice) {
			case '0':
				if (stringCounter != sizeString) {
					sb.append(UPPER_CASE.charAt(rnd.nextInt(UPPER_CASE.length())));
					stringCounter++;
					break;
				}
			case '1':
				if (numberCounter != sizeNumber) {
					sb.append(INT_CHAR.charAt(rnd.nextInt(INT_CHAR.length())));
					numberCounter++;
					break;
				}

			case '2':
				if (specialCharCounter != sizeSpecialChar) {
					sb.append(SPECIAL_CHAR.charAt(rnd.nextInt(SPECIAL_CHAR.length())));
					specialCharCounter++;
					break;
				}
			default:
				if (stringCounter != sizeString) {
					sb.append(UPPER_CASE.charAt(rnd.nextInt(UPPER_CASE.length())));
					stringCounter++;
					break;
				}
				if (numberCounter != sizeNumber) {
					sb.append(INT_CHAR.charAt(rnd.nextInt(INT_CHAR.length())));
					numberCounter++;
					break;
				}
				if (specialCharCounter != sizeSpecialChar) {
					sb.append(SPECIAL_CHAR.charAt(rnd.nextInt(SPECIAL_CHAR.length())));
					specialCharCounter++;
					break;
				}
			}
		}
		log.info("Returning random generated string" + sb.toString());
		return sb.toString();
	}

	/**
	 * Waits for Page Load via Java Script Ready State
	 *
	 * @param iTimeOut time
	 */
	public void waitForPageLoad(int iTimeOut) {

		try {
			Thread.sleep(2000);
			log.info("Waiting For Page load via JS");
			ExpectedCondition<Boolean> pageLoadCondition = new ExpectedCondition<Boolean>() {
				public Boolean apply(WebDriver driver) {
					return ((JavascriptExecutor) driver).executeScript(
							"return document.readyState").equals("complete");
				}
			};
			WebDriverWait wait = new WebDriverWait(driver, iTimeOut);
			wait.until(pageLoadCondition);

		} catch (Exception e) {
			log.error("Error Occured waiting for Page Load "
					+ driver.getCurrentUrl());
		}
	}

	// Wait for element visible
	public static void waitForElementVisible(final String locator,
			final int timeout) {
		for (int i = 0; i < timeout; i++) {
			if (isElementPresent(locator)) {
				if (driver.findElement(byLocator(locator)).isDisplayed()) {
					break;
				}
			}

			try {
				Thread.sleep(1000);
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Waits and Switches to the Frame
	 *
	 * @param locator          locator
	 * @param timeOutInSeconds time
	 */
	public void waitAndSwitchToFrame(By locator, int timeOutInSeconds) {

		try {
			waitForPageLoad(timeoutPageLoad);
			log.info("Switching to Frame" + locator.toString());
			WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds);
			wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(locator));

		} catch (Exception e) {
			log.error("Exception found in waitAndSwitchToFrame: "
					+ e.getMessage());
		}
	}

	/**
	 * Method Description: Closes the Current Active Window
	 */
	public void closeNewWindow() {
		try {
			driver.close();
			log.info("Current Active window has been closed");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Current Active Window could not be closed. " + e.getMessage());
		}
	}

	/**
	 * Method Description: Tab Switching - Function to switch to another tab.
	 */
	public void tabSwitch() {
		try {
			Robot rob = new Robot();
			rob.keyPress(KeyEvent.VK_CONTROL);
			rob.keyPress(KeyEvent.VK_TAB);
			rob.keyRelease(KeyEvent.VK_CONTROL);
			rob.keyRelease(KeyEvent.VK_TAB);
			// Focus on the tab
			driver.switchTo().defaultContent();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void switchToParentWindow() {
		try {
			driver.switchTo().defaultContent();
			log.info("Switched to Parent window");
		} catch (NoSuchWindowException ns) {
			ns.printStackTrace();
			log.error("No window exist. " + ns.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error occurred while switching to parent window. "
					+ e.getMessage());
		}
	}

	/**
	 * Method Description: Gets the handle for the current window
	 */
	private void getWindowHandle() {
		try {
			winHandle = driver.getWindowHandle();
			log.info("Got the handle for the current window");
		} catch (NoSuchWindowException ns) {
			ns.printStackTrace();
			log.error("No window exist. " + ns.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error occured while getting window handle. " + e.getMessage());
		}
	}

	/**
	 * Method Description: Switches to the most recent window opened
	 */
	public void switchtoNewWindow() {
		try {
			getWindowHandle();
			for (String windowsHandle : driver.getWindowHandles()) {
				driver.switchTo().window(windowsHandle);
				log.info("Switched to window: " + windowsHandle);
			}
		} catch (NoSuchWindowException ns) {
			ns.printStackTrace();
			log.error("No window exist. " + ns.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error occuring while switching to most recent new window. "
					+ e.getMessage());
		}
	}

	/**
	 * Method Description: Switches back to original window
	 */
	public void switchtoOriginalWindow() {
		try {
			driver.switchTo().window(winHandle);
			log.info("Switched back to original window");
		} catch (NoSuchWindowException ns) {
			ns.printStackTrace();
			log.error("No window exist. " + ns.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error occuring while switching to most recent new window. "
					+ e.getMessage());
		}
	}

	public void setWindowSize(int Dimension1, int dimension2) {
		driver.manage().window().setSize(new Dimension(Dimension1, dimension2));
	}

	/**
	 * Method Description: MoveSlider - Function to move slider from one position to other.
	 *
	 * @param eleToSlide locator
	 * @param xAxis      x
	 * @param yAxis      y
	 */
	public void moveSlider(WebElement eleToSlide, int xAxis, int yAxis) {
		Actions act = new Actions(driver);
		act.dragAndDropBy(eleToSlide, xAxis, yAxis).build().perform();
	}


	/**
	 * Method Description: It returns text present on Alert box
	 */
	public String getAlertMessage() {
		String message = "";
		try {
			Alert alert = driver.switchTo().alert();
			message = alert.getText();
			log.info("Fetched message present on alert box: " + message);
			alert.accept();
			log.info("Pressed on OK/Yes button present to close the alert box.");
			return message;
		} catch (NoAlertPresentException na) {
			na.printStackTrace();
			log.error("No alert present. " + na.getMessage());
			return message;
		} catch (Exception e) {
			log.error("Error occured while fetching message present on alert box. "
					+ e.getMessage());
			return message;
		}
	}


	/**
	 * Method Description: Clicks on OK present on the alert
	 */
	public void acceptAlertBox() {

		try {
			Alert alert = driver.switchTo().alert();
			alert.accept();
			log.info("Pressed on OK/Yes button present on the alert box.");
		} catch (NoAlertPresentException na) {
			log.error("No alert present. " + na.getMessage());
		} catch (Exception e) {
			log.error("Error occured while accepting alert box. " + e.getMessage());
		}
	}

	/**
	 * Method Description: Clicks on Cancel/No present on the alert
	 */
	public void dismissAlertBox() {
		try {
			Alert alert = driver.switchTo().alert();
			alert.accept();
			log.info("Pressed on Cancel/No button present on the alert box.");
		} catch (NoAlertPresentException na) {
			na.printStackTrace();
			log.error("No alert present. " + na.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error occured while dismissing alert box. " + e.getMessage());
		}
	}

	public void uploadFile(String filepath) {
		StringSelection ss = new StringSelection(filepath);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);
		Robot robot = null;
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_ENTER);
		robot.keyRelease(KeyEvent.VK_ENTER);
		goToSleep(2000);
	}

	/**
	 * Method Description: It applies a hard wait
	 *
	 * @param TimeInMillis time
	 */
	public void goToSleep(int TimeInMillis) {
		try {
			Thread.sleep(TimeInMillis);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
	}

	public String setImplicitWaitInMilliSeconds(int timeOut) {
		driver.manage().timeouts().implicitlyWait(timeOut, TimeUnit.MILLISECONDS);
		return "Timeout set to " + timeOut + " milli seconds.";
	}

	public String getAttribute(By locator, String attribute) {
		try {
			return driver.findElement(locator).getAttribute(attribute);
		} catch (ElementNotVisibleException env) {
			this.goToSleep(1000);
			String text = driver.findElement(locator).getAttribute(attribute);
			env.printStackTrace();
			log.error("Element is present in DOM but not visible on page. " + env.getMessage());
			return text;
		} catch (NoSuchElementException ne) {
			ne.printStackTrace();
			log.error("Element could not be located on page. " + ne.getMessage());
			return "Element could not be located on page. " + ne.getMessage();
		} catch (StaleElementReferenceException se) {
			se.printStackTrace();
			log.error("Either element has been deleted entirely or no longer attached to DOM. "
					+ se.getMessage());
			return "Either element has been deleted entirely or no longer attached to DOM. "
			+ se.getMessage();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error in returning the desired attribute of webelement! " + e.getMessage());
			return "Error in returning the desired attribute of webelement! " + e.getMessage();
		}
	}

	// Store text from a locator
	public String getText(final String locator, final String element) {
		waitForElementPresent(locator, 60);
		Assert.assertTrue(isElementPresent(locator), "Element Locator :"
				+ locator + " Not found for element" + element);
		return driver.findElement(byLocator(locator)).getText();
	}

	public List<String> getListText(By locator) {
		List<String> listTexts = new ArrayList<>();
		List<WebElement> list = driver.findElements(locator);
		for (WebElement ele : list) {
			listTexts.add(ele.getText());
		}
		return listTexts;
	}

	public void scrollWindow(final int x, final int y) {
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		jse.executeScript("scroll(" + x + "," + y + ")");
	}

	public void closeCurrentTab() {
		Set<String> windowsSet = driver.getWindowHandles();
		List<String> windowsList = new ArrayList<>(windowsSet);
		driver.switchTo().window(windowsList.get(1));
		driver.close();
		driver.switchTo().window(windowsList.get(0));
	}

	public void openNewTab() {
		Actions act = new Actions(driver);
		act.keyDown(Keys.CONTROL).sendKeys("t").keyUp(Keys.CONTROL).build().perform();
	}

	/**
	 * wait till text present on element
	 *
	 * @param element           element
	 * @param maxSecondTimeout  time
	 * @param isFailOnExcaption ( optional parameter true if fail on exception)
	 */
	public boolean waitForTextToBePresentOnElement(
			WebElement element, int maxSecondTimeout, String matchText,
			boolean... isFailOnExcaption) {
		try {
			log.info("INTO METHOD waitForTextToBePresentOnElement");
			maxSecondTimeout = maxSecondTimeout * 20;
			while ((!element.isDisplayed() && (maxSecondTimeout > 0) && (element
					.getText().toLowerCase().equalsIgnoreCase(matchText
							.toLowerCase().trim())))) {
				log.info("Loading...CountDown=" + maxSecondTimeout);
				Thread.sleep(50L);
				maxSecondTimeout--;
			}
			if ((maxSecondTimeout == 0) && (isFailOnExcaption.length != 0)) {
				if (isFailOnExcaption[0]) {
					log.error("Element is not display within "
							+ (maxSecondTimeout / 20) + "Sec.");
				}
			}
			log.info("OUT OF METHOD waitForTextToBePresentOnElement");
			return true;

		} catch (Exception e) {
			log.info("Exception found in waitForTextToBePresentOnElement: "
					+ e.getMessage());
			return false;
		}
	}

	/**
	 * Waits Until the Attribute of Element got Changed.
	 *
	 * @param webElement       element
	 * @param attribute        attribute
	 * @param value            value
	 * @param maxSecondTimeout time
	 */
	public void waitTillElementAttributeChange(
			WebElement webElement, String attribute, String value, int maxSecondTimeout) {
		try {
			log.info("INTO METHOD waitTillElementAttributeChange");
			maxSecondTimeout = maxSecondTimeout * 20;
			while (webElement.getAttribute(attribute) != null) {
				if ((!webElement.getAttribute(attribute.trim()).toLowerCase()
						.contains(value.trim().toLowerCase()))
						&& (maxSecondTimeout > 0)) {
					log.info("Loading...CountDown=" + maxSecondTimeout);
					Thread.sleep(50L);
					maxSecondTimeout--;
				}
			}
			log.info("OUT OF METHOD waitTillElementAttributeChange");
		} catch (Exception e) {
			log.error("SOME ERROR CAME IN METHOD->waitTillElementAttributeChange->"
					+ e.getMessage());
		}
	}

	/**
	 * Wait For Element to Enable
	 *
	 * @param element          element
	 * @param maxSecondTimeout time
	 */
	public boolean waitForElementToEnable(WebElement element, int maxSecondTimeout) {

		try {
			log.info("INTO waitForElementToEnable METHOD");
			maxSecondTimeout = maxSecondTimeout * 20;
			while (!element.isEnabled() && maxSecondTimeout > 0) {
				Thread.sleep(50L);
				maxSecondTimeout--;
			}
			if (maxSecondTimeout == 0) {
				log.error("Element is not enabled within "
						+ (maxSecondTimeout / 20) + "Sec.");
			}
			log.info("OUT OF METHOD waitForElementToEnable");
			return true;

		} catch (Exception e) {
			log.info("Exception found in waitForElementToEnable: "
					+ e.getMessage());
			return false;
		}
	}

	public void waitForElementToDisappear(By locator) {
		try {
			log.info("Waiting for an element to be invisible using String locator "
					+ locator.toString());
			WebDriverWait wait = new WebDriverWait(driver, 120);
			wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));

		} catch (Exception e) {
			log.info("Exception while waiting for invisibility: "
					+ e.getMessage());
		}
	}

	/**
	 * Wait For Element to Disable
	 *
	 * @param element          element
	 * @param maxSecondTimeout time
	 */
	public boolean waitForElementToDisable(WebElement element, int maxSecondTimeout) {

		try {
			log.info("INTO waitForElementToDisable METHOD");
			maxSecondTimeout = maxSecondTimeout * 20;
			while (element.isEnabled() && (maxSecondTimeout > 0)) {
				Thread.sleep(50L);
				maxSecondTimeout--;
			}
			if (maxSecondTimeout == 0) {
				log.error("Element is not disabled within "
						+ (maxSecondTimeout / 20) + "Sec.");
			}
			log.info("OUT OF METHOD waitForElementToDisable");
			return true;

		} catch (Exception e) {
			log.info("Exception found in waitForElementToDisable: "
					+ e.getMessage());
			return false;
		}
	}

	/**
	 * waits for specified duration and checks that an element is present on DOM. Visibility means that the element is
	 * not only displayed but also has a height and width that is greater than 0.
	 *
	 * @param locator locator
	 **/
	public void waitForElementToBeVisible(By locator) {

		try {
			log.info("Waiting for element to be visible using String locator: "
					+ locator.toString());
			WebDriverWait wait = new WebDriverWait(driver, 120);
			wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
			Thread.sleep(1000);
		} catch (Exception e) {
			log.info("Exception while waiting for visibility. "
					+ e.getMessage());
		}
	}

	/**
	 * Wait for html element to be hidden
	 *
	 * @param element           element
	 * @param maxSecondTimeout  time
	 * @param isFailOnExcaption ( optional parameter true if fail on exception)
	 */
	public boolean waitForElementToBeHidden(
			WebElement element, int maxSecondTimeout, boolean... isFailOnExcaption) {

		try {
			log.info("INTO waitForElementToBeHidden METHOD");
			maxSecondTimeout = maxSecondTimeout * 20;
			while (element.isDisplayed() && maxSecondTimeout > 0) {
				Thread.sleep(50L);
				maxSecondTimeout--;
			}
			if (maxSecondTimeout == 0 && isFailOnExcaption.length != 0) {
				if (isFailOnExcaption[0]) {
					log.error("Element is not hidden within "
							+ (maxSecondTimeout / 20) + "Sec.");
				}
			}
			log.info("OUT OF METHOD waitForElementToBeHidden");
			return true;

		} catch (Exception e) {
			log.error("Exception in waitForElementToBeHidden: "
					+ e.getMessage());
			return false;
		}
	}

	public boolean waitForElementToBeDisplay(
			WebElement element, int maxSecondTimeout, boolean... isFailOnExcaption) {

		try {
			log.info("INTO METHOD waitForElementToBeDisplay");
			maxSecondTimeout = maxSecondTimeout * 20;
			while (!element.isDisplayed() && maxSecondTimeout > 0) {
				Thread.sleep(50L);
				maxSecondTimeout--;
			}
			if (maxSecondTimeout == 0 && isFailOnExcaption.length != 0) {
				if (isFailOnExcaption[0]) {
					log.error("Element is not display within "
							+ (maxSecondTimeout / 20) + "Sec.");
				}
			}
			log.info("OUT OF METHOD waitForElementToBeDisplay");
			return true;

		} catch (Exception e) {
			log.error("Exception in waitForElementToBeDisplay: "
					+ e.getMessage());
			return false;
		}
	}

	/**
	 * @param key key
	 */
	public void pressKey(Keys key) {
		try {
			Actions action = new Actions(driver);
			action.keyDown(key);
			log.info("Key is pressed successfully");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Could not press key.");
		}
	}

	/**
	 * @param key key
	 */
	public void releaseKey(Keys key) {
		try {
			Actions action = new Actions(driver);
			action.keyUp(key);
			log.info("Key is pressed successfully");

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Could not release key");
		}
	}

	/**
	 * Method Description: It checks the presence and visibility of an element on page of given path
	 *
	 * @param locator locator
	 */
	public Boolean isVisible(By locator) {
		try {
			if (driver.findElements(locator).size() != 0
					&& driver.findElement(locator).isDisplayed()) {
				driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
				log.info("Locator: " + locator.toString() + " is visible on page");
				return true;
			} else {
				log.info("Locator: " + locator.toString() + " is not visible on page");
				return false;
			}
		} catch (NoSuchElementException ne) {
			ne.printStackTrace();
			log.error("Element could not be located on page. " + ne.getMessage());
			return false;
		} catch (StaleElementReferenceException se) {
			se.printStackTrace();
			log.error("Either element has been deleted entirely or no longer attached to DOM. "
					+ se.getMessage());
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Element is not visible on page. " + e.getMessage());
			return false;
		}
	}

	/**
	 * Method Description: It checks the presence of an element on the page of given path
	 *
	 * @param locator locator
	 */
	public Boolean isPresent(By locator) {
		try {
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			if (driver.findElements(locator).size() != 0) {
				log.info("Locator: " + locator.toString() + " is present on page");
				return true;
			} else {
				log.info("Locator: " + locator.toString() + " not present on page");
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error occurred while checking the presence of an element on page"
					+ e.getMessage());
			return false;
		}
	}

	public boolean isChecked(By locator) {
		WebElement element = driver.findElement(locator);
		return element.isSelected();
	}

	public boolean isSelected(By locator) {
		WebElement element = driver.findElement(locator);
		return element.isSelected();
	}

	public void selectCheckbox(By locator) {
		try {
			if (!driver.findElement(locator).isSelected()) {
				driver.findElement(locator).click();
				log.info("Checkbox is selected successfully");
			} else {
				log.info("Checkbox is already selected");
			}
		} catch (ElementNotVisibleException env) {
			this.goToSleep(1000);
			if (!driver.findElement(locator).isSelected())
				driver.findElement(locator).click();
			env.printStackTrace();
			log.error("Element is present in DOM but not visible on page. "
					+ env.getMessage());
		} catch (NoSuchElementException ne) {
			ne.printStackTrace();
			log.error("Element could not be located on page. " + ne.getMessage());
		} catch (StaleElementReferenceException se) {
			se.printStackTrace();
			log.error("Either element has been deleted entirely or no longer attached to DOM. "
					+ se.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error in selecting checkbox! " + e.getMessage());
		}
	}

	public void sendKeysChord(String keyChord) {
		actions.sendKeys(keyChord).build().perform();
	}

	public void clearField(WebElement element) {
		try {
			element.clear();
		} catch (Exception e) {
			System.out.print(String.format("The following element could not be cleared: [%s]", element.getText()));
		}
	}

	/**
	 * @param locator locator
	 */
	public void clickAndHold(By locator) {
		try {
			actions.clickAndHold(driver.findElement(locator));
			log.info("Clicked and held on locator: " + locator);

		} catch (ElementNotVisibleException env) {
			goToSleep(2000);
			actions.clickAndHold(driver.findElement(locator));
			env.printStackTrace();
			log.error("Element is present in DOM but not visible on page. " + env.getMessage());

		} catch (NoSuchElementException ne) {
			ne.printStackTrace();
			log.error("Element could not be located on page. " + ne.getMessage());

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Couldn't click and hold locator");
		}
	}

	/**
	 * Method Description: Double clicks at the last known mouse coordinates
	 *
	 * @param locator locator
	 */
	public void doubleClickTheLocator(By locator) {

		try {
			actions.doubleClick(driver.findElement(locator));
			log.info("Performed double click on locator: " + locator);

		} catch (ElementNotVisibleException env) {
			goToSleep(2000);
			actions.doubleClick(driver.findElement(locator));
			env.printStackTrace();
			log.error("Element is present in DOM but not visible on page. " + env.getMessage());

		} catch (NoSuchElementException ne) {
			ne.printStackTrace();
			log.error("Element could not be located on page. " + ne.getMessage());

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Could not double click the locator.");
		}
	}

	/**
	 * @param locator Method description: It performs mouse hover
	 */
	public void performMouseHover(By locator) {
		try {
			actions.moveToElement(driver.findElement(locator)).build().perform();
		} catch (NoSuchElementException ne) {
			ne.printStackTrace();
			log.error("Element could not be located on page. " + ne.getMessage());
		} catch (ElementNotVisibleException env) {
			this.goToSleep(2000);
			actions.moveToElement(driver.findElement(locator)).build().perform();
			env.printStackTrace();
			log.error("Element is present in DOM but not visible on page. " + env.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Could not perform mouse hover. " + e.getMessage());
		}
	}

	/**
	 * @param locator            locator
	 * @param elementTobeClicked Method description: It performs mouse hover and clicks on the element
	 */
	public void performMouseHoverAndClick(By locator, String elementTobeClicked) {

		try {
			actions.moveToElement(driver.findElement(locator))
			.build().perform();
			log.info("Mouse hover performed on locator: " + locator);
			driver.findElement(locator).click();
			log.info("Element clicked successfully");

		} catch (ElementNotVisibleException env) {
			goToSleep(2000);
			actions.moveToElement(driver.findElement(locator))
			.build().perform();
			driver.findElement(locator).click();
			env.printStackTrace();
			log.error("Element is present in DOM but not visible on page. " + env.getMessage());

		} catch (NoSuchElementException ne) {
			ne.printStackTrace();
			log.error("Element could not be located on page. " + ne.getMessage());

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Could not perform mouse hover and click operation.");
		}
	}

	/**
	 * @param actual              actual String value
	 * @param expected            expected String value
    // * @param description         description for extent report
	 */
	public void verifyEquals(String actual, String expected) {
		Assert.assertEquals(actual, expected);
		// log.info(description + ": Assertion is successful");
		//System.out.println(description + ": Success");

	}

	/**
	 *@param locator            locator
	 */
	public void selectTextByValueFromDropdown(WebElement locator, String value) {
		Select dropdown = new Select(locator);
		System.out.println("Element Found");
		dropdown.selectByValue(value);
	}

	public void selectTextByIndexFromDropdown(WebElement locator, int index) {
		Select dropdown = new Select(locator);
		System.out.println("Element Found");
		dropdown.selectByIndex(index);
	}

}
