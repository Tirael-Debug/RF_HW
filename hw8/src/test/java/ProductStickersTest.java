import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.List;

public class ProductStickersTest {

    private WebDriver driver;

    private WebDriverWait wait;

    @Before
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "C:\\selenium\\chromedriver.exe");
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, 60);
    }

    @Test
    public void  checkStickers() {
        driver.get("http://localhost/litecart/");
        List<WebElement> products = driver.findElements(By.cssSelector(".product"));

        for (WebElement product: products) {
            List<WebElement> stickers = product.findElements(By.cssSelector(".sticker"));
            Assert.assertEquals(1, stickers.size());
        }
    }
}
