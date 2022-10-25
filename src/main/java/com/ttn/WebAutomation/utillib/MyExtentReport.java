package com.ttn.WebAutomation.utillib;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.ttn.WebAutomation.base.BaseLib;
import org.testng.ITestContext;

import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyExtentReport {
    public ExtentTest extentTest;
    private ExtentReports extent;

    //Functions
    public void setUpReport(ITestContext testContext, String browser) {
        ExtentHtmlReporter htmlReporter;
        DateFormat dateformat = new SimpleDateFormat("dd_MM_yy_HH_mm_ss");
        Date date = new Date();
        String timeStamp = dateformat.format(date);
       
        extent = new ExtentReports();
        htmlReporter = new ExtentHtmlReporter(System.getProperty("user.dir") + "/Reports/extentReport/ExtentReport_" + timeStamp + "_"+ testContext.getName() + ".html");
        extent.attachReporter(htmlReporter);
        // To add system or environment info by using the setSystemInfo method.
        extent.setSystemInfo("OS", BaseLib.OS);
        extent.setSystemInfo("Browser", browser);
       
        htmlReporter.config().setDocumentTitle("Extent Report");
        htmlReporter.config().setReportName("Test Report");
      
        htmlReporter.config().setTheme(Theme.STANDARD);
        htmlReporter.config().setTimeStampFormat("EEEE, MMMM dd, yyyy, hh:mm a '('zzz')'");
    }

    public void createTest(ITestContext testContext, String browser, Method method, String className) {
        extentTest = extent.createTest("(" + browser + " - " + testContext.getName() + ") "
                + method.getName(), className);
    }


    public void flushReport() {
        extent.flush();
    }
}
