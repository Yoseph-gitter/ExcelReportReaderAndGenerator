package selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class BackgroundProcessMonitorPage {

    private static WebDriver driver = new ChromeDriver();


    private static String COLUMN_FOUR_ELEMENTS_XPATH = "//*[contains(@headers,'columnheader4')]";

    private static final String URL = "https://impl.wd12.myworkday.com/amazon910/d/gateway.htmld?reloadToken=49de8eb7e116f5bac9c79106c81e13a867e6944f4bcac036d8d611df6c7dcef5";

    public static void main(String[] args) {
        goToProcessMonitor();
    }

    public static void goToProcessMonitor() {
        driver.get(URL);
        WebElement[] elements = driver.findElements(By.xpath(COLUMN_FOUR_ELEMENTS_XPATH)).toArray(new WebElement[0]);
        System.out.println(elements.length);
    }
}
