package com.ttn.WebAutomation.base;

/**
 * This Java program demonstrates the Base Class of Framework.
 *
 * @author TTN
 */

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.ttn.WebAutomation.seleniumUtils.DetailReport;
import com.ttn.WebAutomation.seleniumUtils.FrameworkException;
import com.ttn.WebAutomation.seleniumUtils.ThreadManager;
import com.ttn.WebAutomation.seleniumUtils.WaitStatementLib;
import com.ttn.WebAutomation.utillib.GetPropertyValues;
import com.ttn.WebAutomation.utillib.GlobalVariables;
import com.ttn.WebAutomation.utillib.JiraOperations;
import com.ttn.WebAutomation.utillib.PropertyReader;
import com.ttn.WebAutomation.utillib.TestLinkUtil;
import com.ttn.WebAutomation.utillib.Utility;

public abstract class BaseLib {
	public static ITestContext mContext;

	protected static ThreadLocal<ExtentTest> testLevelLogger = new ThreadLocal<ExtentTest>();
	protected static ThreadLocal<ExtentTest> classLevelLogger = new ThreadLocal<ExtentTest>();
	protected static ThreadLocal<ExtentTest> methodLevelLogger = new ThreadLocal<ExtentTest>();
	public static ThreadLocal<EventFiringWebDriver> driver = new ThreadLocal<EventFiringWebDriver>();
	public static ThreadLocal<ExtentTest> testReport = new ThreadLocal<ExtentTest>();
	public static ThreadLocal<Integer> testLinkID = new ThreadLocal<Integer>();
	public static ThreadLocal<RemoteWebDriver> remoteDriver =  new ThreadLocal<RemoteWebDriver>();
	public static ExtentHtmlReporter htmlReporter;
	public static ExtentReports extent;
	public static ExtentTest test;
	public static Logger log;
	public static Properties properties;
	public static String OS = System.getProperty("os.name");
	public static String URL;
    public static String packageName;
	public static WebDriver getDriver() {
		return driver.get();
	}
	public static RemoteWebDriver getDriver1() {
		return remoteDriver.get();
	}
	public static void setRemoteWebDriver(RemoteWebDriver driver2) {
		remoteDriver.set(driver2);
	}
	public static void setWebDriver(WebDriver driver1) {

		driver.set((EventFiringWebDriver) driver1);
	}
	public static String globalEnvironment;

	@Parameters({ "Environment", "browser" })
	@BeforeSuite
	public void beforeSuite(String environment, String browser, ITestContext context)
			throws FrameworkException, MalformedURLException {
		try {
			PropertyConfigurator.configure(GlobalVariables.getPropertiesPath() + GlobalVariables.LOG4J_FILE_NAME);
		} catch (IOException e1) {

			e1.printStackTrace();
		}
		logger.info(">>>>>>>>>> Starts of " + "BeforeSuite" + " <<<<<<<<<<");
		GlobalVariables.RESULT_BASE_LOCATION = Utility.generateUniqueString();
		try {
			detailReport = new DetailReport(GlobalVariables.RESULT_BASE_LOCATION);
			ThreadManager.setDetailReport(detailReport);
			extent = ExtentManager.getReporter(browser, environment, context.getCurrentXmlTest().getSuite().getName());

			properties = PropertyReader.setup_properties();
			String jirastatus = properties.getProperty("jira_status");
			logger.info("The status of jira is " + jirastatus);
			if (jirastatus.equalsIgnoreCase("YES")) {
				JiraOperations.createJiraInstance(properties.getProperty(GlobalVariables.JIRA_URL),
						properties.getProperty(GlobalVariables.JIRA_USERNAME),
						properties.getProperty(GlobalVariables.JIRA_PASSWORD));
				logger.info("Jira connection done");
			} else {
				logger.info("Jira connection is not required");
			}
		} catch (Exception e) {
			logger.info("------Error------" + e.getMessage());
		}
		// TestLink Configuration
		String testlinkstatus = properties.getProperty("testlink_status");
		logger.info("The status of TestLink is " + testlinkstatus);

		if (testlinkstatus.equalsIgnoreCase("YES")) {
			TestLinkUtil.setTestPlan();
			logger.info("TestLink connection done");

		} else {
			logger.info("TestLink connection is not required");
		}
		logger.info("Inside Before Suite");
	}
	public static ExtentTest parentTest = null;

	@Parameters({ "packageName", "Environment", "browser" })
	@BeforeTest
	public void preTestCondition(String packageName, String environment, String browser, ITestContext testContext) {
		logger.info(">>>>>>>>>> Starts of " + "BeforeTest" + " <<<<<<<<<<");
		globalEnvironment = environment;
		BaseLib.packageName = packageName;
		new GetPropertyValues();
		URL = GetPropertyValues.getEnvironmentProperty("testURL");
		parentTest = extent.createTest(packageName);
		testLevelLogger.set(parentTest);
		logger.info(">>>>>>>>>> Ends of " + "BeforeTest" + " <<<<<<<<<<");
	}

