package com.ttn.WebAutomation.seleniumUtils;

/**
 
 * @author TTN
 */

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
//import java.util.logging.Logger;

import com.ttn.WebAutomation.utillib.AXE;
import org.json.JSONException;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotSelectableException;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.InvalidArgumentException;
import org.openqa.selenium.JavascriptException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.json.JsonException;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;
import com.aventstack.extentreports.Status;
import org.slf4j.Logger;

public class SeleniumHelper {

	private WebDriver driver;
	protected static Logger logger = LoggerFactory.getLogger(SeleniumHelper.class);
	private static final URL scriptUrl = SeleniumHelper.class.getResource("/axe.min.js");
	JSONArray violations;
	JSONObject responseJSON;
	/**
	 * default constructor
	 */
	public SeleniumHelper(WebDriver driver) {
		this.driver = driver;
	}

	/**
	 * default constructor
	 */
	public SeleniumHelper() {
		this.driver = driver;
	}


	public boolean getAXEAnalysisOfTheGivenPage(String pageName) throws InterruptedException, JSONException {
		logger.info("=====================================================");
		logger.info("====================Next page========================");
		logger.info("=====================================================");
		logger.info("in the getAXEAnalysisOfTheGivenPage method Analyzing of the page " + pageName);

		responseJSON = new AXE.Builder(driver, scriptUrl).analyze();

		violations = responseJSON.getJSONArray("violations");
		logger.info("=====================================================+After violation");
		for(int i=0; i<violations.length(); i++)
		{
			JSONArray arr = violations.getJSONObject(i).names();
			logger.info("=====================================================+After violation1");
			for(int j=0; j<arr.length(); j++) {
				if(arr.get(j).equals("help")) {
					logger.info(pageName + " : violation error message: " + violations.getJSONObject(i).get(arr.get(j).toString()));
					logger.info(pageName + " : violations impact level: " + violations.getJSONObject(i).get("impact"));
				}
				else if(arr.get(j).equals("nodes")) {
					logger.info(pageName + " : violation target element: " + violations.getJSONObject(i).getJSONArray("nodes").getJSONObject(0).getJSONArray("target"));

					if(violations.getJSONObject(i).getJSONArray("nodes").getJSONObject(0).getJSONArray("any").length()!=0)
					{
						for(int i1=0; i1<violations.getJSONObject(i).getJSONArray("nodes").getJSONObject(0).getJSONArray("any").length();i1++)
						{
							logger.info(pageName + " : any block error type: \"" + violations.getJSONObject(i).getJSONArray("nodes").getJSONObject(0).getJSONArray("any").getJSONObject(i1).getString("id") + "\"------any block error message: \"" + violations.getJSONObject(i).getJSONArray("nodes").getJSONObject(0).getJSONArray("any").getJSONObject(i1).getString("message") + "\"");
						}
					}
					else if(violations.getJSONObject(i).getJSONArray("nodes").getJSONObject(0).getJSONArray("all").length()!=0)
					{
						for(int m1=0; m1<violations.getJSONObject(i).getJSONArray("nodes").getJSONObject(0).getJSONArray("all").length();m1++)
						{
							logger.info(pageName + " : all block error type: \"" + violations.getJSONObject(i).getJSONArray("nodes").getJSONObject(0).getJSONArray("all").getJSONObject(m1).getString("id") + "\"------all block error message: \"" + violations.getJSONObject(i).getJSONArray("nodes").getJSONObject(0).getJSONArray("all").getJSONObject(m1).getString("message") + "\"");
						}
					}

					logger.info("----------");
				}
			}
		}

		if (violations.length() == 0) {
			logger.info(pageName + " : No violations found");
			return true;
		} else {
			AXE.writeResults(System.getProperty("user.dir") + "\\target\\"+ pageName, responseJSON);

			Assert.assertTrue(false, AXE.report(violations));
			return false;
		}
	}

	/**
	 * clicks on specified field (button,
	 * link, menu etc.), identified by
	 * given locator with follow-up
	 * validation
	 * @param @validationType e.g. verifyTitle/locatorType
	 * @param @expectedValue
	 * @param fieldName
	 */

	public void click(String fieldName, String locatorType, String locatorValue) {
		try {
			WebElement element = getElement(getBy(locatorType, locatorValue));
			element.click();
			ThreadManager.getDetailReport().addStep("click", "Clicked on "
					+ fieldName, Status.PASS);

		} catch (Throwable t) {
			ThreadManager.getDetailReport().addStep("click", "Error while clicking on "
					+ fieldName + "<BR>" + t, Status.FAIL);
			ThreadManager.getSoftAssert().fail(t.getClass().getName() + " -> Error while clicking on "
					+ fieldName + "\n" + t.getMessage());
		}
	}
	/**
	 * clicks on specified field (button,
	 * link, menu etc.), identified by
	 * given locator with follow-up
	 * validation
	 * @param fieldName
	 * @param @webElement element
	 */
	public void click(String fieldName, WebElement element) {
		try {
			getElement(element);
			element.click();
			logger.info("Clicked on " + fieldName);

			ThreadManager.getDetailReport().addStep("click", "Clicked on "
					+ fieldName, Status.PASS);
		} catch (Throwable t) {
			logger.info(t.getClass().getName() + " -> Error while clicking on " + fieldName + "\n" + t);
			ThreadManager.getDetailReport().addStep("click", "Error while clicking on "
					+ fieldName + "<BR>" + t, Status.FAIL );
			ThreadManager.getSoftAssert()
			.fail(t.getClass().getName() + " -> Error while clicking on " + fieldName + "\n" + t.getMessage());
		}
	}

	/**
	 * dragAndDrop on elements using action API
	 */
	public void dragAndDrop(String fieldName, WebElement from, WebElement to) {
		try {
			Actions action = new Actions(driver);
			getElement(from);
			getElement(to);
			action.dragAndDrop(from,to).build().perform();
			logger.info("dragAndDrop " + fieldName);
			ThreadManager.getDetailReport().addStep("dragAndDrop",
					"drag And Drop to the field"+ fieldName, Status.PASS);
		} catch (Throwable t) {
			logger.info("dragAndDrop: " +"Error while draging and Droping on "
					+ fieldName + "<BR>" + t.getMessage());
			ThreadManager.getDetailReport().addStep("dragAndDrop", "Error while draging and Droping on "
					+ fieldName + "<BR>" + t, Status.FAIL);
			ThreadManager.getSoftAssert().fail(t.getClass().getName() + " -> Error while draging and Droping on "
					+ fieldName + "\n" + t.getMessage());
		}
	}
	/**
	 * dragAndDrop on elements using action API
	 */
	public void dragAndDropBy(String fieldName, WebElement element) {
		dragAndDropBy(fieldName, element, 100, 100);
	}
	/**
	 * dragAndDrop on elements using action API
	 */
	public void dragAndDropBy(String fieldName, WebElement element, int xOffset, int yOffset) {
		try {
			Actions action = new Actions(driver);
			getElement(element);
			Point point = element.getLocation();
			xOffset = point.getX();
			yOffset = point.getY();

			System.out.println("Xoffset:"+xOffset);
			System.out.println("Yoffset:"+yOffset);

			action.dragAndDropBy(element, xOffset, yOffset).build().perform();

			System.out.println("Xoffset:"+element.getSize().getWidth());
			System.out.println("Yoffset:"+element.getSize().getHeight());

			logger.info("dragAndDropBy:  " + fieldName);

			ThreadManager.getDetailReport().addStep("dragAndDropBy",
					"drag And Drop to the field"+ fieldName, Status.PASS);
		} catch (Throwable t) {
			logger.info("dragAndDropBy: " +"Error while draging and Droping By on "
					+ fieldName + "\n" + t);

			ThreadManager.getDetailReport().addStep("dragAndDropBy", "Error while draging and Droping By on "
					+ fieldName + "<BR>" + t, Status.FAIL);
			ThreadManager.getSoftAssert().fail(t.getClass().getName() + " -> Error while draging and Droping By on "
					+ fieldName + "\n" + t.getMessage());
		}
	}

	/**
	 * enter empty value in given text field/
	 * textarea, located by given locator
	 *
	 * @param fieldName
	 * @param @locatorType
	 * @param @locatorValue
	 * @param @value
	 */
	public void enterData(String fieldName, WebElement element) {
		enterData(fieldName, element, "", null);
	}
	/**
	 * enter value in given text field/
	 * textarea, located by given locator
	 *
	 * @param fieldName
	 * @param @locatorType
	 * @param @locatorValue
	 * @param value
	 */
	public void enterData(String fieldName, WebElement element, String value) {
		enterData(fieldName, element, value, null);
	}

