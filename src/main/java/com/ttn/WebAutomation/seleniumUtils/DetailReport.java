package com.ttn.WebAutomation.seleniumUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.ttn.WebAutomation.base.BaseLib;
import com.ttn.WebAutomation.seleniumUtils.FrameworkException;
import com.ttn.WebAutomation.seleniumUtils.SeleniumHelper;
import com.ttn.WebAutomation.utillib.GetPropertyValues;
import com.ttn.WebAutomation.utillib.GlobalVariables;


/**
 * this class creates the test detail report
 * @author TTN
 *
 */

public class DetailReport extends BaseLib{
    
	/**
     * add test step with given detail
     * @param stepName
     * @param description
     * @param status
     */
    public void addStep(String stepName, String description,
            Status status)  {
        addStep(stepName, description, null, null, status);
    }
       
    /**
     * add test step report with given information
     * @param stepName
     * @param description
     * @param expectedResult
     * @param actualResult
     * @param status
     */
    public void addStep(String stepName, String description,
            String expectedResult, String actualResult, Status status) {
        String currentDateTimeString = GlobalVariables.simpleDateFormat.format(
                new Date());
        expectedResult = (StringUtils.isEmpty(expectedResult)) 
                ? "" : expectedResult;
        actualResult = (StringUtils.isEmpty(actualResult)) 
                ? "" : actualResult;

        try {
            // take the screenshot if applicable
			if ((BaseLib.getDriver() != null
					&& (GetPropertyValues.getGenericProperty("snapshotLevel").equalsIgnoreCase("all")
							|| GetPropertyValues.getGenericProperty("snapshotLevel").equalsIgnoreCase(status.toString())))) {
				/*String snapshotFileName = reportLocation + "/" + testId + "/" + stepCounter + ".png";*/
                String snapshotFileName = reportLocation + "/" + testId + "/" + stepCounter + ".png";
                String extentReportSnapshotFileName = testId + "/" + stepCounter + ".png";
//
                File snapshotFile = ((TakesScreenshot) BaseLib.getDriver()).getScreenshotAs(OutputType.FILE);
				try {
					FileUtils.copyFile(snapshotFile, new File(snapshotFileName));
				} catch (IOException e) {
					logger.log(Level.INFO, "Error occured, while creating the snapshot file" + snapshotFileName, e);
				}
				if(status.toString().equalsIgnoreCase("fail")) {
					if (!expectedResult.equals("") && !actualResult.equals("")) {
                        getTestLogger().fail("<b>" + stepName +"</b>"+ "<BR>"+ description + "<BR>"+ "actualResult=" + actualResult
								+ "<BR>"+ "expectedResult="+  expectedResult,
                                MediaEntityBuilder.createScreenCaptureFromPath(extentReportSnapshotFileName).build());
					} else {
                        getTestLogger().fail("<b>" + stepName +"</b>" + "<BR>"+ description + "<BR>",
                                MediaEntityBuilder.createScreenCaptureFromPath(extentReportSnapshotFileName).build());
					}
				} else if(status.toString().equalsIgnoreCase("pass")) {
					if (!expectedResult.equals("") && !actualResult.equals("")) {
                        getTestLogger().pass("<b>" + stepName +"</b>" + "<BR>"+ description + "<BR>"+ "actualResult=" + actualResult
								+ "<BR>"+ "expectedResult="+  expectedResult,
                                MediaEntityBuilder.createScreenCaptureFromPath(extentReportSnapshotFileName).build());
					} else {
                        getTestLogger().pass("<b>" + stepName +"</b>" + "<BR>"+ description + "<BR>",
                                MediaEntityBuilder.createScreenCaptureFromPath(extentReportSnapshotFileName).build());
					}
				}
				stepCounter++;
            } else {
            	if (!expectedResult.equals("") && !actualResult.equals("")) {
                    getTestLogger().log(status, "<b>" + stepName +"</b>" + "<BR>"+ description + "<BR>"+ "actualResult=" + actualResult
							+ "<BR>"+ "expectedResult="+  expectedResult);
				} else {
                    getTestLogger().log(status, "<b>" + stepName +"</b>" + "<BR>"+ description + "<BR>");
				}
            }
        } catch (Exception e) {
            logger.log(Level.INFO, "Error occured, while adding step: " 
                    + stepName + "(" + description + ")", e);
        }
//        stepCounter++;
    }
    
    /**
     * set the test case id
     * @param testId
     */
    public void setTestId(String testId) {
        this.testId = testId;
    }
    /**
     * get the test case id
     * @return
     */
    public static String getTestId() {
        return testId;
    }
     /**
     * gets the count of failed test steps
     * @return
     */
    public int getNumberOfFailedSteps() {
        return numberOfFailedSteps;
    }
    
    /**
     * gets the test report folder name
     * from the global.properties file
     * @return
     * @throws FrameworkException
     */
    public static String getReportLocation() throws FrameworkException {
        return reportLocation;
    }
    
    /**
     * set selenium-helper instance
     * @param seleniumHelper
     */
    public void setSeleniumHelper(SeleniumHelper seleniumHelper) {
        this.seleniumHelper = seleniumHelper;
    }
    
    
    public void setReportLocation(String uniqueFolderName) {
       /* reportLocation = GlobalConfiguration.getDynamicProperty(
                "reportFolder") + "/" + uniqueFolderName;*/
    	 reportLocation = System.getProperty("user.dir") + "/Automation Reports"+ "/" + uniqueFolderName;
    }
    

    /**
     * parameterized constructor to create test 
     * detail report with unique folder name
     * 
     * @param uniqueFolderName
     * @throws FrameworkException
     */
    public DetailReport(String uniqueFolderName)
            throws FrameworkException {
        File automationReportsFolder = new File(System.getProperty("user.dir") + "/Automation Reports");
        File OutputFolder = new File(System.getProperty("user.dir") + "/Automation Reports" + "/" + uniqueFolderName);
        createDirectory(automationReportsFolder);
        createDirectory(OutputFolder);
        reportLocation = System.getProperty("user.dir") + "/Automation Reports" + "/" + uniqueFolderName;
    }

    private void createDirectory(File directory) {
        if (!directory.exists()) {
            if (directory.mkdir()) {
                logger.log(Level.INFO, directory + " =>Directory is created!");
            } else {
                logger.log(Level.INFO, directory + " =>Failed to create Directory");
                throw new FrameworkException();
            }
        } else {
            logger.log(Level.INFO, directory + " =>Directory already exists");
        }
    }
    public DetailReport() {
    }
    
    private Logger logger = Logger.getLogger(DetailReport.class.getName());
    
    private int numberOfFailedSteps;
    
    private static String reportLocation;
    
    private SeleniumHelper seleniumHelper;
    
    private int stepCounter = 1;

	public int getStepCounter() {
		return stepCounter;
	}

	public void setStepCounter(int stepCounter) {
		this.stepCounter = stepCounter;
	}
	
	private static String testId;
    
    private String testStatus = "Passed";
}