	public static ExtentTest testLevel = null;

	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		logger.info(">>>>>>>>>> Start of " + "BeforeClass" + " <<<<<<<<<<");
		String className = getClass().getSimpleName();
		testLevel = testLevelLogger.get()
				.createNode("<b>" + "<font color=" + "blue>" + className + "</font>" + "</b >");

		classLevelLogger.set(testLevel);
		logger.info(">>>>>>>>>> Ends of " + "BeforeClass" + " <<<<<<<<<<");
	}

	@AfterClass(alwaysRun = true)
	public void afterClass() {
		logger.info(">>>>>>>>>> Start of " + "AfterClass" + " <<<<<<<<<<");
		String className = getTestLogger().toString();

	}

	@Parameters({ "browser" ,"Port" })
	@BeforeMethod
	public void preReportSetUp(String browser, Method method, ITestContext testContext,String Port) {
		logger.info(">>>>>>>>>> Starts of " + "TC_" + method.getName().split("_")[0] + " <<<<<<<<<<");
		browser = browser.toUpperCase();
		try {
			driver.set(new BrowserFactory().getSingletonBrowser(browser,Port));
			WaitStatementLib.implicitWaitForSeconds(driver.get(), 20);
			driver.get().manage().window().maximize();
			driver.get().navigate().to(URL);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Reporter.log("(" + testContext.getName() + ") " + "Navigating to URL in " + browser + " : " + URL, true);
		WaitStatementLib.implicitWaitForSeconds(driver.get(), 20);
		softAssert = new SoftAssert();
		ThreadManager.setSoftAssert(softAssert);
		detailReport = new DetailReport(GlobalVariables.RESULT_BASE_LOCATION);
		ThreadManager.setDetailReport(detailReport);
		detailReport.setTestId("TC_" + method.getName().split("_")[0]);
		detailReport.setStepCounter(1);

	}

	@AfterMethod
	public void reportClosure(ITestResult result) {
		logger.info(">>>>>>>>>> Start of @AfterMethod <<<<<<<<<<");
		if (result.getStatus() == ITestResult.FAILURE) {
			getTestLogger().log(Status.FAIL,
					MarkupHelper.createLabel(result.getName() + " - Test Case Failed", ExtentColor.RED));
			getTestLogger().log(Status.FAIL,
					MarkupHelper.createLabel(result.getThrowable() + " - Test Case Failed", ExtentColor.RED));
			GlobalVariables.TEST_RESULT_COUNT.add("fail");
		} else if (result.getStatus() == ITestResult.SKIP) {
			getTestLogger().log(Status.SKIP,
					MarkupHelper.createLabel(result.getName() + " - Test Case Skipped", ExtentColor.ORANGE));
			GlobalVariables.TEST_RESULT_COUNT.add("skip");
		} else if (result.getStatus() == ITestResult.SUCCESS) {
			getTestLogger().log(Status.PASS,
					MarkupHelper.createLabel(result.getName() + " Test Case PASSED", ExtentColor.GREEN));
			GlobalVariables.TEST_RESULT_COUNT.add("pass");
		}
		try {
			driver.get().quit();
			extent.flush();
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
		logger.info(">>>>>>>>>> Ends of " + detailReport.getTestId() + " <<<<<<<<<<");
	}

	@AfterTest
	public void getResult(ITestContext testContext) {
		logger.info(">>>>>>>>>> Start of @AfterTest <<<<<<<<<<");
		extent.flush();
		logger.info(">>>>>>>>>> End of @AfterTest <<<<<<<<<<");
	}

	@AfterSuite
	public void afterSuite() throws IOException {
		logger.info(">>>>>>>>>> Start of @AfterSuite <<<<<<<<<<");
		ExtentManager.getReporter("", "","").flush();
		try {
			if (GetPropertyValues.getGenericProperty("emailSend").equalsIgnoreCase("Yes")) {
				Utility.SendMailWithAttachment();
			}
		} catch (Exception e) {
			logger.info("Error in afterSuite" + e);
		}
		logger.info(">>>>>>>>>> Start S3 <<<<<<<<<<");
		logger.info(">>>>>>>>>> "+DetailReport.getReportLocation()+" <<<<<<<<<<");

		// S3 Code
		String s3status = properties.getProperty("s3_status");
		if (s3status.equalsIgnoreCase("YES")) {
			Utility.dumpReportInS3(DetailReport.getReportLocation());
			logger.info("Report is dump into S3");
		} else {
			logger.info("Report is not dump into S3");

		}
	}

	final public void processError(Throwable t) throws Throwable {
		logger.info("Error while running the test." + t);
		throw t;
	}

	public static ExtentTest getTestLogger() {
		return methodLevelLogger.get();
	}

	public static DetailReport detailReport;
	protected SoftAssert softAssert;

	protected static Logger logger = LoggerFactory.getLogger(BaseLib.class);
}