package io.github.rovner.screenshot.assertions.examples.utils;

import org.junit.jupiter.api.AfterEach;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import static java.time.Duration.ofSeconds;
import static org.openqa.selenium.By.cssSelector;
import static org.openqa.selenium.support.ui.ExpectedConditions.invisibilityOfElementLocated;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

public class BaseTest {
    protected WebDriver wd;

    @AfterEach
    void afterEach() {
        wd.quit();
    }

    protected final By textSelector = cssSelector(".home-hero .h1-mktg");
    protected final By animationSelector = cssSelector(".home-globe-container");

    protected void goToGithubMainPage() {
        wd.get("https://github.com/");
        new WebDriverWait(wd, ofSeconds(60))
                .until(visibilityOfElementLocated(animationSelector));
    }

    protected void goToGithubMarketplacePage() {
        wd.get("https://github.com/marketplace?category=api-management&type=apps&verification=verified_creator");
        By loading = By.cssSelector("[src*='/marketplace/search']");
        new WebDriverWait(wd, ofSeconds(60))
                .until(invisibilityOfElementLocated(loading));
    }
}
