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

    private int likes;


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
        Utils.sleepCurrentThread(1500);

        while (likes < 50) {
            swipeSingleEncounter();

            Utils.sleepCurrentThread(500);

            checkForOverlay();

            Utils.sleepCurrentThread(500);
        }
    }

    private void swipeSingleEncounter() {
        if (!isVerified() || !isOnline()) {
            skipAtTheBottom();
            return;
        }

        if (!profileHasMoreThanOnePhoto()) {
            skipAtTheBottom();
            return;
        }

        goToProfile();
        Utils.sleepCurrentThread(500);

        if (!isRightProfile()) {
            skitAtTheTop();
            return;
        }

        likeAtTheTop();
    }

    private void checkForOverlay() {
        WebElement overlay = wrappedDriver().findElementByClassName("ovl__frame");
        if (overlay != null) {
            closeOverlay();
        }
    }

    private void closeOverlay() {
        WebElement skipNotify = driver().findElement(By.className("js-chrome-pushes-deny"));
        skipNotify.click();
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
                && !infoContainsBlackListedWords();
//              && !hasKids();
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

    // DISABLED TILL FIXED
    private boolean hasKids() {
        WebElement personalInfo = wrappedDriver().findElementByClassName("js-profile-personal-info-container");
        if (personalInfo == null) {
            return false;
        }

        List<WebElement> personalInfoElements = personalInfo.findElements(By.className("form-row"));
        for (WebElement infoRow : personalInfoElements) {
            // FIXME: for some reason it does not work
            WebElement infoLabel = infoRow.findElement(By.className("form-label"));

            if (infoLabel.getText().equalsIgnoreCase("Kids")) {
                WebElement infoValue = infoRow.findElement(By.className("info-label"));
                return infoValue.getText().toLowerCase().contains("have");
            }
        }

        return false;
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

        likes++;
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
