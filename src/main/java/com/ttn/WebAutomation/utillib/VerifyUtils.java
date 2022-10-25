package com.ttn.WebAutomation.utillib;

import java.io.File;
import java.io.IOException;

import com.ttn.WebAutomation.base.BaseLib;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.Assert;

//import com.relevantcodes.extentreports.LogStatus;
//import com.ttn.WebAutomation.base.BaseTest;

public class VerifyUtils extends BaseLib {

    private AssertionError error;
    private boolean passStatus;


    private VerifyUtils(AssertionError error) {
        this.passStatus = false;
        this.error = error;
    }

    private VerifyUtils() {
        this.passStatus = true;
    }


    public boolean isPassStatus() {
        return passStatus;
    }

    public void exitOnFailure() {
        log.info("Checking for Assertion errors to exit the test");
        if (error != null) {
            log.info("Exiting the test on Assertion failure as exitOnFail flag is set to True");
        
            throw error;
        }
    }

    private void reportFailure(boolean screenshotOnFailure, String description,
                               AssertionError e) {
        log.info(description + ": Assertion FAILED with exception " + e.getMessage());
        System.out.println(description + ": Fail");

        if (screenshotOnFailure) {
            String screenshotName = "image" + System.currentTimeMillis() + ".png";
            try {
                File src = ((TakesScreenshot) driver.get()).getScreenshotAs(OutputType.FILE);
                FileUtils.copyFile(src, new File(properties.getProperty(GlobalVariables.getExtentCongigPath()) + File.separator + screenshotName));
                log.info("Screenshot " + screenshotName + " taken at " + properties.getProperty(GlobalVariables.getExtentCongigPath()));

            } catch (IOException io) {

                log.info("Fail to take screenshot");
            }
          
           
        } else {
         
        }
    }

    /**
     * @param actual              actual object value
     * @param expected            expected object value
     * @param description         description for extent report
     * @param screenshotOnFailure take screenshot
     */
    public VerifyUtils verifyEquals(Object actual, Object expected, String description,
                                    boolean screenshotOnFailure) {
        try {
            Assert.assertEquals(actual, expected);
            log.info(description + ": Assertion is successful");
            System.out.println(description + ": Success");
         
            return new VerifyUtils();

        } catch (AssertionError e) {
            reportFailure(screenshotOnFailure, description, e);
            return new VerifyUtils(e);
        }
    }

    public VerifyUtils verifyNotEquals(Object actual, Object expected, String description,
                                       boolean screenshotOnFailure) {
        try {
            Assert.assertNotEquals(actual, expected);
            log.info(description + ": Assertion is successful");
            System.out.println(description + ": Success");
        
            return new VerifyUtils();

        } catch (AssertionError e) {
            reportFailure(screenshotOnFailure, description, e);
            return new VerifyUtils(e);
        }
    }

    /**
     * @param condition           condition to be verified
     * @param message             message shown on fail
     * @param description         description for extent report
     * @param screenshotOnFailure take screenshot
     */
    public VerifyUtils verifyTrue(boolean condition, String message, String description,
                                  boolean screenshotOnFailure) {
        try {
            Assert.assertTrue(condition, message);
            log.info(description + ": Assertion is successful");
            System.out.println(description + ": Success");
         
            return new VerifyUtils();

        } catch (AssertionError e) {
            reportFailure(screenshotOnFailure, description, e);
            return new VerifyUtils(e);
        }
    }

    public VerifyUtils verifyFalse(boolean condition, String message, String description,
                                   boolean screenshotOnFailure) {
        try {
            Assert.assertFalse(condition, message);
            log.info(description + ": Assertion is successful");
            System.out.println(description + ": Success");
        
            return new VerifyUtils();

        } catch (AssertionError e) {
            reportFailure(screenshotOnFailure, description, e);
            return new VerifyUtils(e);
        }
    }
}