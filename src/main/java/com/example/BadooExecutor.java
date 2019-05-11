package com.example;

import com.example.config.SeleniumConfig;
import com.example.driver.DriverWrapper;
import com.example.util.Utils;
import java.util.Arrays;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class BadooExecutor implements Executor {

    @Autowired
    private SeleniumConfig seleniumConfig;

    @Value("${BADOO_USER}")
    private String badooUser;

    @Value("${BADOO_PASSWORD}")
    private String badooPassword;

    @Value("${BADOO_CITY}")
    private String city;

    @Value("${BADOO_BLACK_LISTED_WORDS}")
    private String blackListedWords;

    private List<String> separatedBlackListedWords;


    public BadooExecutor checkPrerequisites() {
        if (StringUtils.isEmpty(badooUser)) {
            System.err.println("No badoo user provided; create environment variable BADOO_USER");
            System.exit(-1);
        }

        if (StringUtils.isEmpty(badooPassword)) {
            System.err.println("No badoo password provided; create environment variable BADOO_PASSWORD");
            System.exit(-2);
        }

        return this;
    }

    public BadooExecutor prepareExecution() {
        if (!StringUtils.isEmpty(blackListedWords)) {
            separatedBlackListedWords = Arrays.asList(blackListedWords.toLowerCase().split(","));
        }

        return this;
    }

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
            Utils.sleepCurrentThread(2000);

            swipeSingleEncounter();

            Utils.sleepCurrentThread(2000);
        }
    }

    private void swipeSingleEncounter() {
        if (!isVerified() || !isOnline()) {
            skipAtTheBottom();
        }

        if (!profileHasMoreThanOnePhoto()) {
            skipAtTheBottom();
        }

        goToProfile();
        Utils.sleepCurrentThread(500);

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
        WebElement photoCount = driver().findElement(By.className("js-gallery-photo-total"));
        String count = photoCount.getText();
        return Integer.valueOf(count) > 1;
    }


    private boolean isRightProfile() {
        return isSameLocation()
                && !infoContainsBlackListedWords()
                && hasNoKids();
    }

    private boolean isSameLocation() {
        WebElement location = driver().findElement(By.className("js-location-label"));
        return location.getText().trim().equalsIgnoreCase(city);
    }

    private boolean infoContainsBlackListedWords() {
        if (separatedBlackListedWords == null) {
            return false;
        }

        WebElement info = wrappedDriver().findElementByClassName("profile-section__txt");
        if (info == null) {
            return false;
        }

        String infoText = info.getText().toLowerCase();
        for (String word : separatedBlackListedWords) {
            if (infoText.contains(word)) {
                return true;
            }
        }

        return false;
    }

    private boolean hasNoKids() {

        return true;
    }

    private void goToProfile() {
        WebElement profileHeader = driver().findElement(By.className("profile-header__user"));
        profileHeader.click();
    }

    private void skipAtTheBottom() {
        WebElement skip = driver().findElement(By.className("profile-action--no"));
        skip.click();
    }

    private void skitAtTheTop() {
        WebElement skip = driver().findElement(By.className("profile-header__vote-item--no"));
        skip.click();
    }

    private void likeAtTheTop() {
        WebElement like = driver().findElement(By.className("profile-header__vote-item--yes"));
        like.click();
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
