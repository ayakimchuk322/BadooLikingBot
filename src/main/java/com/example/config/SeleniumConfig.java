package com.example.config;

import java.util.concurrent.TimeUnit;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.stereotype.Component;

@Component
public class SeleniumConfig {

    private WebDriver webDriver;


    static {
        // TODO: replace with env variable
        System.setProperty("webdriver.chrome.driver", "D:/dev/chrome_driver_74/chromedriver.exe");
    }


    @SuppressWarnings("deprecated")
    public SeleniumConfig() {
        DesiredCapabilities desiredCapabilities = DesiredCapabilities.chrome();
        webDriver = new ChromeDriver(desiredCapabilities);
        webDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
    }

    public WebDriver getWebDriver() {
        return webDriver;
    }
}
