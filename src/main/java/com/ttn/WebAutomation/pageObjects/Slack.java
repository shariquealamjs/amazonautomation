package com.ttn.WebAutomation.pageObjects;
import com.ttn.WebAutomation.base.BaseLib;
import com.ttn.WebAutomation.seleniumUtils.CommonUtility;
import com.ttn.WebAutomation.seleniumUtils.SeleniumHelper;
import com.ttn.WebAutomation.utillib.ExcelWriter;
import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.testng.asserts.SoftAssert;
import org.openqa.selenium.support.ui.Select;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.util.List;
import java.util.Set;

public class Slack extends BaseLib {

    private WebDriver driver;
    private SeleniumHelper helper;
    private ThreadLocal<EventFiringWebDriver> driver2;
    private static final Logger logger = Logger.getLogger(Slack.class);
    public static SoftAssert s_assert = new SoftAssert();


    CommonUtility commonutility;

    public Slack(SeleniumHelper helper) {
        this.driver = BaseLib.getDriver();
        this.helper = helper;
        PageFactory.initElements(driver, this);

    }

    String data[]=new String[1000];
    String ouput[]=new String[1000];




    @FindBy(how = How.XPATH, using = "//button[@data-testid='create-copy']")
    private WebElement createcopybutton;

    @FindBy(how = How.XPATH, using = "//button[normalize-space()='Copy All Results']")
    private WebElement copytoclipboard;

    @FindBy(how = How.XPATH, using = "//button[normalize-space()='Make More']")
    private WebElement makemorebutton;

    @FindBy(how = How.XPATH, using = "//textarea[@id='what-are-the-main-points-you-want-to-cover']")
    private WebElement blog_freestyle_sentence;

    @FindBy(how = How.XPATH, using = "//textarea[@id='what-sentence-would-you-like-to-rewrite']")
    private WebElement newsentencefield;

    @FindBy(how = How.XPATH, using = "//input[@id='identifierId']")
    private WebElement emailid;

    @FindBy(how = How.XPATH, using = "//span[normalize-space()='Next']")
    private WebElement next;

    @FindBy(how = How.XPATH, using = "//input[@name='password']")
    private WebElement password;

    @FindBy(how = How.XPATH, using = "//input[@id='enter-your-email']")
    private WebElement enter_your_email;

    @FindBy(how = How.XPATH, using = "//button[normalize-space()='Continue']")
    private WebElement continue_button;

    @FindBy(how = How.XPATH, using = "/html[1]/body[1]/div[7]/div[3]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/div[8]/div[1]/div[1]/div[3]/div[1]/table[1]/tbody[1]/tr[1]/td[4]/div[2]/span[1]/span[1]")
    private WebElement lastemail;

    @FindBy(how = How.XPATH, using = "//a[normalize-space()='Click here']")
    private WebElement click_here_button;

    @FindBy(how = How.XPATH, using = "//span[normalize-space()='Sentence Rewriter']")
    private WebElement sentence_rewriter_project;

    @FindBy(how = How.XPATH, using = "//span[normalize-space()='Sentence Rewriter']")
    private WebElement blog_freestyle_project;

    @FindBy(how = How.XPATH, using = "//div[@class=' css-ackcql']")
    private WebElement tone;


    public void signin() throws InterruptedException
    {
        driver.get("https://app.copy.ai/login");
        helper.pause("15");


        String parent = driver.getWindowHandle();
        System.out.println("Parent: "+parent);


        enter_your_email.click();
        helper.pause("10");
        helper.enterData("Enter Your Email", enter_your_email,"socialmedia@ishield.ai");

        continue_button.click();

        helper.pause("45");

     /*

        driver.get("https://accounts.google.com/signin/v2/identifier?flowName=GlifWebSignIn&flowEntry=ServiceLogin");
        helper.enterData("Email-Id", emailid, "seleniumcopyaitest");
        next.click();
        password.click();
        helper.enterData("Password",password,"MNBVCXz@7");
        helper.pause("4");
        next.click();
        helper.pause("90");

        for(String handle: driver.getWindowHandles())
        {
            if(!handle.equalsIgnoreCase(parent))
            {
                driver.switchTo().window(handle);
                driver.close();
            }
        }
        driver.switchTo().window(parent);

         */

        /* To Open the Sentence Rewriter Project*/
        helper.pause("3");
        sentence_rewriter_project.click();
        helper.pause("3");

        /* To Open the Blog Freestyle Project*/

        /*
        blog_freestyle_project.click();
        helper.pause("5");
         */

    }





    public void senddatafromcsv() throws Exception
    {
        ExcelWriter x =new ExcelWriter("/home/ttn/Desktop/copy.ai/file.xlsx");
        boolean flag =x.isSheetExist("Sheet1");
        System.out.println(flag);
        int rows=x.noofrows();
        for(int i=1;i<(rows+1);i++)
        {

            try
            {
                data[i] = x.getCellData("Sheet1", 0, i);

                newsentencefield.click();
                helper.enterData("newsentencefield", newsentencefield, data[i]);
                helper.pause("2");
                createcopybutton.click();

                helper.pause("30");
                copytoclipboard.click();
                helper.pause("2");
                ouput[i] = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
                System.out.println(ouput[i]);
                x.setCellDataColNo("Sheet1", 1, i, ouput[i]);

                double time = (Math.random() * 60);
                helper.pause(String.valueOf(time));



            }


            catch (Exception xyz)
            {
                x.setCellDataColNo("Sheet1", 2, i, "Automation Failed");
                System.out.println(xyz);
            }
        }
    }


}
