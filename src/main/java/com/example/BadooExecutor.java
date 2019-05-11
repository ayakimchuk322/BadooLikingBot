package com.example;

import com.example.config.SeleniumConfig;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BadooExecutor implements Executor {

    @Autowired
    private SeleniumConfig seleniumConfig = new SeleniumConfig();

    @Value("${BADOO_USER}")
    String badooUser;

    @Value("${BADOO_PASSWORD}")
    String badooPassword;

    @Override
    public void executeSite() {
        login();
    }

    private void login() {
        driver().get("https://badoo.com/signin/?f=top");

        WebElement loginInput = driver().findElement(By.className("js-signin-login")); // TODO: move to constants
        loginInput.sendKeys(badooUser);

        WebElement passwordInput = driver().findElement(By.className("js-signin-password"));
        passwordInput.sendKeys(badooPassword);

        WebElement submit = driver().findElement(By.className("sign-form__submit"));
        submit.submit();
    }

    public void closeWindow() {
        driver().close();
    }

    private WebDriver driver() {
        return seleniumConfig.getWebDriver();
    }
}
