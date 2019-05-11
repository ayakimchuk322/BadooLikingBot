package com.example.driver;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.lang.Nullable;

public class DriverWrapper {

    private WebDriver driver;

    public DriverWrapper(WebDriver driver) {
        this.driver = driver;
    }

    @Nullable
    public WebElement findElementByClassName(String className) {
        WebElement element = null;

        try {
            element = driver.findElement(By.className(className));
        } catch (NoSuchElementException e) {
            // simply ignore
        }

        return element;
    }

    @Nullable
    public WebElement findElementByClassName(WebElement parent, String className) {
        WebElement element = null;

        try {
            element = parent.findElement(By.className(className));
        } catch (NoSuchElementException e) {
            // simply ignore
        }

        return element;
    }
}
