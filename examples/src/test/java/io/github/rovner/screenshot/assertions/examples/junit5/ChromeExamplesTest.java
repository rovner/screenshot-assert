package io.github.rovner.screenshot.assertions.examples.junit5;


import io.github.rovner.screenshot.assertions.core.ignoring.Ignorings;
import io.github.rovner.screenshot.assertions.core.screenshot.Screenshots;
import io.github.rovner.screenshot.assertions.junit.ScreenshotAssertExtension;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.IOException;
import java.net.URL;

import static io.github.rovner.screenshot.assertions.core.ignoring.Ignorings.*;
import static io.github.rovner.screenshot.assertions.core.screenshot.Screenshots.screenshotOfWholePage;

@Epic("Screenshot asserts")
@Feature("Junit5")
@Story("Chrome")
public class ChromeExamplesTest {
    private WebDriver wd;

    @RegisterExtension
    private final ScreenshotAssertExtension screenshotAssert = new ScreenshotAssertExtension(() -> wd);
    private final By textBy = By.cssSelector(".home-hero .h1-mktg");

    @BeforeEach
    void beforeEach() throws IOException {
        wd = new RemoteWebDriver(new URL("http://localhost:9515/"), new ChromeOptions());
        wd.manage().window().setSize(new Dimension(800, 600));
        wd.get("https://github.com/");
    }

    @AfterEach
    void afterEach() {
        wd.quit();
    }

    @Test
    @DisplayName("Full page screenshot")
    void testFullPageScreenshot() {
        screenshotAssert.assertThat(screenshotOfWholePage())
                .isEqualToReferenceId("full_page");
    }

    @Test
    @DisplayName("Screenshot with diff")
    void testScreenshotWithDiff() {
        screenshotAssert.assertThat(screenshotOfWholePage())
                .isEqualToReferenceId("diff");
    }

    @Test
    @DisplayName("Screenshot with area ignoring")
    void testScreenshotWithAreaIgnoring() {
        screenshotAssert.assertThat(screenshotOfWholePage())
                .ignoring(area(20, 140, 640, 120))
                .isEqualToReferenceId("area");
    }

    @Test
    @DisplayName("Screenshot with element ignoring")
    void testScreenshotWithElementIgnoring() {
        WebElement element = wd.findElement(textBy);
        screenshotAssert.assertThat(screenshotOfWholePage())
                .ignoring(element(element))
                .isEqualToReferenceId("element");
    }

    @Test
    @DisplayName("Screenshot with by ignoring")
    void testScreenshotWithElementByIgnoring() {
        screenshotAssert.assertThat(screenshotOfWholePage())
                .ignoring(elementsBy(textBy))
                .isEqualToReferenceId("by");
    }

    @Test
    @DisplayName("Screenshot with hash code ignoring")
    void testScreenshotWithHashCodeIgnoring() {
        screenshotAssert.assertThat(screenshotOfWholePage())
                .ignoring(hash(169794039))
                .isEqualToReferenceId("hash");
    }

    @Test
    @DisplayName("Area screenshot")
    void testAreaScreenshot() {
        screenshotAssert.assertThat(Screenshots.screenshotOfArea(20, 140, 640, 120))
                .isEqualToReferenceId("area");
    }

    @Test
    @DisplayName("Element screenshot")
    void testElementScreenshot() {
        WebElement element = wd.findElement(textBy);
        screenshotAssert.assertThat(Screenshots.screenshotOfElement(element))
                .isEqualToReferenceId("element");
    }

    @Test
    @DisplayName("Element found by screenshot")
    void testElementByScreenshot() {
        screenshotAssert.assertThat(Screenshots.screenshotOfElementFoundBy(textBy))
                .isEqualToReferenceId("by");
    }
}