package com.ttn.WebAutomation.utillib;

import com.ttn.WebAutomation.base.BaseLib;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestResult;
import org.testng.Reporter;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenshotAs {
    ITestResult result;
    Logger actiLog;
    public ScreenshotAs(ITestResult result)
    {
        this.result = result;
        actiLog=Logger.getLogger(this.getClass());
    }
    public String getScreenshot()
    {
        BaseLib obj = (BaseLib) result.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("YY_MM_dd_hh_mm_ss");
        Date date = new Date();
        String fileName = result.getName();
        String  testName = result.getTestContext().getName();
        // Taking Screenshots
        TakesScreenshot screenshots = (TakesScreenshot) obj.driver.get();
        File srcFile = screenshots.getScreenshotAs(OutputType.FILE);

        String screenshotPath = System.getProperty("user.dir") + "/Screenshots_FailedTests/" + dateFormat.format(date)+"_"  + fileName+"_" + testName + "_" + ".png";
        File destFile = new File(screenshotPath);

        try {
            FileUtils.copyFile(srcFile, destFile);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            Reporter.log("Not able to take the screenshot.",true);
            e.printStackTrace();
            actiLog.error("Error: " + e);
        }
        return screenshotPath;
    }
}
