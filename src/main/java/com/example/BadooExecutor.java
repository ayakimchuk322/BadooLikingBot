package com.example;

import com.example.config.SeleniumConfig;
import com.example.driver.DriverWrapper;
import com.example.util.Utils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BadooExecutor implements Executor {

    @Autowired
    private SeleniumConfig seleniumConfig;

    @Value("${BADOO_USER}")
    String badooUser;

    @Value("${BADOO_PASSWORD}")
    String badooPassword;

    @Override
    public void executeSite() {
        login();
        maximizeWindow();
        swipeEncounters();
    }

    @Override
    public void closeSite() {
        closeWindow();
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

    private void maximizeWindow() {
        driver().manage().window().maximize();
    }

    private void swipeEncounters() {
//        for (int i = 0; i < 50; i++) { // TODO: get back after debugging
        for (int i = 0; i < 5; i++) {
            Utils.sleepCurrentThread(1500);

            swipeSingleEncounter();

            Utils.sleepCurrentThread(1500);
        }
    }

    private void swipeSingleEncounter() {
        if (!isVerified() && !isOnline()) {
            skipAtTheBottom();
        }

        if (!profileHasMoreThanOnePhoto()) {
            skipAtTheBottom();
        }

        goToProfile();

        if (!isRightProfile()) {
            skitAtTheTop();
        }

        likeAtTheTop();
    }

    private boolean isVerified() {
        WebElement verifiedIcon = wrappedDriver().findElementByClassName("verify-icon");
        return verifiedIcon != null;
    }

    private boolean isOnline() {
        WebElement profileHeader = driver().findElement(By.className("profile-header__online-status"));
        WebElement onlineIcon = wrappedDriver().findElementByClassName(profileHeader, "online-status--online");
        return onlineIcon != null;
    }

    private boolean profileHasMoreThanOnePhoto() {

        return false;
    }


    private boolean isRightProfile() {
        return isSameLocation()
                && hasNoKids()
                && !infoContainsBlackListedWords();
    }

    private boolean isSameLocation() {

        return false;
    }

    private boolean infoContainsBlackListedWords() {

        return true;
    }

    private boolean hasNoKids() {

        return false;
    }

    private void goToProfile() {

    }

    private void skipAtTheBottom() {
        WebElement skip = driver().findElement(By.className("profile-action--no"));
        skip.click();
    }

    private void skitAtTheTop() {

    }

    private void likeAtTheTop() {

    }

    private void closeWindow() {
        driver().close();
    }

    private WebDriver driver() {
        return seleniumConfig.getWebDriver();
    }

    private DriverWrapper wrappedDriver() {
        return seleniumConfig.getWrappedDriver();
    }
}
