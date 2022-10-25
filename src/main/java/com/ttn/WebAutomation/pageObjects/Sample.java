package com.ttn.WebAutomation.pageObjects;
import com.ttn.WebAutomation.base.BaseLib;
import com.ttn.WebAutomation.seleniumUtils.CommonUtility;
import com.ttn.WebAutomation.seleniumUtils.SeleniumHelper;
import com.ttn.WebAutomation.utillib.ExcelWriter;
import net.sf.json.util.JSONUtils;
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
import java.util.Arrays;
import java.util.List;
import java.util.Set;


public class Sample {

    private WebDriver driver;
    private SeleniumHelper helper;
    private ThreadLocal<EventFiringWebDriver> driver2;
    private static final Logger logger = Logger.getLogger(Slack.class);
    public static SoftAssert s_assert = new SoftAssert();


    CommonUtility commonutility;

    public Sample(SeleniumHelper helper) {
        this.driver = BaseLib.getDriver();
        this.helper = helper;
        PageFactory.initElements(driver, this);
    }

    @FindBy(how = How.XPATH, using = "//input[@id='twotabsearchtextbox']")
    private WebElement searchbar;

    @FindBy(how = How.XPATH, using = "//div[@class='autocomplete-results-container']")
    private WebElement autocompletecontainer;

    @FindBy(how = How.XPATH, using = "//a[normalize-space()='Books']")
    private WebElement books;

    @FindBy(how = How.XPATH, using = "//div[@class='s-main-slot s-result-list s-search-results sg-row']/child::*")
    private List<WebElement> result;

    String url;

    public void demo() throws InterruptedException {

        boolean flag = false;
        char[] bookname = {'H', 'o', 'w', ' ', 'n', 'o', 't', ' ', 't', 'o', ' ', 'd', 'i', 'e',};
        driver.get("https://www.amazon.in/");
        books.click();
        helper.pause("2");
        searchbar.click();

        String val = "How not to die";
        for(int i=0; i<bookname.length;i++)
        {
            char c = val.charAt(i);
            String s = new StringBuilder().append(c).toString();
            searchbar.sendKeys(s);
            helper.pause("2");
            List<WebElement> items = autocompletecontainer.findElements(By.xpath("*"));
            for (WebElement x:items)
            {
                String text = x.getText();
                if (text.equalsIgnoreCase("How not to die"))
                {
                    x.click();
                    helper.pause("3");
                    url = driver.getCurrentUrl();
                    flag = true;
                    break;
                }

            }

            if (flag == true)
            {
                break;
            }
            else
            {
                continue;
            }


        }
    }

    public void displayResults()
    {
        driver.get(url);
        System.out.println("URL is:"+ url);
        helper.pause("2");
        //System.out.println(result.getText());
        System.out.println(result.size());
        int sizeofarray = result.size();

        int actualprice =0;
        int originalprice = 0;

        for (WebElement l: result)
        {

            int i =1;
            System.out.println(i);


            String text = l.getText();
            System.out.println(text);

            String[] result = text.split("\n");
            System.out.println(Arrays.toString(result));

            String[] paperback = new String[2];
            String[] kindle = new String[1];
            String[] hardcover = new String[2];
            String[] audiobook = new String[2];

            for(int j =0; j<result.length-2; j++)
            {
                switch (result[j])
                {
                    case "Paperback":
                        paperback[0] = result[j+1];
                        paperback[1] = result[j+2];
                        break;

                    case "Kindle Edition":
                        kindle[0] = result[j+1];
                        break;

                    case "Audible Audiobook":
                        audiobook[0] = result[j+1];
                        audiobook[1] = result[j+2];
                        break;

                    case "Hardcover":
                        hardcover[0] = result[j+1];
                        hardcover[1] = result[j+2];

                        break;
                }
            }

            System.out.println("Paperback Array="+Arrays.toString(paperback));
            System.out.println("Hardcover Array="+Arrays.toString(paperback));
            System.out.println("Kindle Array="+Arrays.toString(paperback));
            System.out.println("Audio Array="+Arrays.toString(paperback));

            int paperbackindex = 0;
            int hardcoverindex = 0;
            int kindleindex = 0;
            int audioindex = 0;


            for(String item: hardcover)
            {
                if(item != null)
                {
                    try{
                        String res[] = item.split(" ");
                        String  priceStr = res[0].substring(1);
                        String stt = priceStr.replaceAll(",","");
                        int a = Integer.parseInt(stt);
                        if(hardcoverindex == 0)
                        {
                            actualprice = actualprice+ a;
                            hardcoverindex++;
                        }
                        else if(hardcoverindex ==1)
                        {
                            originalprice = originalprice+a;
                        }
                        System.out.println(a);
                    }catch (Exception e)
                    {
                        System.out.println(e);
                    }

                }


            }

            for(String item: audiobook)
            {
                if(item != null)
                {
                    try{
                        String res[] = item.split(" ");
                        String  priceStr = res[0].substring(1);
                        String stt = priceStr.replaceAll(",","");
                        int a = Integer.parseInt(stt);
                        if(audioindex == 0)
                        {
                            actualprice = actualprice+ a;
                            audioindex++;
                        }
                        else if(audioindex ==1)
                        {
                            originalprice = originalprice+a;
                        }
                        System.out.println(a);
                    }catch (Exception e)
                    {
                        System.out.println(e);
                    }

                }


            }


            for(String item: paperback)
            {
                if(item != null)
                {
                    try{
                        String res[] = item.split(" ");
                        String  priceStr = res[0].substring(1);
                        String stt = priceStr.replaceAll(",","");
                        int a = Integer.parseInt(stt);
                        if(paperbackindex == 0)
                        {
                            actualprice = actualprice+ a;
                            paperbackindex++;
                        }
                        else if(paperbackindex ==1)
                        {
                            originalprice = originalprice+a;
                        }
                        System.out.println(a);
                    }catch (Exception e)
                    {
                        System.out.println(e);
                    }

                }


            }

            for(String item: kindle)
            {
                if(item != null)
                {
                    try{
                        String res[] = item.split(" ");
                        String  priceStr = res[0].substring(1);
                        String stt = priceStr.replaceAll(",","");
                        int a = Integer.parseInt(stt);
                        if(kindleindex == 0)
                        {
                            actualprice = actualprice+ a;
                            kindleindex++;
                        }
                        System.out.println(a);
                    }catch (Exception e)
                    {
                        System.out.println(e);
                    }

                }


            }



        }
        System.out.println("Original Price ="+originalprice);
        System.out.println("Actual Price ="+actualprice);
    }

}
