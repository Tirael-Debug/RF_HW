package ru.soft.testing.hw;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class OpenPageTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @Before
    public void start() {
        System.setProperty("webdriver.chrome.driver", "..\\chromeDriver\\chromedriver.exe");
        driver = new ChromeDriver();
    }

    @Test
    public void openPageTest() {
        driver.get("https://yandex.ru/");
    }

    @After
    public void stop() {
        driver.quit();
        driver = null;
    }
}
