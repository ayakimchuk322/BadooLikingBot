package com.example.config;

import com.example.driver.DriverWrapper;
import javax.annotation.PostConstruct;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SeleniumConfig {

    @Value("${SELENIUM_CHROME_DRIVER_LOCATION}")
    private String chromeDriverLocation;

    private WebDriver webDriver;


    @PostConstruct
    public void initializeWebDriver() {
        System.setProperty("webdriver.chrome.driver", chromeDriverLocation);
        webDriver = new ChromeDriver();
    }

    public WebDriver getWebDriver() {
        return webDriver;
    }

    public DriverWrapper getWrappedDriver() {
        return new DriverWrapper(webDriver);
    }
}
