package com.ttn.WebAutomation.pageObjects;

import com.epam.ta.reportportal.ws.annotations.In;
import com.ttn.WebAutomation.base.BaseLib;
import com.ttn.WebAutomation.seleniumUtils.CommonUtility;
import com.ttn.WebAutomation.seleniumUtils.SeleniumHelper;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.asserts.SoftAssert;

import java.util.List;

public class Case2 {
    private WebDriver driver;
    private SeleniumHelper helper;
    private ThreadLocal<EventFiringWebDriver> driver2;
    private static final Logger logger = Logger.getLogger(Slack.class);
    public static SoftAssert s_assert = new SoftAssert();


    CommonUtility commonutility;

    public Case2(SeleniumHelper helper) {
        this.driver = BaseLib.getDriver();
        this.helper = helper;
        PageFactory.initElements(driver, this);
    }

    @FindBy(how = How.XPATH, using = "//div[@class='nav-search-scope nav-sprite']")
    private WebElement categoriesDropdown;

    @FindBy(how = How.XPATH, using = "//input[@id='nav-search-submit-button']")
    private WebElement gobutton;

    @FindBy(how = How.XPATH, using = "//a[@href='/gp/bestsellers/?ref_=nav_cs_bestsellers']")
    private WebElement bestsellers;


    public void additems() throws InterruptedException {
        String[] url=new String[] {"baby", "luggage", "beauty", "books", "automative", "apparel", "computers", "electronics", "gift-cards", "garden", "pet-supplies", "grocery", "kitchen"};
        String baseUrl = "https://www.amazon.in/gp/bestsellers/";
        double totalamount = 0;
        int i =0;
        while(totalamount<4900)
        {
            driver.get(baseUrl+url[i]);
            List<WebElement> items = driver.findElements(By.className("_cDEzb_p13n-sc-price_3mJ9Z"));

            for(WebElement iterator:items)
            {
                String priceraw = iterator.getText();
                String priceformatted = priceraw.replaceAll(",","");
                String finalprice = priceformatted.substring(1, priceformatted.indexOf('.'));

                int priceOfItem = Integer.parseInt(finalprice);

                if(priceOfItem+totalamount>5000)
                {
                    continue;
                }


                if(priceOfItem<500 && priceOfItem>490)
                {
                    iterator.click();
                    System.out.println(driver.getTitle());
                    driver.navigate().refresh();
                    String producttitle = driver.findElement(By.xpath("//span[@id='productTitle']")).getText();
                    System.out.println("Product title is: "+producttitle);
                    driver.findElement(By.xpath("//input[@id='add-to-cart-button']")).click();
                    helper.pause("2");

                    break;
                }
            }
            i++;
        }
    }


}

