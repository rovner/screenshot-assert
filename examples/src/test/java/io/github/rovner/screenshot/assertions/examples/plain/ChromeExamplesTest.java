package io.github.rovner.screenshot.assertions.examples.plain;


import io.github.rovner.screenshot.assertions.core.ScreenshotAssert;
import io.github.rovner.screenshot.assertions.core.ScreenshotAssert.ScreenshotAssertBuilder;
import io.github.rovner.screenshot.assertions.core.allure.DefaultAllureListener;
import io.github.rovner.screenshot.assertions.core.diff.DefaultImageDiffer;
import io.github.rovner.screenshot.assertions.core.screenshot.Screenshots;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;

import static io.github.rovner.screenshot.assertions.core.ignoring.Ignorings.*;
import static io.github.rovner.screenshot.assertions.core.screenshot.Screenshots.screenshotOfWholePage;

@Epic("Screenshot asserts")
@Feature("No framework")
@Story("Chrome")
public class ChromeExamplesTest {
    private WebDriver wd;

    private ScreenshotAssertBuilder screenshotAssertBuilder;
    private final By textBy = By.cssSelector(".home-hero .h1-mktg");

    @BeforeEach
    void beforeEach() throws IOException {
        wd = new RemoteWebDriver(new URL("http://localhost:9515/"), new ChromeOptions());
        wd.manage().window().setSize(new Dimension(800, 600));
        wd.get("https://github.com/");

        screenshotAssertBuilder = ScreenshotAssert.builder()
                .webDriver(wd)
                .references(Paths.get("src/test/resources/references/io.github.rovner.screenshot.assertions.examples.plain.ChromeExamplesTest"))
                .imageDiffer(new DefaultImageDiffer())
                .allureListener(new DefaultAllureListener());
    }

    @AfterEach
    void afterEach() {
        wd.quit();
    }

    @Test
    @DisplayName("Full page screenshot")
    void testFullPageScreenshot() {
        screenshotAssertBuilder
                .screenshot(screenshotOfWholePage()).build()
                .isEqualToReferenceId("full_page");
    }

    @Test
    @DisplayName("Screenshot with diff")
    void testScreenshotWithDiff() {
        screenshotAssertBuilder.screenshot(screenshotOfWholePage()).build()
                .isEqualToReferenceId("diff");
    }

    @Test
    @DisplayName("Screenshot with area ignoring")
    void testScreenshotWithAreaIgnoring() {
        screenshotAssertBuilder.screenshot(screenshotOfWholePage()).build()
                .ignoring(area(20, 140, 640, 120))
                .isEqualToReferenceId("diff");
    }

    @Test
    @DisplayName("Screenshot with element ignoring")
    void testScreenshotWithElementIgnoring() {
        WebElement element = wd.findElement(textBy);
        screenshotAssertBuilder.screenshot(screenshotOfWholePage()).build()
                .ignoring(element(element))
                .isEqualToReferenceId("diff");
    }

    @Test
    @DisplayName("Screenshot with by ignoring")
    void testScreenshotWithElementByIgnoring() {
        screenshotAssertBuilder.screenshot(screenshotOfWholePage()).build()
                .ignoring(elementsBy(textBy))
                .isEqualToReferenceId("diff");
    }

    @Test
    @DisplayName("Screenshot with hash code ignoring")
    void testScreenshotWithHashCodeIgnoring() {
        screenshotAssertBuilder.screenshot(screenshotOfWholePage()).build()
                .ignoring(hash(169794039))
                .isEqualToReferenceId("diff");
    }

    @Test
    @DisplayName("Area screenshot")
    void testAreaScreenshot() {
        screenshotAssertBuilder.screenshot(Screenshots.screenshotOfArea(20, 140, 640, 120)).build()
                .isEqualToReferenceId("area");
    }

    @Test
    @DisplayName("Element screenshot")
    void testElementScreenshot() {
        WebElement element = wd.findElement(textBy);
        screenshotAssertBuilder.screenshot(Screenshots.screenshotOfElement(element)).build()
                .isEqualToReferenceId("element");
    }

    @Test
    @DisplayName("Element found by screenshot")
    void testElementByScreenshot() {
        screenshotAssertBuilder.screenshot(Screenshots.screenshotOfElementFoundBy(textBy)).build()
                .isEqualToReferenceId("element");
    }
}
