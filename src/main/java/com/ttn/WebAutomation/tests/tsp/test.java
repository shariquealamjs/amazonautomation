package com.ttn.WebAutomation.tests.tsp;

import com.ttn.WebAutomation.base.BaseLib;
import com.ttn.WebAutomation.pageObjects.Case2;
import com.ttn.WebAutomation.pageObjects.Sample;
import com.ttn.WebAutomation.seleniumUtils.CommonUtility;
import com.ttn.WebAutomation.seleniumUtils.SeleniumHelper;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

public class test extends BaseLib {
/*
    @Test(priority = 0)
    public void clicklogin() throws Exception
    {
        WebDriver driver = BaseLib.getDriver();
        CommonUtility cu = new CommonUtility(driver);
        SeleniumHelper helper = new SeleniumHelper(driver);
        Sample obj=new Sample(helper);

        obj.demo();
        helper.pause("3");
        obj.displayResults();
    }
 */
    @Test(priority = 0)
    public void casetwo() throws Exception
    {
        WebDriver driver = BaseLib.getDriver();
        CommonUtility cu = new CommonUtility(driver);
        SeleniumHelper helper = new SeleniumHelper(driver);
        Case2 obj=new Case2(helper);
        obj.additems();
    }

}
