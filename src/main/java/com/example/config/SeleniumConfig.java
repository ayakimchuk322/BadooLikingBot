package com.example.config;

import com.example.driver.DriverWrapper;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
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
        webDriver = new ChromeDriver();
    }

    public WebDriver getWebDriver() {
        return webDriver;
    }

    public DriverWrapper getWrappedDriver() {
        return new DriverWrapper(webDriver);
    }
}