	/**
	 * enter value in given text field/
	 * textarea, located by given locator
	 * followed by given key
	 * @param fieldName
	 * @param @locatorType
	 * @param @locatorValue
	 * @param value
	 * @param key
	 */
	public void enterData(String fieldName, WebElement element, String value, Keys key) {
		String valueToBeEntered = value;

		if (key != null) {
			valueToBeEntered = value + key;
		}
		if(value.equalsIgnoreCase("demo2015") || value.equalsIgnoreCase("41795005048") ||
				value.equalsIgnoreCase("48330a0d81cd816fa3a4d038c732963d"))
			value = "********";
		try {
			//          element.clear();
			element.sendKeys(Keys.CONTROL, Keys.chord("a"));
			element.sendKeys(Keys.BACK_SPACE);
			getElement(element).sendKeys(valueToBeEntered);
			logger.info("Entered Value \"" + value +"\" in " + fieldName);
			ThreadManager.getDetailReport().addStep("enterData", "Entered Value \""
					+ value +"\" in " + fieldName, Status.PASS);
		} catch (Throwable t) {
			logger.info(t.getClass().getName() + " -> Error while entering text "
					+ fieldName + "\n" + t.getMessage());
			ThreadManager.getSoftAssert().fail(t.getClass().getName() + " -> Error while entering text \"" + value
					+ "\" in \"" + fieldName + "\" {" + element + "}", t);
			ThreadManager.getDetailReport().addStep("enterData", "Error while entering text \""
					+ value + "\" in \"" + fieldName + "\" {"+ element + "} <BR>" +  t, Status.FAIL);
		}
	}
	/**
	 * enter value in given text field/
	 * textarea, located by given locator
	 *
	 * @param fieldName
	 * @param @locatorType
	 * @param @locatorValue
	 * @param value
	 */
	public void enterDataByJavaScript(String fieldName, WebElement element, String value) {
		enterDataByJavaScript(fieldName, element, value, null);
	}
	/**
	 * enter value in given text field/
	 * textarea, located by given locator
	 * followed by given key
	 * @param fieldName
	 * @param @locatorType
	 * @param @locatorValue
	 * @param value
	 * @param key
	 */
	public void enterDataByJavaScript(String fieldName, WebElement element, String value, Keys key) {
		String valueToBeEntered = value;
		JavascriptExecutor js = (JavascriptExecutor)driver;
		if (key != null) {
			valueToBeEntered = value + key;
		}
		if(value.equalsIgnoreCase("demo2015") || value.equalsIgnoreCase("41795005048") ||
				value.equalsIgnoreCase("48330a0d81cd816fa3a4d038c732963d"))
			value = "********";

		try {
			element.sendKeys(Keys.CONTROL, Keys.chord("a"));
			element.sendKeys(Keys.BACK_SPACE);
			getElement(element);
			js.executeScript("arguments[0].value='"+ valueToBeEntered +"';", element);
			logger.info("Entered Value \"" + value +"\" in " + fieldName);
			ThreadManager.getDetailReport().addStep("enterDataByJavaScript", "Entered Value \""
					+ value +"\" in " + fieldName, Status.PASS);
		} catch (Throwable t) {
			logger.info(t.getClass().getName() + " -> Error while entering text "
					+ fieldName + "\n" + t.getMessage());
			ThreadManager.getSoftAssert().fail(t.getClass().getName() + " -> Error while entering text \"" + value
					+ "\" in \"" + fieldName + "\" {" + element + "}", t);
			ThreadManager.getDetailReport().addStep("enterDataByJavaScript", "Error while entering text \""
					+ value + "\" in \"" + fieldName + "\" {"+ element + "} <BR>" +  t, Status.FAIL);
		}
	}
	/**
	 * verify empty text of label, link, div etc and
	 * empty value of text field/textarea,button
	 * located by given locator
	 * @param @fieldName
	 * @param @locatorType
	 * @param @locatorValue
	 */
	public void verifyData(String fieldName, String locatorType,
			String locatorValue) {
		verifyData(fieldName, locatorType, locatorValue, "");
	}

	/**
	 * verify text of label, link, div etc and
	 * value of text field/textarea,button
	 * located by given locator
	 * @param fieldName
	 * @param locatorType
	 * @param locatorValue
	 * @param expectedData
	 */
	public void verifyData(String fieldName, String locatorType,
			String locatorValue, String expectedData) {
		String actualData = "";
		try {
			actualData = getData(locatorType, locatorValue);
			Assert.assertEquals(actualData, expectedData,
					"\"" + fieldName + "\" data not verified");
			ThreadManager.getDetailReport().addStep("verifyData", fieldName
					+ " data verified", expectedData, actualData, Status.PASS);
		} catch (Throwable t) {
			ThreadManager.getDetailReport().addStep("verifyData", "Error while data verification of  "
					+ fieldName + "<BR>" + t, expectedData, actualData, Status.FAIL);
			ThreadManager.getSoftAssert().fail(t.getClass().getName()
					+ " -> Error while data verification of "
					+ fieldName + "\n" + t.getMessage());
		}
	}

	/**
	 * verify empty text of label, link, div etc and
	 * empty value of text field/textarea,button
	 * located by given locator
	 * @param fieldName
	 * @param @locatorType
	 * @param @locatorValue
	 */
	public void verifyData(String fieldName, WebElement element) {
		verifyData(fieldName, element, "");
	}

	/**
	 * verify text of label, link, div etc and
	 * value of text field/textarea,button
	 * located by given locator
	 * @param @fieldName
	 * @param @element
	 * @param @expectedData
	 */
	public void verifyData(String fieldName, WebElement element, String expectedData) {
		String actualData = "";
		try {
			actualData = getData(element);
			Assert.assertEquals(actualData, expectedData, "\"" + fieldName + "\" data not verified");
			logger.info(fieldName + " data verified: " + "actualData='" + actualData + "' ; \n expectedData='"
					+ expectedData + "'");
			ThreadManager.getDetailReport().addStep("verifyData", fieldName
					+ " data verified", expectedData, actualData, Status.PASS);
		} catch (Throwable t) {
			logger.info("Error while data verification of " + fieldName + " actualData='" + actualData+ "' ;"
					+ "\n expectedData='" + expectedData + "'");
			ThreadManager.getDetailReport().addStep("verifyData", "Error while data verification of  "
					+ fieldName + "<BR>" + t, expectedData, actualData, Status.FAIL);
			ThreadManager.getSoftAssert().fail(t.getClass().getName() + " -> Error while data verification of "
					+ fieldName + "\n" + t.getMessage());
		}
	}

	/**
	 * verify partial text of label, link, div etc and
	 * partial value of text field/textarea,button
	 * @param fieldName
	 * @param element
	 * @param expectedData
	 */
	public void verifyDataContains(String fieldName, WebElement element, String expectedData) {
		verifyDataContains(fieldName, element, expectedData, "false");
	}

	/**
	 * verify partial text of label, link, div etc and
	 * partial value of text field/textarea,button
	 * @param fieldName
	 * @param element
	 * @param expectedData
	 * @param ignoreCaseString
	 */
	public void verifyDataContains(String fieldName, WebElement element, String expectedData, String ignoreCaseString) {
		String actualData = "";
		try {
			boolean ignoreCase = new Boolean(ignoreCaseString);
			actualData = getData(element);
			String expectedStringForComparison = expectedData;
			String actualDataForComparison = actualData;

			// convert expected and actual data to lower case
			// in case comparison is case insensitive
			if (ignoreCase) {
				expectedStringForComparison = expectedStringForComparison.toLowerCase();
				actualDataForComparison = actualDataForComparison.toLowerCase();
			}
			if (actualDataForComparison.contains(expectedStringForComparison)) {
				logger.info("verifyDataContains: "+fieldName + " data verified: " + "actualData='" + actualData + "' ; \n expectedData='"
						+ expectedData + "'");
				ThreadManager.getDetailReport().addStep("verifyDataContains", fieldName + " data verified",
						expectedData, actualData, Status.PASS);
			} else {
				throw new AssertionError(fieldName + " data not verified");
			}
		} catch (Throwable t) {
			logger.info("verifyDataContains \n"+"Error while data verification of " + fieldName + " actualData='" + actualData+ "' ;"
					+ "\n expectedData='" + expectedData + "'");
			ThreadManager.getDetailReport().addStep("verifyDataContains", "Error while data verification of  "
					+ fieldName + "<BR>" + t, expectedData, actualData, Status.FAIL);
			ThreadManager.getSoftAssert().fail(t.getClass().getName() + " -> Error while data verification of "
					+ fieldName + "Actual value \"" + actualData
					+ "\" and expected value \"" + expectedData + "\"\n" + t.getMessage());
		}
	}

	/**
	 * verify partial text of label, link, div etc and partial value of text
	 * field/textarea,button
	 *
	 * @param fieldName
	 * @param element
	 * @param expectedData
	 */
	public void verifyDataContainsWithoutHilighted(String fieldName, WebElement element, String expectedData) {
		verifyDataContainsWithoutHilighted(fieldName, element, expectedData, "false");
	}

	/**
	 * verify partial text of label, link, div etc and partial value of text
	 * field/textarea,button
	 * @param fieldName
	 * @param element
	 * @param expectedData
	 * @param ignoreCaseString
	 */
	public void verifyDataContainsWithoutHilighted(String fieldName, WebElement element, String expectedData,
			String ignoreCaseString) {
		String actualData = "";
		try {
			boolean ignoreCase = new Boolean(ignoreCaseString);
			actualData = getData(element, false);
			System.out.println("actualData>>" + actualData + "<<");
			String expectedStringForComparison = expectedData;
			String actualDataForComparison = actualData;

			// convert expected and actual data to lower case
			// in case comparison is case insensitive
			if (ignoreCase) {
				expectedStringForComparison = expectedStringForComparison.toLowerCase();
				actualDataForComparison = actualDataForComparison.toLowerCase();
			}
			if (actualDataForComparison.contains(expectedStringForComparison)) {
				logger.info("verifyDataContains: " + fieldName + " data verified: " + "actualData='" + actualData
						+ "' ; \n expectedData='" + expectedData + "'");
				ThreadManager.getDetailReport().addStep("verifyDataContains", fieldName + " data verified",
						expectedData, actualData, Status.PASS);
			} else {
				throw new AssertionError(fieldName + " data not verified");
			}
		} catch (Throwable t) {
			logger.info("verifyDataContains \n" + "Error while data verification of " + fieldName + " actualData='"
					+ actualData + "' ;" + "\n expectedData='" + expectedData + "'");
			ThreadManager.getDetailReport().addStep("verifyDataContains",
					"Error while data verification of  " + fieldName + "<BR>" + t, expectedData, actualData,
					Status.FAIL);
			ThreadManager.getSoftAssert()
			.fail(t.getClass().getName() + " -> Error while data verification of " + fieldName
					+ "Actual value \"" + actualData + "\" and expected value \"" + expectedData + "\"\n"
					+ t.getMessage());
		}
	}

