import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SortCountryAndZoneTest {

    private WebDriver driver;

    private WebDriverWait wait;

    private void openPageTest() {
        driver.get("http://localhost/litecart/admin/login.php");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"box-login\"]/form/div[1]/table/tbody/tr[1]/td[2]/span/input")));

        WebElement loginInput = driver.findElement(By.xpath("//*[@id=\"box-login\"]/form/div[1]/table/tbody/tr[1]/td[2]/span/input"));
        WebElement passwordInput = driver.findElement(By.xpath("//*[@id=\"box-login\"]/form/div[1]/table/tbody/tr[2]/td[2]/span/input"));

        loginInput.sendKeys("admin");
        passwordInput.sendKeys("admin");

        driver.findElement(By.xpath("//*[@id=\"box-login\"]/form/div[2]/button")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"chart-sales-monthly\"]/canvas[7]")));
        Assert.assertNotNull(driver.findElement(By.xpath("//*[@id=\"chart-sales-monthly\"]/canvas[7]")));
    }

    private boolean listIsSorted(List<String> list) {
        List<String> sortedLIst = new ArrayList<>(list);
        Collections.sort(sortedLIst);
        for (int i=0; i < list.size(); i++) {
            if (!list.get(i).equals(sortedLIst.get(i))) {
                return false;
            }
        }
        return true;
    }

    @Before
    public void start() {
        System.setProperty("webdriver.chrome.driver", "C:\\selenium\\chromedriver.exe");
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, 10);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        openPageTest();
    }

    @Test
    public void checkCountryOrderTest() {
        driver.get("http://localhost/litecart/admin/?app=countries&doc=countries");
        List<WebElement> tableRowsList = driver.findElements(By.cssSelector(".row td"));
        List<String> countriesLink = new ArrayList<>();
        List<String> countryStr = new ArrayList<>();
        List<String> countryZone = new ArrayList<>();

        for (WebElement webElement : tableRowsList) {
            if (webElement.getAttribute("cellIndex").equals("4")) {
                countriesLink.add(webElement.findElement(By.tagName("a")).getAttribute("href"));
                countryStr.add(webElement.getText());
            } else if (webElement.getAttribute("cellIndex").equals("5")) {
                countryZone.add(webElement.getText());
            }
        }

        if (!listIsSorted(countryStr)) {
            Assert.fail();
        }

        for (int i = 0; i < countryZone.size(); i++) {
            System.out.println("Country: " + countryStr.get(i) + " with " + countryZone.get(i) + " zones");
            List<String> zonesList = new ArrayList<>();

            if (!(countryZone.get(i).equals("0"))) {
                driver.findElement(By.xpath("//a[@href ='" + countriesLink.get(i) + "']")).click();
                List<WebElement> list = driver.findElements(By.cssSelector("#table-zones tr td"));

                for (WebElement zone : list) {
                    if (zone.getAttribute("cellIndex").equals("2")
                            && (zone.findElement(By.tagName("input")).getAttribute("type").equals("hidden"))) {
                        zonesList.add(zone.getText());
                    }
                }

                if (!listIsSorted(zonesList) || zonesList.size() == 0) {
                    Assert.fail();
                }

                driver.get("http://localhost/litecart/admin/?app=countries&doc=countries");
            }
        }
    }

    @Test
    public void checkGeoZonesOrderTest() {
        driver.get("http://localhost/litecart/admin/?app=geo_zones&doc=geo_zones");
        List<WebElement> countries = driver.findElements(By.cssSelector(".dataTable .row a:not([title])"));

        for (int i = 0; i < countries.size(); i++) {
            WebElement link = countries.get(i);
            link.click();
            List<String> zonesStr = new ArrayList<>();
            List<WebElement> countyZones = driver.findElements(By.cssSelector("#table-zones td select[name *= zone_code]"));
            for (WebElement zone : countyZones) {
                List<WebElement> zoneOptions = zone.findElements(By.tagName("option"));
                for (WebElement option : zoneOptions) {
                    String selectedOption = option.getAttribute("selected");
                    if ("true".equals(selectedOption)) {
                        zonesStr.add(option.getText());
                    }
                }
            }

            if (!listIsSorted(zonesStr)) {
                Assert.fail();
            }

            driver.get("http://localhost/litecart/admin/?app=geo_zones&doc=geo_zones");
            countries = driver.findElements(By.cssSelector(".dataTable .row a:not([title])"));
        }
    }

    @After
    public void stop() {
        driver.quit();
        driver = null;
    }
}
