package com.ttn.WebAutomation.tests.tsp;

import com.ttn.WebAutomation.base.BaseLib;
import com.ttn.WebAutomation.pageObjects.Sample;
import com.ttn.WebAutomation.pageObjects.Slack;
import com.ttn.WebAutomation.seleniumUtils.CommonUtility;
import com.ttn.WebAutomation.seleniumUtils.SeleniumHelper;
import com.ttn.WebAutomation.utillib.ExcelWriter;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.testng.annotations.Test;

public class demoTest {
    @Test(priority = 0)
    public void clicklogin() throws Exception
    {
        WebDriver driver = BaseLib.getDriver();
        CommonUtility cu = new CommonUtility(driver);
        SeleniumHelper helper = new SeleniumHelper(driver);
        Sample obj=new Sample(helper);
        obj.demo();

    }

}