	/**
	 * verify existence of field (web element),
	 * located by given locator
	 * @param fieldName
	 * @param @locatorType
	 * @param @locatorValue
	 */
	public void verifyElementExists(String fieldName, WebElement element) {
		try {
			if(!isElementExists(element)) {
				logger.info("verifyElementExists: "+ "\""
						+ fieldName + "\" element not exists "
						+ " with locator {" + element + "}");
				/*BaseLib.test.log(Status.FAIL, "verifyElementExists: "+ "\""
		                    + fieldName + "\" element not exists "
		                    + " with locator {" + element + "}");*/
				ThreadManager.getDetailReport().addStep("verifyElementExists", "\""
						+ fieldName + "\" element not exists "
						+ " with locator {" + element + "}", Status.FAIL);
				ThreadManager.getSoftAssert().fail("org.openqa.selenium.NoSuchElementException -> \""
						+ fieldName + "\" element not exists "
						+ " with locator {" + element + "}");
			} else {
				logger.info("verifyElementExists: "+ "\""
						+ fieldName + "\" element exists ");
				/*BaseLib.test.log(Status.PASS, "verifyElementExists: "+  "\""
			                    + fieldName + "\" element exists " );*/
				ThreadManager.getDetailReport().addStep("verifyElementExists", "\""
						+ fieldName + "\" element exists ", Status.PASS);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * verify field should not exists(web element),
	 * located by given locator type/value
	 * @param fieldName
	 * @param @locatorType
	 * @param @locatorValue
	 */
	public void verifyElementNotExists(String fieldName,WebElement element) {
		try {
			if(isElementExists(element)) {
				logger.info("verifyElementNotExists: "+ "\""
						+ fieldName + "\" element exists "
						+ " with locator {" + element + "}");

				ThreadManager.getDetailReport().addStep("verifyElementNotExists", "\""
						+ fieldName + "\" element exists "
						+ " with locator {" + element + "}", Status.FAIL);
				ThreadManager.getSoftAssert().fail("com.ttn.WebAutomation.seleniumUtils.FrameworkException -> \""
						+ fieldName + "\" element exists "
						+ " with locator {" + element + "}");
			} else {
				logger.info("verifyElementNotExists:"+ "\""
						+ fieldName + "\" element not exists ");
				ThreadManager.getDetailReport().addStep("verifyElementNotExists", "\""
						+ fieldName + "\" element not exists ", Status.PASS);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * verify attribute of specified
	 * field, located by given locator
	 * @param fieldName
	 * @param @locatorType
	 * @param @locatorValue
	 * @param attributeNameAndValue
	 */
	public void verifyAttribute(String fieldName, WebElement element, String attributeNameAndValue) {
		String attributeName = "";
		String attributeExpectedValue = "";
		String attributeActualValue = "";
		try {
			if (!attributeNameAndValue.contains(":")) {
				throw new FrameworkException("Incorrect test data, "
						+ "test-data should be in \"attributeName:expected value\" format");
			}
			attributeName = attributeNameAndValue.substring(0,
					attributeNameAndValue.indexOf(':'));
			attributeExpectedValue = attributeNameAndValue.substring(
					attributeNameAndValue.indexOf(':')+1,
					attributeNameAndValue.length());
			// not verifying visibility and enable status for this scenario
			getElement(element, false, false);
			attributeActualValue = element.getAttribute(attributeName);
			Assert.assertEquals(attributeActualValue, attributeExpectedValue,
					fieldName + " attribute \"" + attributeName
					+ "\" value not verified");
			logger.info("verifyAttribute: " + "Attribute \"" + attributeName + "\" value verified of " + fieldName
					+ "attributeExpectedValue: " + attributeExpectedValue + "\n attributeActualValue: "
					+ attributeActualValue);
			/*BaseLib.test.log(Status.PASS, "verifyAttribute: " + "Attribute \"" + attributeName + "\" value verified of " + fieldName
					 	+"<BR>"+ "attributeExpectedValue: "+attributeExpectedValue+ "\n attributeActualValue: "+ attributeActualValue);*/
			ThreadManager.getDetailReport().addStep("verifyAttribute", "Attribute \""
					+ attributeName + "\" value verified of " + fieldName,
					attributeExpectedValue, attributeActualValue, Status.PASS);
		} catch (Throwable t) {
			logger.info("verifyAttribute: "+
					"Error while verification of attribute/value ("
					+ attributeNameAndValue + ") of " + fieldName + "\n"
					+ t+ "attributeExpectedValue: "+attributeExpectedValue+ "\n attributeActualValue: "+ attributeActualValue);

			ThreadManager.getDetailReport().addStep("verifyAttribute",
					"Error while verification of attribute/value ("
							+ attributeNameAndValue + ") of " + fieldName + "<BR>"
							+ t, attributeExpectedValue, attributeActualValue, Status.FAIL);
			ThreadManager.getSoftAssert().fail(t.getClass().getName()
					+ " -> Error while verification of attribute/value ("
					+ attributeNameAndValue + ") of " + fieldName + "\n"
					+ t.getMessage());
		}
	}

	/**
	 * verify that attribute value must contains specified
	 * expected string of given element (field)
	 * @param fieldName
	 * @param @locatorType
	 * @param @locatorValue
	 * @param attributeNameAndValue
	 */
	public void verifyAttributeContains(String fieldName, WebElement element, String attributeNameAndValue) {
		String attributeName = "";
		String attributeExpectedValue = "";
		String attributeActualValue = "";
		try {
			if (!attributeNameAndValue.contains(":")) {
				throw new FrameworkException("Incorrect test data, "
						+ "test-data should be in \"attributeName:expected value\" format");
			}
			attributeName = attributeNameAndValue.substring(0,
					attributeNameAndValue.indexOf(':'));
			attributeExpectedValue = attributeNameAndValue.substring(
					attributeNameAndValue.indexOf(':')+1,
					attributeNameAndValue.length());
			// not verifying visibility and enable status for this scenario
			getElement(element, false, false);
			attributeActualValue = element.getAttribute(attributeName);
			if (attributeActualValue != null
					&& attributeActualValue.contains(attributeExpectedValue)) {

				logger.info("verifyAttributeContains: "+ "Attribute \""
						+ attributeName + "\" value verified of " + fieldName + "attributeExpectedValue: " +
						attributeExpectedValue+ "\n attributeActualValue: "+attributeActualValue);

				ThreadManager.getDetailReport().addStep("verifyAttributeContains", "Attribute \""
						+ attributeName + "\" value verified of " + fieldName,
						attributeExpectedValue, attributeActualValue, Status.PASS);
			} else {
				throw new AssertionError(attributeName + " value not verified");
			}
		} catch (Throwable t) {
			logger.info("verifyAttributeContains: "+
					"Error while verification of attribute/value ("
					+ attributeNameAndValue + ") of " + fieldName + "\n"
					+ t+ "attributeExpectedValue: "+attributeExpectedValue+ "\n attributeActualValue: "+ attributeActualValue);

			ThreadManager.getDetailReport().addStep("verifyAttributeContains",
					"Error while verification of attribute/value ("
							+ attributeNameAndValue + ") of " + fieldName + "<BR>"
							+ t, attributeExpectedValue, attributeActualValue, Status.FAIL);
			ThreadManager.getSoftAssert().fail(t.getClass().getName()
					+ " -> Error while verification of attribute/value ("
					+ attributeNameAndValue + ") of " + fieldName + "\n"
					+ t.getMessage());
		}
	}

	/**
	 * verify that attribute value must not contains specified
	 * expected string of given element (field)
	 * @param fieldName
	 * @param element
	 * @param attributeNameAndValue
	 */
	public void verifyAttributeNotContains(String fieldName,WebElement element, String attributeNameAndValue) {
		String attributeName = "";
		String attributeExpectedValue = "";
		String attributeActualValue = "";
		try {
			if (!attributeNameAndValue.contains(":")) {
				throw new FrameworkException("Incorrect test data, "
						+ "test-data should be in \"attributeName:expected value\" format");
			}
			attributeName = attributeNameAndValue.substring(0,
					attributeNameAndValue.indexOf(':'));
			attributeExpectedValue = attributeNameAndValue.substring(
					attributeNameAndValue.indexOf(':')+1,
					attributeNameAndValue.length());
			// not verifying visibility and enable status for this scenario
			getElement(element, false, false);
			attributeActualValue = element.getAttribute(attributeName);
			if (!attributeActualValue.contains(attributeExpectedValue)) {
				logger.info("verifyAttributeNotContains: "+ "Attribute \""
						+ attributeName + "\" value verified of " + fieldName
						+"attributeExpectedValue: "+ attributeExpectedValue  +"\n attributeActualValue: "+attributeActualValue);

				ThreadManager.getDetailReport().addStep("verifyAttributeNotContains", "Attribute \""
						+ attributeName + "\" value verified of " + fieldName,
						attributeExpectedValue, attributeActualValue, Status.PASS);
			} else {
				throw new AssertionError(attributeName + " value not verified");
			}
		} catch (Throwable t) {
			logger.info("verifyAttributeNotContains: "+
					"Error while verification of attribute/value ("
					+ attributeNameAndValue + ") of " + fieldName + "\n"
					+ t+"attributeExpectedValue: "+ attributeExpectedValue+"\n attributeActualValue: "+ attributeActualValue);

			ThreadManager.getDetailReport().addStep("verifyAttributeNotContains",
					"Error while verification of attribute/value ("
							+ attributeNameAndValue + ") of " + fieldName + "<BR>"
							+ t, attributeExpectedValue, attributeActualValue, Status.FAIL);
			ThreadManager.getSoftAssert().fail(t.getClass().getName()
					+ " -> Error while verification of attribute/value ("
					+ attributeNameAndValue + ") of " + fieldName + "\n"
					+ t.getMessage());
		}
	}
	/**
	 * verify that specified attribute should exists
	 * of given element (field)
	 * @param fieldName
	 * @param @locatorType
	 * @param @locatorValue
	 * @param attributeName
	 */
	public void verifyAttributeExists(String fieldName, WebElement element, String attributeName) {
		try {
			// not verifying visibility and enable status for this scenario
			getElement(element, false, false);
			String attributeActualValue = element.getAttribute(attributeName);
			if (attributeActualValue != null) {
				logger.info("verifyAttributeExists: "+ fieldName
						+ " attribute \""+ attributeName + "\" exists.");
				ThreadManager.getDetailReport().addStep("verifyAttributeExists", fieldName
						+ " attribute \""+ attributeName + "\" exists.",
						Status.PASS);

			} else {
				throw new AssertionError(fieldName
						+ " attribute \""+ attributeName + "\" not exists.");
			}
		} catch (Throwable t) {
			logger.info("verifyAttributeExists: "+
					"Error while verification of attribute ("
					+ attributeName + ") of " + fieldName + "\n"
					+ t);

			ThreadManager.getDetailReport().addStep("verifyAttributeExists",
					"Error while verification of attribute ("
							+ attributeName + ") of " + fieldName + "<BR>"
							+ t, Status.FAIL);
			ThreadManager.getSoftAssert().fail(t.getClass().getName()
					+ " -> Error while verification of attribute ("
					+ attributeName + ") of " + fieldName + "\n"
					+ t.getMessage());
		}
	}
	/**
	 * verify that specified attribute not exists
	 * of given element (field)
	 * @param fieldName
	 * @param @locatorType
	 * @param @locatorValue
	 * @param attributeName
	 */
	public void verifyAttributeNotExists(String fieldName, WebElement element, String attributeName) {
		try {
			// not verifying visibility and enable status for this scenario
			getElement(element, false, false);
			String attributeActualValue = element.getAttribute(attributeName);
			if (attributeActualValue == null) {
				logger.info("verifyAttributeNotExists: "+ fieldName
						+ " attribute \""+ attributeName + "\" not exists.");

				ThreadManager.getDetailReport().addStep("verifyAttributeExists", fieldName
						+ " attribute \""+ attributeName + "\" exists.",
						Status.PASS);
			} else {
				throw new AssertionError(fieldName
						+ " attribute \""+ attributeName + "\" exists.");
			}
		} catch (Throwable t) {
			logger.info("verifyAttributeNotExists: "+
					"Error while verification of attribute ("
					+ attributeName + ") of " + fieldName + "<BR>"
					+ t);

			ThreadManager.getDetailReport().addStep("verifyAttributeNotExists",
					"Error while verification of attribute ("
							+ attributeName + ") of " + fieldName + "<BR>"
							+ t, Status.FAIL);
			ThreadManager.getSoftAssert().fail(t.getClass().getName()
					+ " -> Error while verification of attribute ("
					+ attributeName + ") of " + fieldName + "\n"
					+ t.getMessage());
		}
	}
	/**
	 * verify empty text of label, link, div etc and
	 * empty value of text field/textarea,button
	 * located by given locator
	 * @param fieldName
	 * @param @locatorType
	 * @param @locatorValue
	 */
	public void verifyText(String fieldName, WebElement element) {
		verifyText(fieldName, element, "");
	}

	/**
	 * Verify Text of given Element
	 * @param fieldName
	 * @param element
	 * @param expectedData
	 */
	public void verifyText(String fieldName,WebElement element, String expectedData) {
		String actualData = "";
		try {
			actualData = getText(element);
			Assert.assertEquals(actualData, expectedData,
					"\"" + fieldName + "\" Text not verified");
			logger.info("verifyText: "+ fieldName
					+ " Text verified"+ "{ expectedData = "+expectedData+ "; actualData = "+actualData+"}");

			ThreadManager.getDetailReport().addStep("verifyText", fieldName
					+ " Text verified", expectedData, actualData, Status.PASS);
		} catch (Throwable t) {
			logger.info("verifyText: "+ "Error while data verification of  "
					+ fieldName + "\n" + t+ "{ expectedData = "+expectedData+ "; actualData = "+actualData+"}");

			ThreadManager.getDetailReport().addStep("verifyText", "Error while data verification of  "
					+ fieldName + "<BR>" + t, expectedData, actualData, Status.FAIL);
			ThreadManager.getSoftAssert().fail(t.getClass().getName()
					+ " -> Error while Text verification of "
					+ fieldName + "\n" + t.getMessage());
		}
	}
	/**
	 * verify partial text of label, link, div etc and
	 * partial value of text field/textarea,button
	 * @param fieldName
	 * @param element
	 * @param expectedData
	 */
	public void verifyTextContains(String fieldName,WebElement element, String expectedData) {
		verifyTextContains(fieldName, element, expectedData, "false");
	}

	/**
	 * verify partial text of label, link, div etc and
	 * partial value of text field/textarea,button
	 * @param fieldName
	 * @param element
	 * @param expectedData
	 * @param ignoreCaseString
	 */
	public void verifyTextContains(String fieldName, WebElement element, String expectedData, String ignoreCaseString) {
		String actualData = "";
		try {
			boolean ignoreCase = new Boolean(ignoreCaseString);
			actualData = getText(element);

			String expectedStringForComparison = expectedData;
			String actualDataForComparison = actualData;

			// convert expected and actual data to lower case
			// in case comparison is case insensitive
			if (ignoreCase) {
				expectedStringForComparison = expectedStringForComparison.toLowerCase();
				actualDataForComparison = actualDataForComparison.toLowerCase();
			}
			if (actualDataForComparison.contains(expectedStringForComparison)) {
				logger.info("verifyTextContains: "+ fieldName
						+ " Text verified"+ "{ expectedData = "+expectedData+ "; actualData = "+actualData+"}");

				ThreadManager.getDetailReport().addStep("verifyTextContains", fieldName
						+ " Text verified", expectedData, actualData, Status.PASS);

			} else {
				throw new AssertionError(fieldName + " Text not verified");
			}
		} catch (Throwable t) {
			logger.info("verifyTextContians: "+ "Error while data verification of  "
					+ fieldName + "\n" + t+ "{ expectedData = "+expectedData+ "; actualData = "+actualData+"}");
			ThreadManager.getDetailReport().addStep("verifyTextContains", "Error while Text verification of  "
					+ fieldName + "<BR>" + t, expectedData, actualData, Status.FAIL);

			ThreadManager.getSoftAssert().fail(t.getClass().getName() + " -> Error while Text verification of "
					+ fieldName + "Actual value \"" + actualData
					+ "\" and expected value \"" + expectedData + "\"\n" + t.getMessage());
		}
	}
	/**
	 *
	 * @param element
	 * @return
	 * @throws FrameworkException
	 * @throws Exception
	 */
	protected String getText(WebElement element)
			throws FrameworkException, Exception {
		String actualData = "";
		getElement(element, true, false);
		actualData = element.getText();
		return actualData;
	}
	/**
	 * verify the font size value of given
	 * field with specified locator type/value
	 * @param fieldName
	 * @param @locatorType
	 * @param @locatorValue
	 * @param @fontSize
	 */
	public void verifyTextColor(String fieldName,WebElement element, String expectedFontColor) {
		String actualFontColor = "";
		// RGB to HEX
		String color_hex[];
		try {
			getElement(element);
			fieldName = element.getText();
			//Read font-size property.
			actualFontColor = element.getCssValue("color");

			color_hex = actualFontColor.replace("rgba(", "").split(",");
			actualFontColor = String.format("#%02x%02x%02x", Integer.parseInt(color_hex[0].trim()),
					Integer.parseInt(color_hex[1].trim()), Integer.parseInt(color_hex[2].trim()));

			Assert.assertEquals(actualFontColor, expectedFontColor,
					"\"" + fieldName + "\" Text-color not verified");
			logger.info("verifyTextColor: "+  fieldName
					+ " Text-color verified"+"{ expectedFontColor: "+ expectedFontColor+ "actualFontColor: "+actualFontColor+" }");
			ThreadManager.getDetailReport().addStep("verifyTextColor", fieldName
					+ " Text-color verified", expectedFontColor, actualFontColor, Status.PASS);

		} catch (Throwable t) {
			logger.info("verifyTextColor: "+  "Error while Color verification of  "+ fieldName
					+ " \n"+t.getMessage() +"{ expectedFontColor: "+ expectedFontColor+ "actualFontColor: "+actualFontColor+" }");

			ThreadManager.getDetailReport().addStep("verifyTextColor", "Error while Color verification of  "
					+ fieldName + "<BR>" + t, expectedFontColor, actualFontColor, Status.FAIL);
			ThreadManager.getSoftAssert().fail(t.getClass().getName()
					+ " -> Error while  text Color verification of "
					+ fieldName + "\n" + t.getMessage());
		}
	}
	/**
	 * verify the font size value of given
	 * field with specified locator type/value
	 * @param fieldName
	 * @param @locatorType
	 * @param @locatorValue
	 * @param @font-Family
	 */
	public void verifyFontFamily(String fieldName, WebElement element, String expectedFontFamily) {
		String actualFontFamily = "";
		try {
			fieldName = element.getText();
			//Read font-size property.
			actualFontFamily = element.getCssValue("font-family");
			Assert.assertEquals(actualFontFamily, expectedFontFamily,
					"\"" + fieldName + "\" Font font-family not verified");
			logger.info("verifyFontFamily: "+ fieldName
					+ " font-family verified"+" { expectedFontFamily: "+ expectedFontFamily + "actualFontFamily: "+actualFontFamily+" }");

			ThreadManager.getDetailReport().addStep("verifyFontFamily", fieldName
					+ " font-family verified", expectedFontFamily, actualFontFamily, Status.PASS);

		} catch (Throwable t) {
			logger.info("verifyFontFamily:"+ "Error while font-family verification of  "
					+ fieldName + "\n" + t+" \n{ expectedFontFamily: "+ expectedFontFamily + "actualFontFamily: "+actualFontFamily+" }");

			ThreadManager.getDetailReport().addStep("verifyFontFamily", "Error while font-family verification of  "
					+ fieldName + "<BR>" + t, expectedFontFamily, actualFontFamily, Status.FAIL);
			ThreadManager.getSoftAssert().fail(t.getClass().getName()
					+ " -> Error while font-family verification of "
					+ fieldName + "\n" + t.getMessage());
		}
	}
	/**
	 * verify the font size value of given
	 * field with specified locator type/value
	 * @param fieldName
	 * @param @locatorType
	 * @param @locatorValue
	 * @param @text-align
	 */
	public void verifyTextAlign(String fieldName, WebElement element, String expectedTextAlign) {
		String actualTextAlign = "";
		try {
			fieldName = element.getText();
			//Read font-size property.
			actualTextAlign = element.getCssValue("text-align");
			Assert.assertEquals(actualTextAlign, expectedTextAlign,
					"\"" + fieldName + "\" Font text-align not verified");
			logger.info("verifyTextAlign: "+ fieldName
					+ " Font-family verified"+"\n { expectedTextAlign: "+ expectedTextAlign+" actualTextAlign: "+ actualTextAlign+" }");

			ThreadManager.getDetailReport().addStep("verifyTextAlign", fieldName
					+ " Font-family verified", expectedTextAlign, actualTextAlign, Status.PASS);

		} catch (Throwable t) {
			logger.info("verifyTextAlign:"+ "Error while text-align verification of  "
					+ fieldName + "<BR>" + t +"\n { expectedTextAlign: "+ expectedTextAlign+" actualTextAlign: "+ actualTextAlign+" }");

			ThreadManager.getDetailReport().addStep("verifyTextAlign", "Error while text-align verification of  "
					+ fieldName + "<BR>" + t, expectedTextAlign, actualTextAlign, Status.FAIL);
			ThreadManager.getSoftAssert().fail(t.getClass().getName()
					+ " -> Error while text-align verification of "
					+ fieldName + "\n" + t.getMessage());
		}
	}


	/**
	 * wait for specified field got visible, located by given locator
	 * @param fieldName
	 * @param element
	 * @param waitSecondsString
	 */
	public void explicitWaitForElementGetVisible(String fieldName, WebElement element, String waitSecondsString) {
		int waitSeconds = 15;
		try {
			waitSeconds = Integer.parseInt(waitSecondsString);
		} catch (Exception e) {

		}
		logger.info("explicitWaitForFieldGetVisible" + " Wait untill \"" + fieldName + "\" got visible or upto "
				+ waitSeconds + " second(s)");

		ThreadManager.getDetailReport().addStep("explicitWaitForFieldGetVisible",
				"Wait untill \"" + fieldName + "\" got visible or upto " + waitSeconds + " second(s)", Status.PASS);
		try {
			Thread.sleep(1000);
			WebDriverWait wait = new WebDriverWait(driver, waitSeconds);

			Thread.sleep(1000);
		} catch (Throwable t) {
			logger.info(fieldName + " did not got visible" + t);

			ThreadManager.getDetailReport().addStep("explicitWaitForFieldGetVisible", fieldName
					+ " did not got visible" + "<BR>" + t, Status.FAIL);
			ThreadManager.getSoftAssert()
			.fail(t.getClass().getName() + " -> " + fieldName + " did not got visible" + "\n" + t.getMessage());
		}
	}
	/**
	 * check if element exists for the
	 * given locator
	 * @param @locatorType
	 * @param @locatorValue
	 * @return
	 * @throws Exception
	 * @throws FrameworkException
	 */
	public boolean isElementExists(WebElement element) throws Exception {
		driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
		boolean isElementExists = true;
		if (driver.findElements(getBy(element)).size()
				== 0) {
			isElementExists = false;
		} else {
			try {
				// calling below method to just highlight the
				// current web element
				getElement(element);
			} catch (Throwable t) {
				// dumping exception (if any)
			}

		}
		int globalSyncSeconds = Integer.parseInt("15");
		driver.manage().timeouts().implicitlyWait(globalSyncSeconds, TimeUnit.SECONDS);
		return isElementExists;
	}
	/**
	 * Put focus on specified field name
	 * identified by given locator type/value
	 * @param fieldName
	 * @param @locatorType
	 * @param @locatorValue
	 */
	public void focusMoveToElement(String fieldName, WebElement element) {
		try {
			Actions action = new Actions(driver);
			getElement(element);
			action.moveToElement(element).build().perform();
			logger.info("focusMoveToElement: " + "Focus moved to field " + fieldName);
			ThreadManager.getDetailReport().addStep("focusMoveToElement",
					"Focus moved to field "+ fieldName, Status.PASS);
		} catch (Throwable t) {
			logger.info("focusMoveToElement: "+ "Error while focus movement to field "
					+ fieldName +"\n"+ t);
			ThreadManager.getDetailReport().addStep("focusMoveToElement",
					"Error while focus movement to field "
							+ fieldName + " <BR>"+ t, Status.FAIL);
			ThreadManager.getSoftAssert().fail(t.getClass().getName()
					+ " -> Error while focus movement to field "
					+ fieldName + "\n"+ t.getMessage());
		}
	}
	/**
	 * Put focus on specified field name
	 * identified by given locator type/value
	 * @param fieldName
	 * @param locatorType
	 * @param locatorValue
	 */
	public void focusMoveToElement(String fieldName, String locatorType,
			String locatorValue) {
		try {
			Actions action = new Actions(driver);
			WebElement element = getElement(getBy(locatorType, locatorValue));
			action.moveToElement(element).build().perform();
			ThreadManager.getDetailReport().addStep("focusMoveToElement",
					"Focus moved to field "+ fieldName, Status.PASS);
		} catch (Throwable t) {
			ThreadManager.getDetailReport().addStep("focusMoveToElement",
					"Error while focus movement to field "
							+ fieldName + " <BR>"+ t, Status.FAIL);
			ThreadManager.getSoftAssert().fail(t.getClass().getName()
					+ " -> Error while focus movement to field "
					+ fieldName + "\n"+ t.getMessage());
		}
	}
	/**
	 * respond to alert/confirmation/prompt
	 * dialog box as per given value
	 * @param responseString
	 */
	public void respondAlert(String responseString) {
		respondAlert(responseString,1);
	}

	/**
	 * respond to alert/confirmation/prompt
	 * dialog box as per given value
	 * with specified retry count,
	 * (with the interval of 1 second)
	 * in case dialog box not visible
	 * @param responseString
	 * @param retryCount
	 */
	public void respondAlert(String responseString, int retryCount) {
		try {
			boolean response = new Boolean(responseString);
			if (response) {
				getAlert(retryCount).accept();
				logger.info("respondAlert: "+
						"Clicked on Ok button");
				ThreadManager.getDetailReport().addStep("respondAlert",
						"Clicked on Ok button", Status.PASS);

			} else {
				getAlert(retryCount).dismiss();
				logger.info("respondAlert: "+
						"Clicked on Cancel button");
				ThreadManager.getDetailReport().addStep("respondAlert",
						"Clicked on Cancel button", Status.PASS);

			}
		} catch (Throwable t) {
			logger.info("respondAlert: "+ "Error occured while "
					+ "responding alert \n "+ t);

			ThreadManager.getDetailReport().addStep("respondAlert", "Error occured while "
					+ "responding alert <BR> "+ t, Status.FAIL);
			ThreadManager.getSoftAssert().fail(t.getClass().getName() + " -> Error occured while "
					+ "responding alert \n" + t.getMessage());
		}
	}
	/**
	 * get the instance of alert/confirmation/prompt
	 * based on retry setting
	 * @param retryCount
	 * @return
	 */
	private Alert getAlert(int retryCount) {
		Alert alert = null;
		for (int i =1; i <= retryCount; i++) {
			try {
				alert = driver.switchTo().alert();
				break;
			} catch(NoAlertPresentException e) {
				if (i == retryCount) {
					logger.info("respondAlert: "+
							"Unable to switch to alert window in "
							+ retryCount + " seconds.");
					ThreadManager.getDetailReport().addStep("respondAlert",
							"Unable to switch to alert window in "
									+ retryCount + " seconds.", Status.FAIL);

					ThreadManager.getSoftAssert().fail(e.getClass().getName()
							+  " -> Unable to switch to alert window in "
							+ retryCount + " seconds.");
				}
				logger.info("Alert not found, will retry after 1 second");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {

				}
			}
		}
		return alert;
	}
	/**
	 * switch to frame by given frame index
	 * @param frameIndex
	 */
	public void switchToFrameByIndex(String frameIndex) {
		try {
			driver.switchTo().defaultContent();
			driver.switchTo().frame(Integer.parseInt(frameIndex));
			logger.info("switchToFrameByIndex: "+
					"Switched to frame index \""+ frameIndex + "\"");
			ThreadManager.getDetailReport().addStep("switchToFrameByIndex",
					"Switched to frame index \""+ frameIndex + "\"", Status.PASS);

		} catch (Throwable t) {
			logger.info("switchToFrameByIndex: "+
					"Error occured while switching frame \"" + frameIndex
					+ "\"<BR>" + t);

			ThreadManager.getDetailReport().addStep("switchToFrameByIndex",
					"Error occured while switching frame \"" + frameIndex
					+ "\"<BR>" + t, Status.FAIL);
			ThreadManager.getSoftAssert().fail(t.getClass().getName() + " -> "
					+ "Error occured while switching frame \""
					+ frameIndex + "\" \n" + t.getMessage());
		}
	}

	/**
	 * switch to frame by given frame name
	 * @param frameName
	 */
	public void switchToFrameByName(String frameName) {
		try {
			driver.switchTo().defaultContent();
			driver.switchTo().frame(frameName);
			logger.info("switchToFrameByName: "+ "Switched to frame named \""
					+ frameName + "\"");

			ThreadManager.getDetailReport().addStep("switchToFrameByName", "Switched to frame named \""
					+ frameName + "\"", Status.PASS);
		} catch(Throwable t) {
			logger.info("switchToFrameByName: "+
					"Error occured while switching frame \""
					+ frameName + "\"<BR>" + t);
			ThreadManager.getDetailReport().addStep("switchToFrameByName",
					"Error occured while switching frame \""
							+ frameName + "\"<BR>" + t, Status.FAIL);

			ThreadManager.getSoftAssert().fail(t.getClass().getName()
					+ " -> Error occured while switching frame \""
					+ frameName +"\" \n" + t.getMessage());
		}
	}

	/**
	 * switch to inner frame by given frame name
	 * @param parentFrameName
	 * @param childFrameName
	 */
	public void switchToInnerFrameByName(String parentFrameName, String childFrameName) {
		try {
			driver.switchTo().defaultContent();
			driver.switchTo().frame(parentFrameName);
			int childFrameCount = driver.findElements(
					By.tagName("iframe")).size();
			if(childFrameCount != 0) {
				driver.switchTo().frame(childFrameName);
			}
			logger.info("switchToInnerFrameByName: "+ "Switched to frame named \""
					+ childFrameName + "\"");
			ThreadManager.getDetailReport().addStep("switchToInnerFrameByName", "Switched to frame named \""
					+ childFrameName + "\"", Status.PASS);

		} catch(Throwable t) {

			logger.info("switchToInnerFrameByName: "+
					"Error occured while switching frame \""
					+ parentFrameName + "\"<BR>" + t);
			ThreadManager.getDetailReport().addStep("switchToInnerFrameByName",
					"Error occured while switching frame \""
							+ parentFrameName + "\"<BR>" + t, Status.FAIL);

			ThreadManager.getSoftAssert().fail(t.getClass().getName()
					+ " -> Error occured while switching inner frame \""
					+ parentFrameName +"\" \n" + t.getMessage());
		}
	}
	/**
	 * switch to frame by given frame index
	 * @param @frameIndex
	 */
	public void switchToFrameDefaultContent() {
		try {
			driver.switchTo().defaultContent();
			logger.info("switchToFrameDefaultContent: "+
					"Switched to frame index \""+ "switchToFrameDefaultConten" + "\"");

			ThreadManager.getDetailReport().addStep("switchToFrameDefaultContent",
					"Switched to frame index \""+ "switchToFrameDefaultConten" + "\"", Status.PASS);
		} catch (Throwable t) {
			logger.info("switchToFrameDefaultContent: "+
					"Error occured while switching frame \"" + "switchToFrameDefaultContent"
					+ "\"<BR>" + t);
			ThreadManager.getDetailReport().addStep("switchToFrameDefaultContent",
					"Error occured while switching frame \"" + "switchToFrameDefaultContent"
							+ "\"<BR>" + t, Status.FAIL);

			ThreadManager.getSoftAssert().fail(t.getClass().getName() + " -> "
					+ "Error occured while switching frame \""
					+ "switchToFrameDefaultContent" + "\" \n" + t.getMessage());
		}
	}

	/**
	 * switch to frame by given locator
	 * @param @locatorType
	 * @param @locatorValue
	 */
	public void switchToFrameByWebElement(WebElement element) {
		try {
			driver.switchTo().frame(getElement(element, false, false));

			logger.info("switchToFrameByWebElement: "+
					"Switched to frame with locator {"
					+ element + ":" +   "}");

			ThreadManager.getDetailReport().addStep("switchToFrameByWebElement",
					"Switched to frame with locator {"
							+ element + "}", Status.PASS);
		} catch (Throwable t) {
			logger.info("switchToFrameByWebElement: "+
					"Error while switching frame <BR>"+ t);

			ThreadManager.getDetailReport().addStep("switchToFrameByWebElement",
					"Error while switching frame <BR>"+ t,
					Status.FAIL);
			ThreadManager.getSoftAssert().fail(t.getClass().getName() + " -> "
					+ "Error while switching frame "+ t.getMessage());
		}
	}
	/**
	 * switch to popup window by given
	 * window handle
	 * @param handle
	 */
	public void switchToWindowByHandle(String handle) {
		try {
			Set<String> openWindowHandlesSet = driver.getWindowHandles();
			Iterator<String> openWindowIterator = openWindowHandlesSet.iterator();
			ThreadManager.setPreviousWindowHandle(driver.getWindowHandle());
			while (openWindowIterator.hasNext()) {
				String currentHandle = (String) openWindowIterator.next();
				if (currentHandle.equals(handle)) {
					driver.switchTo().window(handle);

					logger.info("switchToWindowByHandle: "+
							"Switched to window handel "
							+ handle);
					ThreadManager.getDetailReport().addStep("switchToWindowByHandle",
							"Switched to window handel "
									+ handle, Status.PASS);
					break;
				}
			}
		} catch (Throwable t) {

			logger.info("switchToWindowByHandle: "+
					"Error occured while switching to window by title \""
					+ handle + "\n" + t);
			ThreadManager.getDetailReport().addStep("switchToWindowByHandle",
					"Error occured while switching to window by title \""
							+ handle + "\"<BR>" + t, Status.FAIL);
			ThreadManager.getSoftAssert().fail(t.getClass().getName()
					+ " -> Error occured while switching to window by title \""
					+ handle + "\" \n" + t.getMessage());
		}
	}

	/**
	 * switch to popup window by given window Index
	 * @param @Index
	 */
	public void switchToWindowByIndex(String indexString){
		int index = Integer.parseInt(indexString);
		ThreadManager.setPreviousWindowHandle(driver.getWindowHandle());
		Set<String> allWindowHadils = driver.getWindowHandles();

		String handleWindow[] = new String[allWindowHadils.size()];
		int i = 0;
		for (String handle : allWindowHadils) {
			handleWindow[i] = handle;
			i++;
		}
		if (driver.switchTo().window(handleWindow[index]) != null) {

			logger.info("switchToWindowByIndex: "+
					"Switched to window with index "
					+ index);
			ThreadManager.getDetailReport().addStep("switchToWindowByIndex",
					"Switched to window with index "
							+ index, Status.PASS);
		}
	}

	/**
	 * switch to popup window by given
	 * window title
	 * @param title
	 */
	public void switchToWindowByTitle(String title) {
		try {
			Set<String> openWindowHandlesSet = driver.getWindowHandles();
			Iterator<String> openWindowIterator = openWindowHandlesSet.iterator();
			ThreadManager.setPreviousWindowHandle(driver.getWindowHandle());
			while (openWindowIterator.hasNext()) {
				String currentHandle = (String) openWindowIterator.next();
				driver.switchTo().window(currentHandle);
				if (driver.getTitle().equals(title)) {

					logger.info("switchToWindowByTitle: "+
							"Switched to window with title: "
							+ title);
					ThreadManager.getDetailReport().addStep("switchToWindowByTitle",
							"Switched to window with title: "
									+ title, Status.PASS);
					return;
				}
			}
			throw new NoSuchWindowException("Unable to switched to window with title "
					+ title);
		} catch (Throwable t) {

			logger.info("switchToWindowByTitle: "+
					"Error occured while switching to window by title \""
					+ title + "\n" + t);
			ThreadManager.getDetailReport().addStep("switchToWindowByTitle",
					"Error occured while switching to window by title \""
							+ title + "\"<BR>" + t, Status.FAIL);
			ThreadManager.getSoftAssert().fail(t.getClass().getName()
					+ " -> Error occured while switching to window by title \""
					+ title + "\" \n" + t.getMessage());
		}
	}
	/**
	 * select value in select/list box
	 * as per index value, located by
	 * given locator
	 * @param fieldName
	 * @param @locatorType
	 * @param @locatorValue
	 * @param indexString
	 */
	public void selectDropDownByIndex(String fieldName, WebElement element, String indexString) {
		int index = -1;
		try {
			try {
				index = Integer.parseInt(indexString);
			} catch (Exception e) {
				throw new FrameworkException(fieldName + ": Invalid index value: "
						+ indexString);
			}
			getSelect(element).selectByIndex(index);

			logger.info("selectDropDownByIndex: "+ "Index \"" + indexString
					+ "\"" + " is selected in " + fieldName);
			ThreadManager.getDetailReport().addStep("deselectDropDownByIndex",
					"Index \"" + indexString + "\"" + " is deselected in "
							+ fieldName, Status.PASS);
		} catch (Throwable t) {

			logger.info("selectDropDownByIndex: "+ "Index \"" + indexString
					+ "\"" + " is not selected in " + fieldName + "\n" + t);
			ThreadManager.getDetailReport().addStep("deselectDropDownByIndex",  "Index \""
					+ indexString + "\"" + " is not deselected in "
					+ fieldName + "<BR>" + t, Status.FAIL);
			ThreadManager.getSoftAssert().fail(t.getClass().getName() + " -> Index \"" + indexString
					+ "\"" + " is selected in " + fieldName + "\n" + t.getMessage());
		}
	}

	/**
	 * select value in select/list box
	 * as per option value, located by
	 * given locator
	 * @param fieldName
	 * @param @locatorType
	 * @param @locatorValue
	 * @param value
	 */
	public void selectDropDownByValue(String fieldName, WebElement element, String value) {
		try {
			getSelect(element).selectByValue(value);
			logger.info("selectDropDownByValue: "+ "Value \"" + value
					+ "\"" + " is selected in " + fieldName);
			ThreadManager.getDetailReport().addStep("selectDropDownByValue", "Value \"" + value
					+ "\"" + " is deselected in " + fieldName, Status.PASS);
		} catch (Throwable t) {
			logger.info("selectDropDownByValue: "+ " value \"" + value
					+ "\"" + " is not selected in " + fieldName + "\n" + t);
			ThreadManager.getDetailReport().addStep("selectDropDownByValue",  "Value \"" + value
					+ "\"" + " is not deselected in " + fieldName + "<BR>"
					+ t, Status.FAIL);
			ThreadManager.getSoftAssert().fail(t.getClass().getName() + " -> \"" + value
					+ " value \"" + " is not selected in " + fieldName + "\n"
					+ t.getMessage());
		}
	}

	/**
	 * select value in select/list box
	 * as per visible text, located by
	 * given locator
	 * @param fieldName
	 * @param @locatorType
	 * @param @locatorValue
	 * @param visibleText
	 */
	public void selectDropDownByVisibleText(String fieldName, WebElement element, String visibleText) {
		try {
			getSelect(element).selectByVisibleText(visibleText);
			/*BaseLib.test.log(Status.PASS,"selectDropDownByVisibleText: "+ "Visible text \""
                    + visibleText + "\"" + " is selected in " + fieldName);*/
			ThreadManager.getDetailReport().addStep("deselectDropDownByVisibleText",
					"Visible text \"" + visibleText + "\"" + " is deselected in "
							+ fieldName, Status.PASS);
			logger.info("selectDropDownByVisibleText: "+ "Visible text \""
					+ visibleText + "\"" + " is selected in " + fieldName);
		} catch (Throwable t) {

			ThreadManager.getDetailReport().addStep("deselectDropDownByVisibleText",
					"Visible text \"" + visibleText + "\"" + " is not deselected in "
							+ fieldName + "<BR>" + t, Status.FAIL);
			logger.info("selectDropDownByVisibleText: "+ "Visible text \""
					+ visibleText + "\"" + " is not selected in " + fieldName
					+ "<BR>" + t.getMessage());
			ThreadManager.getSoftAssert().fail(t.getClass().getName() + " -> Visible text \""
					+ visibleText + "\"" + " is selected in " + fieldName + "\n"+ t.getMessage());
		}
	}
	/**
	 * get select/list instance, located
	 * by given locator
	 * @param @locatorType
	 * @param @locatorValue
	 * @return
	 * @throws Exception
	 */
	private Select getSelect(WebElement element) throws Exception {
		return getSelect(element, true, true);
	}

	private Select getSelect(WebElement element, boolean isDisplayCheck, boolean isEnableCheck) throws Exception {
		WebElement dropDownElement = getElement(element, isDisplayCheck, isEnableCheck);
		return new Select(dropDownElement);
	}

	/**
	 * press the given key once
	 * @param key
	 */
	public void pressKey(String key) {
		pressKey(key, "1");
	}

	/**
	 * press the given key as specified number of times
	 * e.g. ENTER, ESCAPE, PAGE_DOWN
	 * PAGE_UP, TAB, ARROW_DOWN, ARROW_UP,
	 * ARROW_LEFT, ARROW_RIGHT
	 * @param key
	 */
	public void pressKey(String key, String countString) {
		try {
			int count = Integer.parseInt(countString);
			Actions action = new Actions(driver);
			for (int counter = 0; counter < count; counter++) {
				switch (key.toLowerCase()) {
				case "enter":
					action.sendKeys(Keys.ENTER).build().perform();
					break;
				case "escape":
					action.sendKeys(Keys.ESCAPE).build().perform();
					break;
				case "page_down":
					action.sendKeys(Keys.PAGE_DOWN).build().perform();
					break;
				case "page_up":
					action.sendKeys(Keys.PAGE_UP).build().perform();
					break;
				case "tab":
					action.sendKeys(Keys.TAB).build().perform();
					break;
				case "arrow_down":
					action.sendKeys(Keys.ARROW_DOWN).build().perform();
					break;
				case "arrow_up":
					action.sendKeys(Keys.ARROW_UP).build().perform();
					break;
				case "arrow_left":
					action.sendKeys(Keys.ARROW_LEFT).build().perform();
					break;
				case "arrow_right":
					action.sendKeys(Keys.ARROW_RIGHT).build().perform();
					break;
				case "backspace":
					action.sendKeys(Keys.BACK_SPACE).build().perform();
					break;
				case "5":
					action.sendKeys(Keys.SEMICOLON).build().perform();
					break;
				case "F5":
					action.sendKeys(Keys.F5).build().perform();
					break;
				}
			}

			logger.info("pressKey: "+ key
					+" key pressed");
			ThreadManager.getDetailReport().addStep("pressKey", key
					+" key pressed", Status.PASS);
		} catch (Throwable t) {

			logger.info("pressKey: "+ "Error while pressing "
					+ key + " key \n" + t);
			ThreadManager.getDetailReport().addStep("pressKey", "Error while pressing "
					+ key + " key<BR>" + t, Status.FAIL);
			ThreadManager.getSoftAssert().fail(t.getClass().getName()+
					" -> Error while pressing "+ key + " key\n"+ t.getMessage());
		}
	}

	/**
	 * pause the script by specified seconds
	 * @param waitSecondsString
	 */
	public void pause(String waitSecondsString){
		int waitSeconds = 5;
		try {
			waitSeconds = Integer.parseInt(waitSecondsString) * 1000;
			logger.info("Pause script for " + waitSecondsString
					+ " second(s)");
			Thread.sleep(waitSeconds);
		} catch (InterruptedException e) {

		} catch (Exception e) {

		}
	}
	/**
	 * get By instance based on given locator type
	 * and locator value
	 * @param locatorType
	 * @param locatorValue
	 * @return
	 * @throws FrameworkException
	 */
	public By getBy(String locatorType, String locatorValue)
			throws FrameworkException {

		By by = null;
		switch (locatorType.toLowerCase()) {
		case "id":                    by = By.id(locatorValue);
		break;
		case "name":                by = By.name(locatorValue);
		break;
		case "xpath":                by = By.xpath(locatorValue);
		break;
		case "cssselector":            by = By.cssSelector(locatorValue);
		break;
		case "classname":            by = By.className(locatorValue);
		break;
		case "linktext":            by = By.linkText(locatorValue);
		break;
		case "partiallinktext":        by = By.partialLinkText(locatorValue);
		break;
		default:                    throw new FrameworkException(
				"Invalid locator type defined: " + locatorType);
		}
		return by;
	}

	/**
	 * get By instance based on given locator type
	 * and locator value
	 * @param @locatorType
	 * @param @locatorValue
	 * @return
	 * @throws FrameworkException
	 */
	public By getBy(WebElement element)
			throws Exception {
		String locatorTypeAndlocatorValue = element.toString();
		String locatorType = "";
		String locatorValue = "";
		int stringLength = 0;

		if(locatorTypeAndlocatorValue.contains("By.")) {
			locatorType = locatorTypeAndlocatorValue.split("By.")[1].split(":")[0].trim();
			stringLength = locatorTypeAndlocatorValue.split("By.")[1].split(":")[1].trim().length();
			locatorValue = locatorTypeAndlocatorValue.split("By.")[1].split(":")[1].trim().substring(0, stringLength-1);

		} else if(locatorTypeAndlocatorValue.contains("->")) {
			locatorType = locatorTypeAndlocatorValue.split("->")[1].split(":")[0].trim();
			stringLength = locatorTypeAndlocatorValue.split("->")[1].split(":")[1].trim().length();
			locatorValue = locatorTypeAndlocatorValue.split("->")[1].split(":")[1].trim().substring(0, stringLength-1);
		}
		By by = null;
		switch (locatorType.toLowerCase()) {
		case "id":                  by = By.id(locatorValue);
		break;
		case "name":                by = By.name(locatorValue);
		break;
		case "xpath":               by = By.xpath(locatorValue);
		break;
		case "css":            		by = By.cssSelector(locatorValue);
		break;
		case "classname":           by = By.className(locatorValue);
		break;
		case "linktext":            by = By.linkText(locatorValue);
		break;
		case "partiallinktext":     by = By.partialLinkText(locatorValue);
		break;
		default:                    throw new Exception(
				"Invalid locator type defined: " + locatorType);
		}
		return by;
	}
	/**
	 * gets the value of text field, text-area
	 * or label/button text
	 * @param @locatorType
	 * @param @locatorValue
	 * @return
	 * @throws FrameworkException
	 * @throws Exception
	 */
	public String getData(WebElement element)
			throws Exception {
		return getData(element, true);
	}

	/**
	 * gets the value of text field, text-area
	 * or label/button text
	 * @param @locatorType
	 * @param @locatorValue
	 * @return
	 * @throws FrameworkException
	 * @throws Exception
	 */
	public String getData(WebElement element, boolean hilighted)
			throws Exception {
		String actualData = "";
		if (hilighted) {
			getElement(element, true, false);
		}
		String elementTypeAttribute = element.getAttribute("type");
		if (elementTypeAttribute != null &&
				(elementTypeAttribute.equalsIgnoreCase("text")
						|| elementTypeAttribute.equalsIgnoreCase("textarea")
						|| elementTypeAttribute.equalsIgnoreCase("submit")
						|| elementTypeAttribute.equalsIgnoreCase("button"))) {
			actualData = element.getAttribute("value");
		} else {
			actualData = element.getText();
		}
		return actualData;
	}
	/**
	 * gets the value of text field, text-area
	 * or label/button text
	 * @param locatorType
	 * @param locatorValue
	 * @return
	 * @throws FrameworkException
	 * @throws Exception
	 */
	public String getData(String locatorType, String locatorValue)
			throws FrameworkException, Exception {
		String actualData = "";
		WebElement element = getElement(getBy(locatorType, locatorValue), true, false);
		String elementTypeAttribute = element.getAttribute("type");
		if (elementTypeAttribute != null &&
				(elementTypeAttribute.equalsIgnoreCase("text")
						|| elementTypeAttribute.equalsIgnoreCase("textarea")
						|| elementTypeAttribute.equalsIgnoreCase("submit")
						|| elementTypeAttribute.equalsIgnoreCase("button"))) {
			actualData = element.getAttribute("value");
		} else {
			actualData = element.getText();
		}
		return actualData;
	}

	/**
	 * get instance of web element based
	 * on given by reference and make-sure
	 * that element is displayed and in
	 * enabled state
	 * it also highlight the element
	 * during test run
	 * @param @by
	 * @return
	 * @throws Exception
	 */
	public WebElement getElement(WebElement element) throws Exception {
		return getElement(element, true, true);
	}

	/**
	 * get instance of web element based
	 * on given by reference, display and
	 * enable check
	 * @param @by
	 * @param isDisplayCheck
	 * @param isEnabledCheck
	 * @return
	 * @throws Exception
	 */
	public WebElement getElement(WebElement element, boolean isDisplayCheck,
			boolean isEnabledCheck) throws Exception {

		try {
			if (ThreadManager.getPreviousElement() != null && driver
					instanceof org.openqa.selenium.JavascriptExecutor) {
				Thread.sleep(300);
				((org.openqa.selenium.JavascriptExecutor)driver).executeScript(
						"arguments[0].style.border='none';", ThreadManager.getPreviousElement());
				Thread.sleep(300);
			}
		} catch (Throwable t) {
			//dumping exception
		}
		if (element != null) {
			// ignoring exceptions in case of IEDriver as its false alarm
			try {
				if (isDisplayCheck && (!element.isDisplayed())) {
					throw new ElementNotVisibleException("Element is not displayed");
				}
			} catch (InvalidArgumentException e) {
				if (!(driver instanceof InternetExplorerDriver)) {
					throw e;
				}
				logger.info("InternetExplorerDriver: Dumping InvalidArgumentException for "+ element);
			} catch (JsonException e) {
				if (!(driver instanceof InternetExplorerDriver)) {
					throw e;
				}
				logger.info("InternetExplorerDriver: Dumping JsonException for "+ element);
			} catch (JavascriptException e) {
				if (!(driver instanceof InternetExplorerDriver)) {
					throw e;
				}
				logger.info("InternetExplorerDriver: Dumping JavascriptException for "+ element);
			}
			if (isEnabledCheck && (!element.isEnabled())) {
				throw new ElementNotSelectableException("Element is not enabled");
			}
			if (driver instanceof org.openqa.selenium.JavascriptExecutor) {
				((org.openqa.selenium.JavascriptExecutor)driver).executeScript(
						"arguments[0].style.border='4px solid red';",
						//                        "arguments[0].setAttribute('style', 'background: yellow; border: 4px solid red;')",
						element);
			}
			ThreadManager.setPreviousElement(element);

			Point elementLocation = element.getLocation();
			((JavascriptExecutor)driver).executeScript("window.scrollTo("
					+ "0,"+(elementLocation.getY() - 150)+");");
			return element;
		} else {
			throw new NoSuchElementException("Unable to find element by specified locators");
		}
	}
	/**
	 * get instance of web element based
	 * on given by reference and make-sure
	 * that element is displayed and in
	 * enabled state
	 * it also highlight the element
	 * during test run
	 * @param by
	 * @return
	 * @throws Exception
	 */
	public WebElement getElement(By by) throws Exception {
		return getElement(by, true, true);
	}

	/**
	 * get instance of web element based
	 * on given by reference, display and
	 * enable check
	 * @param by
	 * @param isDisplayCheck
	 * @param isEnabledCheck
	 * @return
	 * @throws Exception
	 */
	public WebElement getElement(By by, boolean isDisplayCheck,
			boolean isEnabledCheck) throws Exception {
		WebElement element = driver.findElement(by);
		//		return element;
		try {
			if (ThreadManager.getPreviousElement() != null && driver
					instanceof org.openqa.selenium.JavascriptExecutor) {
				((org.openqa.selenium.JavascriptExecutor)driver).executeScript(
						"arguments[0].style.border='none';", ThreadManager.getPreviousElement());
			}
		} catch (Throwable t) {
			//dumping exception
		}
		if (element != null) {
			// ignoring exceptions in case of IEDriver as its false alarm
			try {
				if (isDisplayCheck && (!element.isDisplayed())) {
					throw new ElementNotVisibleException("Element is not displayed");
				}
			} catch (InvalidArgumentException e) {
				if (!(driver instanceof InternetExplorerDriver)) {
					throw e;
				}
				logger.info("InternetExplorerDriver: Dumping InvalidArgumentException for "+ element);
			} catch (JsonException e) {
				if (!(driver instanceof InternetExplorerDriver)) {
					throw e;
				}
				logger.info("InternetExplorerDriver: Dumping JsonException for "+ element);
			} catch (JavascriptException e) {
				if (!(driver instanceof InternetExplorerDriver)) {
					throw e;
				}
				logger.info("InternetExplorerDriver: Dumping JavascriptException for "+ element);
			}

			if (isEnabledCheck && (!element.isEnabled())) {
				throw new ElementNotSelectableException("Element is not enabled");
			}
			if (driver instanceof org.openqa.selenium.JavascriptExecutor) {
				((org.openqa.selenium.JavascriptExecutor)driver).executeScript(
						"arguments[0].style.border='4px solid green';",
						//                        "arguments[0].setAttribute('style', 'background: yellow; border: 4px solid red;')",
						element);
			}
			ThreadManager.setPreviousElement(element);

			Point elementLocation = element.getLocation();
			((JavascriptExecutor)driver).executeScript("window.scrollTo("
					+ "0,"+(elementLocation.getY() - 150)+");");
			return element;
		} else {
			throw new NoSuchElementException("Unable to find element by specified locators");
		}
	}

	/**
	 * return web element as per given locator type/value
	 * @param element
	 * @return
	 */
	public WebElement getWebElement(WebElement element) {
		try {
			return driver.findElement(getBy(element));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return element;
	}

	/**
	 * return list of web elements as per given locator type/value
	 * @param element
	 * @return
	 */
	public List<WebElement> getWebElements(WebElement element) {
		try {
				return driver.findElements(getBy(element));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * set soft assert instance
	 * @param softAssert
	 */
	public void setSoftAssert(SoftAssert softAssert) {
		ThreadManager.setSoftAssert(softAssert);
	}

	/**
	 * Closed web browser and quit the web
	 * driver instance
	 */
	public void quit() {
		driver.quit();
		ThreadManager.getDetailReport().addStep("quit", "Closed web browser and quit "
				+ "the web driver instance", Status.PASS);
	}

}
